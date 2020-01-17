package com.projectmanagerapi.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

@Entity
@Table(name="task")
public class TaskEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="task_id")
	private Integer taskId;
	
	@Column(name="parent_id")
	private Integer parentId;
	
	@Column(name="project_id")
	@NotNull
	private Integer projectId;
	
	@Column(name="task")
	private String task;
	
	@Column(name="start_date")
	@Type(type="date")
	private Date startDate;
	
	@Column(name="end_date")
	@Type(type="date")
	private Date endDate;
	
	@Column(name="priority")
	private Integer priority;
	
	@Column(name="status")
	private String status;

	public TaskEntity() {
		super();
	}

	public Integer getTaskId() {
		return taskId;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public TaskEntity(Integer taskId, Integer parentId, @NotNull Integer projectId, String task, Date startDate,
			Date endDate, Integer priority, String status) {
		super();
		this.taskId = taskId;
		this.parentId = parentId;
		this.projectId = projectId;
		this.task = task;
		this.startDate = startDate;
		this.endDate = endDate;
		this.priority = priority;
		this.status = status;
	}
	

}
