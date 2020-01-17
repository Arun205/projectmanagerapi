package com.projectmanagerapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectmanagerapi.entity.TaskEntity;
import com.projectmanagerapi.repository.TaskRepository;

@Service("taskService")
public class TaskService {
	
	@Autowired
	TaskRepository taskRepository;
	
	public List<TaskEntity> getTasksForParent(Integer parentId) {
		return taskRepository.getTasksForParent(parentId);
	}
	
	public Integer dupCheck(String task) {
		return taskRepository.getByTask(task);
	}
	
	public Integer addTask(TaskEntity task) {
		taskRepository.save(task);
		return task.getTaskId();
	}
	
	public TaskEntity getTask(String task) {
		return taskRepository.getTask(task);
	}
	
	public Integer getTaskCount(Integer parentId) {
		return taskRepository.getByParent(parentId);
	}

	public Integer getCompletedCount(Integer projectId) {
		return taskRepository.getCompletedTasks(projectId, "completed");
	}
}
