package com.projectmanagerapi.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.projectmanagerapi.entity.ParentEntity;
import com.projectmanagerapi.entity.ProjectEntity;
import com.projectmanagerapi.entity.TaskEntity;
import com.projectmanagerapi.entity.UserEntity;
import com.projectmanagerapi.model.AddProjectRequestModel;
import com.projectmanagerapi.model.AddTaskRequestModel;
import com.projectmanagerapi.model.AddUserRequestModel;
import com.projectmanagerapi.model.AllProjectResponseModel;
import com.projectmanagerapi.model.AllTaskResponseModel;
import com.projectmanagerapi.model.AllUsersResponseModel;
import com.projectmanagerapi.service.ParentService;
import com.projectmanagerapi.service.ProjectService;
import com.projectmanagerapi.service.TaskService;
import com.projectmanagerapi.service.UserService;

@Controller
/* @CrossOrigin(origins="http://localhost:4200") */
@CrossOrigin(origins="http://172.18.2.50:6201")
public class ProjectManagerController {

	@Autowired
	private UserService userService;
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	ParentService parentService;
	
	private Logger LOGGER = LoggerFactory.getLogger(ProjectManagerController.class);
	
	@ResponseBody
	@RequestMapping(value={"/allUsers"}, method=RequestMethod.GET)
	public List<AllUsersResponseModel> getAllUsers() {
		LOGGER.info("All Users list requested");
		return this.userService.getAllUsers();
	}
	
	@RequestMapping(value={"/addUser"}, method=RequestMethod.POST)
	public ResponseEntity<String> addUser(@RequestBody AddUserRequestModel user) {
		LOGGER.info("Add User requested for " + user.getFirstName());
		if (user.getFirstName().trim().length() == 0 || 
				user.getFirstName() == null ||
				user.getEmployeeId() == null) {
			LOGGER.error("BAD REQUEST to add user");
			return new ResponseEntity<String>("invalid user", HttpStatus.BAD_REQUEST);
		}
		
		if (this.userService.dupCheck(user.getEmployeeId()) > 0) {
			LOGGER.error("Employee id already exists " + user.getEmployeeId());
			return new ResponseEntity<String>("duplicate employeeid", HttpStatus.BAD_REQUEST);
		}

		UserEntity newUser = new UserEntity();
		newUser.setFirstName(user.getFirstName());
		newUser.setLastName(user.getLastName());
		newUser.setEmployeeId(user.getEmployeeId());
		this.userService.addUser(newUser);
		LOGGER.info("User added successfully");
		return new ResponseEntity<String>("user added", HttpStatus.OK);
	}
	
	@RequestMapping(value={"/editUser"}, method=RequestMethod.POST)
	public ResponseEntity<String> editUser(@RequestBody AddUserRequestModel user) {
		LOGGER.info("Edit User requested for " + user.getFirstName());
		if (user.getFirstName().trim().length() == 0 || 
				user.getFirstName() == null ||
				user.getEmployeeId() == null) {
			LOGGER.error("BAD REQUEST to edit user");
			return new ResponseEntity<String>("invalid user", HttpStatus.BAD_REQUEST);
		}
		
		if (this.userService.dupCheck(user.getEmployeeId()) == 0) {
			LOGGER.error("Employee id does not exist " + user.getEmployeeId());
			return new ResponseEntity<String>("invalid employeeid", HttpStatus.BAD_REQUEST);
		}
		
		UserEntity[] oldUsers = userService.getUserByEmployeeId(user.getEmployeeId());
		for (int i=0; i<oldUsers.length; i++) {
			UserEntity eachRow = new UserEntity();
			eachRow = userService.getByUserId(oldUsers[i].getUserId());
			eachRow.setFirstName(user.getFirstName());
			eachRow.setLastName(user.getLastName());
			eachRow.setEmployeeId(user.getEmployeeId());
			this.userService.addUser(eachRow);
		}
		LOGGER.info("User updated successfully");
		return new ResponseEntity<String>("user updated", HttpStatus.OK);
	}

