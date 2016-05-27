package com.test.spring.bean;

public class Employee {

	private User user;

	public Employee() {
		System.out.println("Employee对象创建。。。");
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
