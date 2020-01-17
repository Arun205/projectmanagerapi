package com.projectmanagerapi.model;

import java.util.Date;

public class AllTaskResponseModel {
	
	public String project;
	public String task;
	public Integer priority;
	public String parentTask;
	public Date startDate;
	public Date endDate;
	public String userFirstName;
	public String userLastName;
	public String status;
	
	public AllTaskResponseModel(String project, String task, Integer priority, String parentTask, Date startDate,
			Date endDate, String userFirstName, String userLastName, String status) {
		super();
		this.project = project;
		this.task = task;
		this.priority = priority;
		this.parentTask = parentTask;
		this.startDate = startDate;
		this.endDate = endDate;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
		this.status = status;
	}
	
}
