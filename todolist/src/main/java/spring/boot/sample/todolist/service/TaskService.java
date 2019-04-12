package spring.boot.sample.todolist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import spring.boot.sample.todolist.entity.TaskEntity;
import spring.boot.sample.todolist.repository.TaskRepository;

@Service
public class TaskService {
	@Autowired
	private TaskRepository repo;
	
	@Transactional
    public List<TaskEntity> listTasks() {
        return repo.findAll();
    }
	
	@Transactional
    public void removeTask(int id) {
        repo.deleteById(id);
    }
	
	@Transactional
    public TaskEntity getTaskById(int id) {
        return repo.findById(id).orElse(null);
    }
	
	@Transactional
    public int addTask(TaskEntity task) {
        repo.save(task);
        
        return task.getId();
    }
	
	@Transactional
    public void updateTask(TaskEntity task) {
        repo.save(task);
    }
}
