package com.projectmanagerapi.model;

import java.util.Date;

public class AllProjectResponseModel {
	
	public String project;
	public Integer completed;
	public Integer tasksCount;
	public Date startDate;
	public Date endDate;
	public Integer priority;
	public String managerFirstName;
	public String managerLastName;
	public AllProjectResponseModel(String project, Integer completed, Integer tasksCount, Date startDate, Date endDate, Integer priority, String managerFirstName, String managerLastName) {
		super();
		this.project = project;
		this.completed = completed;
		this.tasksCount = tasksCount;
		this.startDate = startDate;
		this.endDate = endDate;
		this.priority = priority;
		this.managerFirstName = managerFirstName;
		this.managerLastName = managerLastName;
	}
	
}
