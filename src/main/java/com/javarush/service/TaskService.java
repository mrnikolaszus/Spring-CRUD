package com.javarush.service;

import com.javarush.dao.TaskDAO;
import com.javarush.domain.Status;
import com.javarush.domain.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class TaskService {
    private final TaskDAO taskDAO;

    public TaskService(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public List<Task> getAll(int offset, int limit){
        return taskDAO.allTasks(offset, limit);

    }

    public int getAllCount(){
        return taskDAO.allCount();
    }

    @Transactional
    public Task edit(int id, String description, Status status){
        var task = taskDAO.findById(id);
        if(Objects.isNull(task)){
            throw new RuntimeException("Not found");
        }

        task.setDescription(description);
        task.setStatus(status);
        taskDAO.saveOrUpdate(task);
        return task;

    }

    public Task create(String description, Status status){
        Task task = new Task();
        task.setDescription(description);
        task.setStatus(status);
        taskDAO.saveOrUpdate(task);
        return task;

    }

    @Transactional
    public void delete(int id){
        var task = taskDAO.findById(id);
        if(Objects.isNull(task)){
            throw new RuntimeException("Not found");
        }
        taskDAO.delete(task);

    }


}
