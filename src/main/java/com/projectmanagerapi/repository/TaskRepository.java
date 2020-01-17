package com.projectmanagerapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projectmanagerapi.entity.TaskEntity;

@Repository("taskRepository")
public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {
	
	@Query("select count(t) from TaskEntity t where t.task = :task")
	public Integer getByTask(@Param("task") String task);
	
	@Query("select t from TaskEntity t where t.task = :task")
	public TaskEntity getTask(@Param("task") String task);
	
	@Query("select count(t) from TaskEntity t where t.projectId = :projectId")
	public Integer getByParent(@Param("projectId") Integer projectd);

	@Query("select count(t) from TaskEntity t where t.projectId = :projectId "
			+ "and status = :completed")
	public Integer getCompletedTasks(@Param("projectId") Integer projectId, @Param("completed") String completed);
	
	@Query("select t from TaskEntity t where t.projectId = :projectId")
	public List<TaskEntity> getTasksForParent(@Param("projectId") Integer projectId);
}
