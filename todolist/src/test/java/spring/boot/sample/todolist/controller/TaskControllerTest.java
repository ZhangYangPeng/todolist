package spring.boot.sample.todolist.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import spring.boot.sample.todolist.entity.TaskEntity;
import spring.boot.sample.todolist.service.TaskService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = TaskController.class, secure = false)
public class TaskControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private TaskService taskService;
	
	@Test
	public void index() throws Exception {
		mockMvc.perform(get("/"))
			.andExpect(status().isOk())
			.andExpect(view().name("index"));
	}
	
	@Test
	public void tasks_with_ok() throws Exception {
		List<TaskEntity> mockTasks = new ArrayList<TaskEntity>();
		TaskEntity task1 = new TaskEntity();
		task1.setId(100);
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		task1.setDate(sdf.parse("29/01/2019 16:33:00"));
		task1.setDescription("task1");
		task1.setHasDone(false);
		mockTasks.add(task1);
		
		Mockito.when(taskService.listTasks()).thenReturn(mockTasks);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/tasks");
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
		
		String expected = "[{\"description\":\"task1\",\"id\":100,\"date\":\"2019-01-29T16:33:00.000+0000\",\"hasdone\":false}]";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void tasks_with_no_content() throws Exception {
		List<TaskEntity> mockTasks = new ArrayList<TaskEntity>();
		
		Mockito.when(taskService.listTasks()).thenReturn(mockTasks);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/tasks");
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());
	}
	
	//@Test
	//public void tasks_fail() throws Exception {
	//	List<TaskEntity> mockTasks = new ArrayList<TaskEntity>();
	//	
	//	Mockito.when(taskService.listTasks()).thenReturn(mockTasks);
	//	RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/tasks");
	//	MvcResult result = mockMvc.perform(requestBuilder).andReturn();
	//	
	//	assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
	//}
}
