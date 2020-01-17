package com.projectmanagerapi.model;

import java.util.Date;

public class AddProjectRequestModel {
	
	private String oldProject;
	private String project;
	private Date startDate;
	private Date endDate;
	private Integer priority;
	private String manager;
	
	public AddProjectRequestModel(String oldProject, String project, Date startDate, Date endDate, Integer priority, String manager) {
		super();
		this.oldProject = oldProject;
		this.project = project;
		this.startDate = startDate;
		this.endDate = endDate;
		this.priority = priority;
		this.manager = manager;
	}

	public AddProjectRequestModel() {
		super();
	}
	
	public String getOldProject() {
		return oldProject;
	}

	public void setOldProject(String oldProject) {
		this.oldProject = oldProject;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
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

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

}