	@RequestMapping(value= {"/deleteUser"}, method=RequestMethod.POST)
	public ResponseEntity<String> deleteUser(@RequestBody AddUserRequestModel user) {
		LOGGER.info("Add User requested for " + user.getFirstName());
		if (this.userService.dupCheck(user.getEmployeeId()) == 0) {
			LOGGER.error("Employee id does not exist " + user.getEmployeeId());
			return new ResponseEntity<String>("invalid employeeid", HttpStatus.BAD_REQUEST);
		}
		userService.deleteUser(user.getEmployeeId());
		LOGGER.info("User deleted successfully");
		return new ResponseEntity<String>("user deleted", HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value={"/allProjects"}, method=RequestMethod.GET)
	public List<ProjectEntity> getAllProjects() {
		LOGGER.info("All Users list requested");
		return this.projectService.getAllProjects();
	}
	
	@ResponseBody
	@RequestMapping(value={"/allProjectsDetails"}, method=RequestMethod.GET)
	public List<AllProjectResponseModel> getAllProjectsDetails() {
		LOGGER.info("All Projects list requested");
		List<AllProjectResponseModel> response = new ArrayList<AllProjectResponseModel>();
		List<ProjectEntity> allProjects = projectService.getAllProjects(); 
		allProjects.forEach(project -> {
			String firstName = "";
			String lastName = "";
			if (userService.getManager(project.getProjectId()) != null) {
				firstName = userService.getManager(project.getProjectId()).getFirstName();
				lastName = userService.getManager(project.getProjectId()).getLastName();
			}
			response.add(new AllProjectResponseModel(project.getProject(),
					taskService.getCompletedCount(project.getProjectId()),
					taskService.getTaskCount(project.getProjectId()),
					project.getStartDate(), project.getEndDate(),
					project.getPriority(), firstName, lastName));
		});
		return response;
	}
	
	@RequestMapping(value={"/addProject"}, method=RequestMethod.POST)
	public ResponseEntity<String> addProject(@RequestBody AddProjectRequestModel project) {
		LOGGER.info("Project add requested for " + project.getProject());
		ProjectEntity projectEntity = new ProjectEntity();
		Integer employeeId = 0;
		String fname = "";
		String lname = "";
		
		if (project.getProject().trim().length() == 0) {
			LOGGER.error("Invalid project name " + project.getProject());
			return new ResponseEntity<String>("invalid project", HttpStatus.BAD_REQUEST);
		}
		
		if (this.projectService.dupCheck(project.getProject()) > 0) {
			LOGGER.error("Duplicate project name " + project.getProject());
			return new ResponseEntity<String>("duplicate project", HttpStatus.BAD_REQUEST);
		}
		
		if (project.getManager() != null && project.getManager().trim().length() > 0) {
			String[] manager = project.getManager().split("\\,");
			lname = manager[0].trim();
			if (manager[1] != null) {
				fname = manager[1].trim();
			}
			employeeId = this.userService.userCheck(fname, lname);
			if (employeeId == null) {
				LOGGER.error("Invalid manager " + project.getManager());
				return new ResponseEntity<String>("invalid manager", HttpStatus.BAD_REQUEST);
			}
		}
				
		projectEntity.setProject(project.getProject());
		projectEntity.setStartDate(project.getStartDate());
		projectEntity.setEndDate(project.getEndDate());
		projectEntity.setPriority(project.getPriority());
		Integer projectId = this.projectService.addProject(projectEntity);
		LOGGER.info("New project added with id " + projectId);
		
		if (project.getManager() != null && project.getManager().trim().length() > 0) {
			UserEntity newManager = new UserEntity();
			newManager.setFirstName(fname);
			newManager.setLastName(lname);
			newManager.setEmployeeId(employeeId);
			newManager.setProjectId(projectId);
			this.userService.addUser(newManager);
			LOGGER.info("Manager added successfully");
		}
		return new ResponseEntity<String>("project added", HttpStatus.OK);
	}
	
	@RequestMapping(value={"/editProject"}, method=RequestMethod.POST)
	public ResponseEntity<String> editProject(@RequestBody AddProjectRequestModel project) {
		LOGGER.info("Project edit requested for " + project.getProject());
		Integer employeeId = 0;
		String fname = "";
		String lname = "";
		
		if (project.getOldProject().trim().length() == 0) {
			LOGGER.error("Invalid old project name " + project.getOldProject());
			return new ResponseEntity<String>("invalid old project", HttpStatus.BAD_REQUEST);
		}
		
		if (project.getProject().trim().length() == 0) {
			LOGGER.error("Invalid project name " + project.getProject());
			return new ResponseEntity<String>("invalid project", HttpStatus.BAD_REQUEST);
		}
		
		if (projectService.dupCheck(project.getOldProject()) == 0) {
			LOGGER.error("Project not found " + project.getOldProject());
			return new ResponseEntity<String>("project not found", HttpStatus.BAD_REQUEST);
		}
		
		if (project.getManager() != null && project.getManager().trim().length() > 0) {
			String[] manager = project.getManager().split("\\,");
			lname = manager[0].trim();
			if (manager[1] != null) {
				fname = manager[1].trim();
			}
			employeeId = this.userService.userCheck(fname, lname);
			if (employeeId == null) {
				LOGGER.error("Invalid manager " + project.getManager());
				return new ResponseEntity<String>("invalid manager", HttpStatus.BAD_REQUEST);
			}
		}
		
		ProjectEntity projectEntity = projectService.getProject(project.getOldProject());
		projectEntity.setProject(project.getProject());
		projectEntity.setStartDate(project.getStartDate());
		projectEntity.setEndDate(project.getEndDate());
		projectEntity.setPriority(project.getPriority());
		projectService.addProject(projectEntity);
		LOGGER.info("Project updated " + project.getOldProject());
		
		if (userService.getManager(projectEntity.getProjectId()) != null) {
			userService.deleteUserEntity(userService.getManager(projectEntity.getProjectId()));
			LOGGER.info("Old Manager removed successfully");
		}
		
		if (project.getManager() != null && project.getManager().trim().length() > 0) {
			UserEntity newManager = new UserEntity();
			newManager.setFirstName(fname);
			newManager.setLastName(lname);
			newManager.setEmployeeId(employeeId);
			newManager.setProjectId(projectEntity.getProjectId());
			this.userService.addUser(newManager);
			LOGGER.info("New Manager added successfully");
		}
		return new ResponseEntity<String>("project updated", HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value={"/allTasksDetails"}, method=RequestMethod.GET)
	public List<AllTaskResponseModel> getAllTasksDetails(@RequestParam String project) {
		LOGGER.info("All tasks list requested for project " + project);
		Integer projectId = projectService.getProject(project).getProjectId();
		LOGGER.info("projectId " + projectId);
		List<AllTaskResponseModel> response = new ArrayList<AllTaskResponseModel>();
		List<TaskEntity> allTasks = taskService.getTasksForParent(projectId); 
		LOGGER.info("allTasks " + allTasks);
		allTasks.forEach(task -> {
			String firstName = "";
			String lastName = "";
			if (userService.getOwner(task.getProjectId(), task.getTaskId()) != null) {
				firstName = userService.getOwner(task.getProjectId(), task.getTaskId()).getFirstName();
				lastName = userService.getOwner(task.getProjectId(), task.getTaskId()).getLastName();
			}
			
			String parent = "";
			if (parentService.getParent(task.getParentId()) != null) {
				parent = parentService.getParent(task.getParentId()).getParent();
			}
			response.add(new AllTaskResponseModel(projectService.getProjectforId(task.getProjectId()).getProject(),
					task.getTask(), task.getPriority(), parent,
					task.getStartDate(), task.getEndDate(), firstName, lastName, task.getStatus()));
		});
		return response;
	}
	
	@RequestMapping(value={"/addTask"}, method=RequestMethod.POST)
	public ResponseEntity<String> addTask(@RequestBody AddTaskRequestModel task) {
		LOGGER.info("Task add requested for " + task.getTask());
		TaskEntity taskEntity = new TaskEntity();
		String fname = "";
		String lname = "";
		Integer projectId = 0;
		Integer parentId = 0;
		Integer employeeId = 0;
		Integer taskId = 0;
		
		if (task.getProject().trim().length() == 0) {
			LOGGER.error("Blank project name " + task.getProject());
			return new ResponseEntity<String>("invalid project", HttpStatus.BAD_REQUEST);
		}
		
		if (task.getTask().trim().length() == 0) {
			LOGGER.error("Blank task name " + task.getTask());
			return new ResponseEntity<String>("invalid task", HttpStatus.BAD_REQUEST);
		}
		
		if (this.taskService.dupCheck(task.getTask()) > 0) {
			LOGGER.error("Duplicate task name " + task.getTask());
			return new ResponseEntity<String>("duplicate task", HttpStatus.BAD_REQUEST);
		}
		
		if (task.getParentTask().trim().length() > 0 && parentService.dupCheck(task.getParentTask()) == 0) {
			LOGGER.error("Parent task not found " + task.getParentTask());
			return new ResponseEntity<String>("parent task not found", HttpStatus.BAD_REQUEST);
		}
		
		if (projectService.dupCheck(task.getProject()) == 0) {
			LOGGER.error("Project not found " + task.getProject());
			return new ResponseEntity<String>("project not found", HttpStatus.BAD_REQUEST);
		}
		
		if (task.getUser() != null && task.getUser().trim().length() > 0) {
			String[] user = task.getUser().split("\\,");
			lname = user[0].trim();
			if (user[1] != null) {
				fname = user[1].trim();
			}
			employeeId = this.userService.userCheck(fname, lname);
			if (employeeId == null) {
				LOGGER.error("Invalid user " + task.getUser());
				return new ResponseEntity<String>("invalid user", HttpStatus.BAD_REQUEST);
			}
		}
		
		if (task.getParentTask().trim().length() > 0) {
			parentId = parentService.getParentId(task.getParentTask());
		}
		
		projectId = projectService.getProject(task.getProject()).getProjectId();
		taskEntity.setTask(task.getTask());
		taskEntity.setParentId(parentId);
		taskEntity.setProjectId(projectId);
		taskEntity.setStartDate(task.getStartDate());
		taskEntity.setEndDate(task.getEndDate());
		taskEntity.setPriority(task.getPriority());
		taskEntity.setStatus("open");
		taskId = taskService.addTask(taskEntity);
		
		if (task.getUser() != null && task.getUser().trim().length() > 0) {
			UserEntity assignedUser = new UserEntity();
			assignedUser.setFirstName(fname);
			assignedUser.setLastName(lname);
			assignedUser.setEmployeeId(employeeId);
			assignedUser.setProjectId(projectId);
			assignedUser.setTaskId(taskId);
			this.userService.addUser(assignedUser);
			LOGGER.info("User added successfully");
		}
		return new ResponseEntity<String>("task added", HttpStatus.OK);
	}
	
	@RequestMapping(value={"/editTask"}, method=RequestMethod.POST)
	public ResponseEntity<String> editTask(@RequestBody AddTaskRequestModel task) {
		LOGGER.info("Edit task requested for " + task.getTask());
		String fname = "";
		String lname = "";
		Integer projectId = 0;
		Integer parentId = 0;
		Integer employeeId = 0;
		Integer taskId = 0;
		
		if (task.getProject().trim().length() == 0) {
			LOGGER.error("Blank project name " + task.getProject());
			return new ResponseEntity<String>("invalid project", HttpStatus.BAD_REQUEST);
		}
		
		if (task.getOldTask().trim().length() == 0) {
			LOGGER.error("Blank task name " + task.getOldTask());
			return new ResponseEntity<String>("invalid task", HttpStatus.BAD_REQUEST);
		}
		
		if (this.taskService.dupCheck(task.getOldTask()) == 0) {
			LOGGER.error("Task does not exit " + task.getOldTask());
			return new ResponseEntity<String>("task not found", HttpStatus.BAD_REQUEST);
		}
		
		if (task.getParentTask().trim().length() > 0 && parentService.dupCheck(task.getParentTask()) == 0) {
			LOGGER.error("Parent task not found " + task.getParentTask());
			return new ResponseEntity<String>("parent task not found", HttpStatus.BAD_REQUEST);
		}
		
		if (projectService.dupCheck(task.getProject()) == 0) {
			LOGGER.error("Project not found " + task.getProject());
			return new ResponseEntity<String>("project not found", HttpStatus.BAD_REQUEST);
		}
		
		if (task.getUser() != null && task.getUser().trim().length() > 0) {
			String[] user = task.getUser().split("\\,");
			lname = user[0].trim();
			if (user[1] != null) {
				fname = user[1].trim();
			}
			employeeId = this.userService.userCheck(fname, lname);
			if (employeeId == null) {
				LOGGER.error("Invalid user " + task.getUser());
				return new ResponseEntity<String>("invalid user", HttpStatus.BAD_REQUEST);
			}
		}
		
		if (task.getParentTask().trim().length() > 0) {
			parentId = parentService.getParentId(task.getParentTask());
		}
		
		projectId = projectService.getProject(task.getProject()).getProjectId();
		TaskEntity taskEntity = taskService.getTask(task.getOldTask());
		taskEntity.setTask(task.getTask());
		taskEntity.setParentId(parentId);
		taskEntity.setProjectId(projectId);
		taskEntity.setStartDate(task.getStartDate());
		taskEntity.setEndDate(task.getEndDate());
		taskEntity.setPriority(task.getPriority());
		taskEntity.setStatus(task.getStatus());
		taskId = taskService.addTask(taskEntity);
		
		if (userService.getOwner(taskEntity.getProjectId(), taskEntity.getTaskId()) != null) {
			userService.deleteUserEntity(userService.getOwner(taskEntity.getProjectId(), taskEntity.getTaskId()));
			LOGGER.info("Old Owner removed successfully");
		}
		
		if (task.getUser() != null && task.getUser().trim().length() > 0) {
			UserEntity assignedUser = new UserEntity();
			assignedUser.setFirstName(fname);
			assignedUser.setLastName(lname);
			assignedUser.setEmployeeId(employeeId);
			assignedUser.setProjectId(projectId);
			assignedUser.setTaskId(taskId);
			this.userService.addUser(assignedUser);
			LOGGER.info("New Owner added successfully");
		}
		
		return new ResponseEntity<String>("task updated", HttpStatus.OK);
	}
	
	@RequestMapping(value= {"/addParent"}, method=RequestMethod.POST)
	public ResponseEntity<String> addParent(@RequestBody ParentEntity parent) {
		LOGGER.info("Parent Add requested for " + parent.getParent());
		if (parent.getParent().trim().length() == 0) {
			LOGGER.error("Invalid parent " + parent);
			return new ResponseEntity<String>("invalid parent", HttpStatus.BAD_REQUEST);
		}
		
		if (parentService.dupCheck(parent.getParent()) > 0) {
			LOGGER.error("Duplicate parent " + parent);
			return new ResponseEntity<String>("duplicate parent", HttpStatus.BAD_REQUEST);
		}
		
		ParentEntity parentEntity = new ParentEntity();
		parentEntity.setParent(parent.getParent());
		parentService.addParent(parentEntity);
		
		return new ResponseEntity<String>("parent added", HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value={"/allParents"}, method=RequestMethod.GET)
	public List<ParentEntity> getAllParents() {
		LOGGER.info("All Parent Projects list requested");
		return this.parentService.getAllParents();
	}
	
}
