package com.test.spring.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class BeanUtils {

	public static Method getWriteMethod(Object beanObj, String name) {
		Method method = null;
		// 使用内省技术实现
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(beanObj.getClass());
			// 获取所有属性的描述器
			PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
			if (pds != null) { // 遍历属性描述器
				for (PropertyDescriptor pd : pds) {
					// 获取当前描述器的属性名称
					String pName = pd.getName();
					if (pName.equals(name)) {
						method = pd.getWriteMethod();
					}
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		if (method == null) {
			throw new RuntimeException("没有属性：" + name + "set方法");
		}
		return method;
	}

}
