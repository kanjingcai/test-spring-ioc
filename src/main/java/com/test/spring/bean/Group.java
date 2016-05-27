package com.test.spring.bean;

public class Group {

	private Employee employee;

	public Group() {
		System.out.println("Group对象创建。。。");
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

}
