package com.projectmanagerapi.model;

public class AddUserRequestModel {
	
	private String firstName;
	private String lastName;
	private Integer employeeId;
	private Integer projectId;
	private Integer taskId;
	
	public AddUserRequestModel(String firstName, String lastName, Integer employeeId, Integer projectId,
			Integer taskId) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.employeeId = employeeId;
		this.projectId = projectId;
		this.taskId = taskId;
	}

	public AddUserRequestModel(String firstName, String lastName, Integer employeeId) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.employeeId = employeeId;
	}

	public AddUserRequestModel() {
		super();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	
}
