package com.test.spring.main;

public interface BeanFactory {

	/**
	 * 根据bean的name获取bean对象
	 * 
	 * @param beanName
	 * @return
	 */
	Object getBean(String beanName);

}
