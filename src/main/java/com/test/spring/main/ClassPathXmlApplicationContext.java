package com.test.spring.main;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.test.spring.config.Bean;
import com.test.spring.config.Property;
import com.test.spring.config.parse.ConfigManager;
import com.test.spring.utils.BeanUtils;

public class ClassPathXmlApplicationContext implements BeanFactory {

	private Map<String, Bean> config;

	// 使用map作为spring容器
	private Map<String, Object> context = new HashMap<>();

	@Override
	public Object getBean(String beanName) {
		Object bean = context.get(beanName);

		// 如果bean的scope配置为prototype，那么context中不会包含该bean对象
		if (bean == null) {
			bean = createBean(config.get(beanName));
		}
		return bean;
	}

	public ClassPathXmlApplicationContext(String path) {
		// 读取配置文件获得需要初始化的bean信息
		config = ConfigManager.getConfig(path);

		// 遍历初始化bean
		if (config != null) {
			for (Entry<String, Bean> entry : config.entrySet()) {
				String beanName = entry.getKey();
				Bean bean = entry.getValue();

				Object existBean = context.get(beanName);
				// 判断容器是否已经存在bean
				// bean的scope属性值为singleton，才将bean放入容器中
				if (existBean == null && "singleton".equals(bean.getScope())) {
					// 根据bean配置创建bean对象

					Object beanObj = createBean(bean);

					// 将bean放入spring容器中
					context.put(beanName, beanObj);
				}
			}
		}
	}

	private Object createBean(Bean bean) {
		String className = bean.getClassName();
		Class<?> clazz = null;
		Object beanObj = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("找不到类：" + className);
		}
		try {
			beanObj = clazz.newInstance(); // 调用空参构造
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException("类没有空构造函数：" + className);
		}

		if (bean.getProperties() != null) {
			for (Property prop : bean.getProperties()) {
				String name = prop.getName();
				String value = prop.getValue();
				/**
				 * 使用BeanUtils工具类属性注入
				 */

				// 注入两种情况，注入值类型属性方式
				if (prop.getValue() != null) { // 简单属性注入
					Map<String, String[]> paramMap = new HashMap<>();
					paramMap.put(name, new String[] { value });
					// 调用BeanUtils方法将值类型的属性注入，可以完成自动完成类型转换
					try {
						org.apache.commons.beanutils.BeanUtils.populate(
								beanObj, paramMap);
					} catch (IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
						throw new RuntimeException("属性注入异常，请检查属性：" + name);
					}
				}
				if (prop.getRef() != null) { // 其他bean注入
					// 根据属性名称获得注入属性对应的set方法
					Method setMethod = BeanUtils.getWriteMethod(beanObj, name);

					// 查找spring容器是否已经创建所依赖的bean
					Object existBean = context.get(prop.getRef());
					if (existBean == null) {// 容器不存在，需要注入bean
						existBean = createBean(config.get(prop.getRef()));

						// 初始化bean，放入容器中
						if ("singleton".equals(config.get(prop.getRef())
								.getScope())) {
							context.put(prop.getRef(), existBean);
						}
					}

					try {
						setMethod.invoke(beanObj, existBean);
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						e.printStackTrace();
						throw new RuntimeException("类属性：" + name
								+ "没有对应的set方法，或方法不正确！" + className);
					}
				}
			}
		}

		return beanObj;
	}

}
