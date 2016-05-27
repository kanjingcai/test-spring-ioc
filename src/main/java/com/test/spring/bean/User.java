package com.test.spring.bean;

public class User {

	private String name;
	
	private Integer value;
	
	public User() {
		System.out.println("User对象创建。。。");
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", value=" + value + "]";
	}
}

