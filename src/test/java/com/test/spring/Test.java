package com.test.spring;

import java.util.Map;

import com.test.spring.bean.Employee;
import com.test.spring.bean.Group;
import com.test.spring.bean.User;
import com.test.spring.config.Bean;
import com.test.spring.config.parse.ConfigManager;
import com.test.spring.main.BeanFactory;
import com.test.spring.main.ClassPathXmlApplicationContext;

public class Test {

	@org.junit.Test
	public void test() {
		
		Map<String, Bean> config = ConfigManager.getConfig("/applicationContext.xml");
		
		System.out.println(config);
	}
	
	@org.junit.Test
	public void test1() {
		
		BeanFactory bf = new ClassPathXmlApplicationContext("/applicationContext.xml");
		
		User u = (User)bf.getBean("user");
		
		User u1 = (User)bf.getBean("user");  //单例测试
		
		System.out.println(u.getName());
		System.out.println(u.getValue());
	}
	
	
	@org.junit.Test
	public void test2() {
		
		BeanFactory bf = new ClassPathXmlApplicationContext("/applicationContext.xml");
		
		Employee e = (Employee)bf.getBean("employee");
		Employee e1 = (Employee)bf.getBean("employee");
		Employee e2 = (Employee)bf.getBean("employee");
		
		System.out.println(e.getUser().getName());
	}
	
	
	@org.junit.Test
	public void test3() {
		
		BeanFactory bf = new ClassPathXmlApplicationContext("/applicationContext.xml");
		
		Group g = (Group)bf.getBean("group");
		Group g2 = (Group)bf.getBean("group");
		Group g3 = (Group)bf.getBean("group");
		Group g4 = (Group)bf.getBean("group");
		
		System.out.println(g.getEmployee().getUser().getName());
	}
	
	
}
