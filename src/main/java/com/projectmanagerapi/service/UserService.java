package com.projectmanagerapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectmanagerapi.entity.UserEntity;
import com.projectmanagerapi.model.AllUsersResponseModel;
import com.projectmanagerapi.repository.UserRepository;

@Service("userService")
public class UserService {
	
	@Autowired
	public UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public List<AllUsersResponseModel> getAllUsers() {
		return userRepository.getAllUsers();
	}
	
	public UserEntity[] getUserByEmployeeId(Integer employeeId) {
		return userRepository.getForEmployeeId(employeeId);
	}
	
	public void addUser(UserEntity user) {
		userRepository.save(user);
	}
	
	public Integer dupCheck(Integer employeeId) {
		return userRepository.getByEmployeeId(employeeId);
	}
	
	public UserEntity getByUserId(Integer userId) {
		return userRepository.getForUserId(userId);
	}
	
	public Integer userCheck(String fname, String lname) {
		return userRepository.getByEmployeeName(fname, lname);
	}
	
	public UserEntity getManager(Integer projectId) {
		return userRepository.getByProjectId(projectId);
	}
	
	public UserEntity getOwner(Integer projectId, Integer taskId) {
		return userRepository.getForParentTask(projectId, taskId);
	}
	
	public void deleteUserEntity(UserEntity user) {
		userRepository.delete(user);
	}
	
	public void deleteUser(Integer employeeId) {
		userRepository.deleteUser(employeeId);
	}
}
