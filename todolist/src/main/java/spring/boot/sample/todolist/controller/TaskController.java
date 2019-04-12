package spring.boot.sample.todolist.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import spring.boot.sample.todolist.entity.TaskEntity;
import spring.boot.sample.todolist.service.TaskService;

@RestController
public class TaskController {
	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
	
	@Autowired
    private TaskService taskService;
	
	@RequestMapping("/")
	public ModelAndView index(Map<String, Object> model) {
		return new ModelAndView("index");
	}
	
	/**
	 * get list of task
	 * 
	 * @return JSON Task Object
	 */
	@RequestMapping(value = "tasks", method = RequestMethod.GET)
    public ResponseEntity<List<TaskEntity>> listTasks() {
        logger.info("List all tasks");
        List<TaskEntity> tasks = taskService.listTasks();
        if (tasks.isEmpty()) {
            return new ResponseEntity<List<TaskEntity>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<TaskEntity>>(tasks, HttpStatus.OK);
    }
	
	/**
     * GET TASK BY ID
     *
     * @param id indentification of task
     * @return Return JSON Object task by id
     */
    @RequestMapping(value = "/task/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskEntity> getTask(@PathVariable("id") int id) {
        logger.info("Get Task by id: " + id);
        TaskEntity task = taskService.getTaskById(id);
        if (task == null) {
            return new ResponseEntity<TaskEntity>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<TaskEntity>(task, HttpStatus.OK);
    }
	
	/**
	 * delete task by id
	 * 
	 * @param id task indentification
     * @return Http status
	 */
	@RequestMapping(value = "/task/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<TaskEntity> deleteTask(@PathVariable("id") int id) {
        logger.info("Deleting Task by id: " + id);

        TaskEntity findedTask = taskService.getTaskById(id);

        if (findedTask == null) {
            logger.info("Can't to delete. Task with id " + id + " not found");
            return new ResponseEntity<TaskEntity>(HttpStatus.NOT_FOUND);
        }

        taskService.removeTask(id);
        return new ResponseEntity<TaskEntity>(HttpStatus.NO_CONTENT);
    }
	
	/**
     * add task
     *
     * Example JSON {"description":"This is task","id":0,"date":1493212282000,"hasdone":false}
     *
     * @param task geted task object
     * @return Http status
     */
    @RequestMapping(value = "/task", method = RequestMethod.POST)
    public ResponseEntity<TaskEntity> createTask(@RequestBody TaskEntity task) { //,    UriComponentsBuilder ucBuilder) {
        logger.info("Add new Task to BD");

        if (task.getId() == 0) {
            int id = taskService.addTask(task);

            TaskEntity storedTask = taskService.getTaskById(id);
            if (storedTask == null) {
                return new ResponseEntity<TaskEntity>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<TaskEntity>(task, HttpStatus.OK);

        } else {
            taskService.updateTask(task);
            return new ResponseEntity<TaskEntity>(HttpStatus.CONFLICT);
        }
    }
    
    /**
     * UPDATE TASK BY ID
     *
     * @param id   task indentification
     * @param task geted task object
     * @return Http status
     */
    @RequestMapping(value = "/task/{id}", method = RequestMethod.PUT)
    public ResponseEntity<TaskEntity> updateTask(@PathVariable("id") int id, @RequestBody TaskEntity task) {
        logger.info("Update Task by id: " + id);

        TaskEntity findedTask = taskService.getTaskById(id);

        if (findedTask == null) {
            logger.info("Task with id: " + id + " not found");
            return new ResponseEntity<TaskEntity>(HttpStatus.NOT_FOUND);
        }

        findedTask.setDescription(task.getDescription());
        findedTask.setDate(task.getDate());
        findedTask.setHasDone(task.getHasDone());
        taskService.updateTask(findedTask);

        return new ResponseEntity<TaskEntity>(findedTask, HttpStatus.OK);
    }
}
