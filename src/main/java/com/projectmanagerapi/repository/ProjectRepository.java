package com.projectmanagerapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projectmanagerapi.entity.ProjectEntity;

@Repository("projectRepository")
public interface ProjectRepository extends JpaRepository<ProjectEntity, Integer> {
	
	@Query("select count(p) from ProjectEntity p where p.project = :project")
	public Integer getCountByProjectName(@Param("project") String project);
	
	@Query("select p from ProjectEntity p where p.project = :project")
	public ProjectEntity getByProjectName(@Param("project") String project);
	
	@Query("select p from ProjectEntity p where p.projectId = :projectId")
	public ProjectEntity getByProjectId(@Param("projectId") Integer projectId);

}
