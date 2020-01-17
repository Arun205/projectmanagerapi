package com.projectmanagerapi.model;

public class AllUsersResponseModel {
	
	public int employeeId;
	public String firstName;
	public String lastName;

	public AllUsersResponseModel(int employeeId, String firstName, String lastName) {
		super();
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;
	}

}
