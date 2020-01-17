package com.projectmanagerapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.projectmanagerapi.entity.UserEntity;
import com.projectmanagerapi.model.AllUsersResponseModel;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
	
	@Query("select count(u) from UserEntity u where u.employeeId = :employeeId "
			+ "and u.projectId = null and u.taskId = null")
	public Integer getByEmployeeId(@Param("employeeId") Integer employeeId);
	
	@Query("select new com.projectmanagerapi.model.AllUsersResponseModel(u.employeeId, u.firstName, u.lastName) from UserEntity u group by employeeId, firstName, lastName")
	public List<AllUsersResponseModel> getAllUsers();

	@Query("select distinct u.employeeId from UserEntity u where u.firstName=:firstName and u.lastName=:lastName")
	public Integer getByEmployeeName(@Param("firstName") String firstName, @Param("lastName") String lastName);

	@Query("select u from UserEntity u where u.employeeId = :employeeId")
	public UserEntity[] getForEmployeeId(@Param("employeeId") Integer employeeId);
	
	@Query("select u from UserEntity u where u.projectId = :projectId "
			+ "and u.taskId = null")
	public UserEntity getByProjectId(@Param("projectId") Integer projectId);
	
	@Query("select u from UserEntity u where u.userId = :userId")
	public UserEntity getForUserId(@Param("userId") Integer userId);
	
	@Query("select u from UserEntity u where u.projectId = :projectId and u.taskId = :taskId")
	public UserEntity getForParentTask(@Param("projectId") Integer projectId, @Param("taskId") Integer taskId);
	
	@Transactional
	@Modifying
	@Query("delete from UserEntity where employeeId = :employeeId")
	public void deleteUser(@Param("employeeId") Integer employeeId);
}
