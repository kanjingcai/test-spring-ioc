package com.test.spring.config.parse;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.test.spring.config.Bean;
import com.test.spring.config.Property;

/**
 * 读取配置文件并返回结果
 */
public class ConfigManager {

	@SuppressWarnings("unchecked")
	public static Map<String, Bean> getConfig(String path) {
		Map<String, Bean> map = new HashMap<>();

		SAXReader reader = new SAXReader();
		InputStream is = ConfigManager.class.getResourceAsStream(path);
		Document doc = null;
		try {
			doc = reader.read(is);
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException("配置文件有误！");
		}

		// 定义xpath表达式，取出所有bean元素
		String xpath = "//bean";
		List<Element> list = doc.selectNodes(xpath);
		if (list != null) { // 对bean元素进行遍历
			for (Element element : list) {
				Bean bean = new Bean();
				String name = element.attributeValue("name");
				String className = element.attributeValue("class");
				String scope = element.attributeValue("scope");

				bean.setName(name);
				bean.setClassName(className);
				if(scope != null) {
					bean.setScope(scope);
				}

				// 获取bean元素下所有的property子元素，将属性name/value/ref封装
				List<Element> children = element.elements("property");
				if (children != null) {
					for (Element ele : children) {
						Property prop = new Property();

						String pName = ele.attributeValue("name");
						String pValue = ele.attributeValue("value");
						String pRef = ele.attributeValue("ref");

						prop.setName(pName);
						prop.setValue(pValue);
						prop.setRef(pRef);

						bean.getProperties().add(prop);
					}
				}
				map.put(name, bean);
			}
		}
		return map;
	}

}
