package com.projectmanagerapi.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectmanagerapi.entity.ParentEntity;
import com.projectmanagerapi.entity.ProjectEntity;
import com.projectmanagerapi.entity.TaskEntity;
import com.projectmanagerapi.entity.UserEntity;
import com.projectmanagerapi.model.AddProjectRequestModel;
import com.projectmanagerapi.model.AddTaskRequestModel;
import com.projectmanagerapi.model.AddUserRequestModel;
import com.projectmanagerapi.model.AllUsersResponseModel;
import com.projectmanagerapi.service.ParentService;
import com.projectmanagerapi.service.ProjectService;
import com.projectmanagerapi.service.TaskService;
import com.projectmanagerapi.service.UserService;

import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@WebMvcTest(ProjectManagerController.class)
public class ProjectManagerControllerTest {
	
	@ClassRule
	public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();
	
	@Rule
	public final SpringMethodRule springMethodRule = new SpringMethodRule(); 
	
	@Autowired
	MockMvc mockMvc;
	MvcResult mvcResult;
	
	@Autowired
	ProjectManagerController projectManagerController;
	
	@MockBean
	UserService userService;
	
	@MockBean
	ProjectService projectService;
	
	@MockBean
	TaskService taskService;
	
	@MockBean
	ParentService parentService;
	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.projectManagerController).build();
	}
	
	@Test
	public void allUsersTest() throws Exception {
		List<AllUsersResponseModel> allUsers = new ArrayList<>();
		Path path = Paths.get(getClass().getClassLoader().getResource("allUsers.csv").toURI());
		Stream<String> lines = Files.lines(path);
		lines.forEach(user -> {
			String[] userData = user.split("\\,");
			AllUsersResponseModel eachUser = new AllUsersResponseModel(Integer.parseInt(userData[2]),
					userData[0], userData[1]);
			allUsers.add(eachUser);
		});
		lines.close();
		Mockito.when(userService.getAllUsers()).thenReturn(allUsers);
		this.mockMvc.perform(get("/allUsers").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.*", hasSize(allUsers.size())));
	}
	
	@Test
	@FileParameters("src/test/resources/addUser.csv")
	public void addUserTest(String fname, String lname, Integer employeeId, Integer status,
			String expected) throws Exception {
			AddUserRequestModel user = new AddUserRequestModel(fname, lname, employeeId);

			mvcResult = this.mockMvc.perform(post("/addUser")
							.content(asJsonString(user))
							.contentType(MediaType.APPLICATION_JSON)	
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().is(status))
					.andReturn();

			String response = mvcResult.getResponse().getContentAsString();
			Assert.assertEquals("", expected, response);

	}
	
	@Test
	@FileParameters("src/test/resources/duplicateUser.csv")
	public void duplicateAddUserTest(String fname, String lname, Integer employeeId, Integer status,
			String expected) throws Exception {
		
		Mockito.when(userService.dupCheck(employeeId)).thenReturn(1);
		AddUserRequestModel user = new AddUserRequestModel(fname, lname, employeeId);

			mvcResult = this.mockMvc.perform(post("/addUser")
							.content(asJsonString(user))
							.contentType(MediaType.APPLICATION_JSON)	
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().is(status))
					.andReturn();

			String response = mvcResult.getResponse().getContentAsString();
			Assert.assertEquals("", expected, response);

	}
	
	@Test
	@FileParameters("src/test/resources/editUser.csv")
	public void editUserTest(String fname, String lname, Integer employeeId, Integer status,
			String expected) throws Exception {
		
		UserEntity[] users = new UserEntity[1];
		users[0] = new UserEntity();		
		users[0].setFirstName(fname);
		users[0].setLastName(lname);
		users[0].setEmployeeId(employeeId);
		users[0].setUserId(0);
		
		Mockito.when(userService.dupCheck(1)).thenReturn(1);
		Mockito.when(userService.dupCheck(3)).thenReturn(0);
		AddUserRequestModel user = new AddUserRequestModel(fname, lname, employeeId);
		Mockito.when(userService.getUserByEmployeeId(employeeId)).thenReturn(users);
		Mockito.when(userService.getByUserId(ArgumentMatchers.anyInt())).thenReturn(new UserEntity());
			mvcResult = this.mockMvc.perform(post("/editUser")
							.content(asJsonString(user))
							.contentType(MediaType.APPLICATION_JSON)	
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().is(status))
					.andReturn();

			String response = mvcResult.getResponse().getContentAsString();
			Assert.assertEquals("", expected, response);

	}
	
	@Test
	@FileParameters("src/test/resources/deleteUser.csv")
	public void deleteUserTest(String fname, String lname, Integer employeeId, Integer status,
			String expected) throws Exception {
			AddUserRequestModel user = new AddUserRequestModel(fname, lname, employeeId);

			Mockito.when(userService.dupCheck(1)).thenReturn(0);
			Mockito.when(userService.dupCheck(2)).thenReturn(1);
			mvcResult = this.mockMvc.perform(post("/deleteUser")
							.content(asJsonString(user))
							.contentType(MediaType.APPLICATION_JSON)	
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().is(status))
					.andReturn();

			String response = mvcResult.getResponse().getContentAsString();
			Assert.assertEquals("", expected, response);

	}
	
	@Test
	@FileParameters("src/test/resources/allProjectsDetails.csv")
	public void getAllProjectsDetailsTest(String project, String startDate,
			String endDate, Integer priority, Integer status) throws Exception {
		
		List<ProjectEntity> projects = new ArrayList<ProjectEntity>();
		ProjectEntity project1 = new ProjectEntity();		
		project1.setProject(project);
		project1.setStartDate(dateFormatter(startDate));
		project1.setEndDate(dateFormatter(endDate));
		project1.setPriority(priority);
		projects.add(project1);
		
		Mockito.when(projectService.getAllProjects()).thenReturn(projects);
		this.mockMvc.perform(get("/allProjectsDetails").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(status));
	}
	
	@Test
	@FileParameters("src/test/resources/allProjects.csv")
	public void getAllProjectsTest(String project, String startDate,
			String endDate, Integer priority, Integer status) throws Exception {
		
		List<ProjectEntity> projects = new ArrayList<ProjectEntity>();
		ProjectEntity project1 = new ProjectEntity();		
		project1.setProject(project);
		project1.setStartDate(dateFormatter(startDate));
		project1.setEndDate(dateFormatter(endDate));
		project1.setPriority(priority);
		projects.add(project1);
		
		Mockito.when(projectService.getAllProjects()).thenReturn(projects);
		this.mockMvc.perform(get("/allProjects").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(status));
	}
	
	@Test
	@FileParameters("src/test/resources/allTasksDetails.csv")
	public void getAllTasksDetailsTest(Integer taskId, Integer projectId, Integer parentId,
			String task, String startDate, String endDate, Integer priority, String status) throws Exception {
		
		List<TaskEntity> tasks = new ArrayList<TaskEntity>();
		TaskEntity task1 = new TaskEntity();	
		task1.setProjectId(projectId);
		task1.setParentId(parentId);
		task1.setTask(task);
		task1.setStartDate(dateFormatter(startDate));
		task1.setEndDate(dateFormatter(endDate));
		task1.setStatus(status);
		task1.setPriority(priority);
		tasks.add(task1);
		
		ProjectEntity project = new ProjectEntity();
		project.setProjectId(1);
		project.setProject("project");
		
		Mockito.when(projectService.getProject(ArgumentMatchers.anyString())).thenReturn(project);
		Mockito.when(taskService.getTasksForParent(ArgumentMatchers.anyInt())).thenReturn(tasks);
		Mockito.when(userService.getOwner(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn(null);
		Mockito.when(parentService.getParent(ArgumentMatchers.anyInt())).thenReturn(null);
		Mockito.when(projectService.getProjectforId(ArgumentMatchers.anyInt())).thenReturn(project);
		this.mockMvc.perform(get("/allTasksDetails?project=project").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
	}
	
	@Test
	@FileParameters("src/test/resources/addProject.csv")
	public void addProjectTest(String project, String startDate,
			String endDate, Integer priority, String fname, String lname, Integer status
			, String expected) throws Exception {
		String manager = lname + ", " + fname;
		AddProjectRequestModel newProject = new AddProjectRequestModel("", project, 
				dateFormatter(startDate), dateFormatter(endDate), priority, manager);
		Mockito.when(projectService.dupCheck("dupProject")).thenReturn(1);
		Mockito.when(userService.userCheck("fname", "lname")).thenReturn(null);
		Mockito.when(userService.userCheck("fname1", "lname1")).thenReturn(1);
			mvcResult = this.mockMvc.perform(post("/addProject")
							.content(asJsonString(newProject))
							.contentType(MediaType.APPLICATION_JSON)	
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().is(status))
					.andReturn();

			String response = mvcResult.getResponse().getContentAsString();
			Assert.assertEquals("", expected, response);

	}
	
	@Test
	@FileParameters("src/test/resources/editProject.csv")
	public void editProjectTest(String oldProject, String project, String startDate,
			String endDate, Integer priority, String fname, String lname, Integer projectId, 
			Integer status, String expected) throws Exception {
		String manager = "";
		if (lname.trim().length() > 0 && fname.trim().length() > 0) {
			manager = lname + ", " + fname;
		}
		ProjectEntity projectEntity = new ProjectEntity();
		projectEntity.setProjectId(projectId);
		AddProjectRequestModel editProject = new AddProjectRequestModel(oldProject, project, 
				dateFormatter(startDate), dateFormatter(endDate), priority, manager);
		Mockito.when(projectService.dupCheck("oldProject3")).thenReturn(0);
		Mockito.when(projectService.dupCheck("oldProject4")).thenReturn(1);
		Mockito.when(userService.userCheck("fname4", "lname4")).thenReturn(null);
		Mockito.when(projectService.getProject(oldProject)).thenReturn(new ProjectEntity());
		Mockito.when(projectService.dupCheck("oldProject5")).thenReturn(1);
		Mockito.when(userService.getManager(5)).thenReturn(new UserEntity());
		Mockito.when(projectService.dupCheck("oldProject6")).thenReturn(1);
		Mockito.when(userService.getManager(6)).thenReturn(null);
		Mockito.when(projectService.dupCheck("oldProject7")).thenReturn(1);
		Mockito.when(userService.getManager(7)).thenReturn(new UserEntity());
		Mockito.when(projectService.dupCheck("oldProject8")).thenReturn(0);
			mvcResult = this.mockMvc.perform(post("/editProject")
							.content(asJsonString(editProject))
							.contentType(MediaType.APPLICATION_JSON)	
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().is(status))
					.andReturn();

			String response = mvcResult.getResponse().getContentAsString();
			Assert.assertEquals("", expected, response);

	}
	
	@Test
	@FileParameters("src/test/resources/addTask.csv")
	public void addTaskTest(String project, String task, Integer priority, String parentTask,
			String startDate, String endDate, String fname, String lname,
			Integer status, String expected) throws Exception {
		String user = "";
		if (fname.trim().length() > 0) {
			user = lname + ", " + fname;
		}
		AddTaskRequestModel newProject = new AddTaskRequestModel(project, "", task, priority,
				parentTask,	dateFormatter(startDate), dateFormatter(endDate), user, "");
		Mockito.when(taskService.dupCheck("task3")).thenReturn(1);
		Mockito.when(parentService.dupCheck("parent4")).thenReturn(0);
		Mockito.when(parentService.dupCheck("parent5")).thenReturn(1);
		Mockito.when(projectService.dupCheck("project5")).thenReturn(0);
		Mockito.when(parentService.dupCheck("parent6")).thenReturn(1);
		Mockito.when(projectService.dupCheck("project6")).thenReturn(1);
		Mockito.when(userService.userCheck("fname6", "lname6")).thenReturn(null);
		Mockito.when(projectService.getProject("project7")).thenReturn(new ProjectEntity());
		Mockito.when(projectService.dupCheck("project7")).thenReturn(1);
		Mockito.when(parentService.dupCheck("parent7")).thenReturn(1);
		Mockito.when(userService.userCheck("fname7", "lname7")).thenReturn(1);
		Mockito.when(projectService.getProject(project)).thenReturn(new ProjectEntity());
			mvcResult = this.mockMvc.perform(post("/addTask")
							.content(asJsonString(newProject))
							.contentType(MediaType.APPLICATION_JSON)	
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().is(status))
					.andReturn();

			String response = mvcResult.getResponse().getContentAsString();
			Assert.assertEquals("", expected, response);
	}
	
	@Test
	@FileParameters("src/test/resources/editTask.csv")
	public void editTaskTest(String project, String oldTask, String task, Integer priority, String parentTask,
			String startDate, String endDate, String fname, String lname,
			Integer status, String expected) throws Exception {
		String user = "";
		if (fname.trim().length() > 0) {
			user = lname + ", " + fname;
		}
		TaskEntity taskEntity = new TaskEntity();
		taskEntity.setTask(task);
		taskEntity.setParentId(0);
		taskEntity.setProjectId(0);
		taskEntity.setStartDate(dateFormatter(startDate));
		taskEntity.setEndDate(dateFormatter(endDate));
		taskEntity.setPriority(priority);
		AddTaskRequestModel newProject = new AddTaskRequestModel(project, oldTask, task, priority,
				parentTask,	dateFormatter(startDate), dateFormatter(endDate), user, "");
		Mockito.when(projectService.dupCheck(project)).thenReturn(1);
		Mockito.when(parentService.dupCheck(parentTask)).thenReturn(1);
		Mockito.when(userService.userCheck(fname, lname)).thenReturn(1);
		Mockito.when(taskService.dupCheck(oldTask)).thenReturn(1);
		Mockito.when(projectService.getProject(project)).thenReturn(new ProjectEntity());
		Mockito.when(taskService.getTask(ArgumentMatchers.anyString())).thenReturn(new TaskEntity());
		Mockito.when(taskService.dupCheck("oldTask3")).thenReturn(0);
		Mockito.when(parentService.dupCheck("parent4")).thenReturn(0);
		Mockito.when(parentService.dupCheck("parent5")).thenReturn(1);
		Mockito.when(projectService.dupCheck("project5")).thenReturn(0);
		Mockito.when(parentService.dupCheck("parent6")).thenReturn(1);
		Mockito.when(projectService.dupCheck("project6")).thenReturn(1);
		Mockito.when(userService.userCheck("fname6", "lname6")).thenReturn(null);
		Mockito.when(projectService.dupCheck("project7")).thenReturn(1);
		Mockito.when(parentService.dupCheck("parent7")).thenReturn(1);
		Mockito.when(userService.userCheck("fname7", "lname7")).thenReturn(1);
		Mockito.when(userService.getOwner(ArgumentMatchers.anyInt(),ArgumentMatchers.anyInt())).thenReturn(new UserEntity());
		
			mvcResult = this.mockMvc.perform(post("/editTask")
							.content(asJsonString(newProject))
							.contentType(MediaType.APPLICATION_JSON)	
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().is(status))
					.andReturn();

			String response = mvcResult.getResponse().getContentAsString();
			Assert.assertEquals("", expected, response);
	}
	
	@Test
	@FileParameters("src/test/resources/addParent.csv")
	public void addParentTest(String parent, Integer status, String expected) throws Exception {
		ParentEntity newParent = new ParentEntity();
		newParent.setParent(parent);
		Mockito.when(parentService.dupCheck("parent1")).thenReturn(1);
		mvcResult = this.mockMvc.perform(post("/addParent")
						.content(asJsonString(newParent))
						.contentType(MediaType.APPLICATION_JSON)	
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is(status))
				.andReturn();

		String response = mvcResult.getResponse().getContentAsString();
		Assert.assertEquals("", expected, response);
	}
	
	public static String asJsonString(final Object obj) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(obj);
	}
	
	public Date dateFormatter (String sDate) throws ParseException {
		DateFormat format = new SimpleDateFormat("Y-L-d");
		return format.parse(sDate);
	}

}
