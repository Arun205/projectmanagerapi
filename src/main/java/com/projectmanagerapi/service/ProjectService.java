package com.projectmanagerapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectmanagerapi.entity.ProjectEntity;
import com.projectmanagerapi.repository.ProjectRepository;

@Service("projectService")
public class ProjectService {
	
	@Autowired
	ProjectRepository projectRepository;
	
	public List<ProjectEntity> getAllProjects() {
		return projectRepository.findAll();
	}
	
	public Integer addProject(ProjectEntity projectEntity) {
		projectRepository.save(projectEntity);
		return projectEntity.getProjectId();
	}
	
	public Integer dupCheck(String project) {
		return projectRepository.getCountByProjectName(project);
	}
	
	public ProjectEntity getProject(String project) {
		return projectRepository.getByProjectName(project);
	}

	public ProjectEntity getProjectforId(Integer projectId) {
		return projectRepository.getByProjectId(projectId);
	}
	
}
