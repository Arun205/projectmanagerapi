package com.projectmanagerapi.model;

import java.util.Date;

public class AddTaskRequestModel {
	
	private String project;
	private String oldTask;
	private String task;
	private Integer priority;
	private String parentTask;
	private Date startDate;
	private Date endDate;
	private String user;
	private String status;
	
	public AddTaskRequestModel(String project, String oldTask, String task, Integer priority, String parentTask,
			Date startDate, Date endDate, String user, String status) {
		super();
		this.project = project;
		this.oldTask = oldTask;
		this.task = task;
		this.priority = priority;
		this.parentTask = parentTask;
		this.startDate = startDate;
		this.endDate = endDate;
		this.user = user;
		this.status = status;
	}

	public AddTaskRequestModel() {
		super();
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getOldTask() {
		return oldTask;
	}

	public void setOldTask(String oldTask) {
		this.oldTask = oldTask;
	}
	
	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getParentTask() {
		return parentTask;
	}

	public void setParentTask(String parentTask) {
		this.parentTask = parentTask;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
