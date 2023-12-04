package com.javarush.controller;

import com.javarush.domain.Status;
import com.javarush.domain.Task;
import com.javarush.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;
import java.util.stream.IntStream;

@Slf4j
@Controller
@RequestMapping
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @RequestMapping("/")
    public String tasks(Model model,
                            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit
                            ){
        log.error("Accessing tasks endpoint. Page: {}, Limit: {}", page, limit);

        var all = taskService.getAll((page - 1) * limit, limit);
        model.addAttribute("tasks", all);
        model.addAttribute("current_page", page);
        int totalPages = (int) Math.ceil(1.0 * taskService.getAllCount() / limit);
        if(totalPages>1){
            var pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().toList();
            model.addAttribute("page_numbers", pageNumbers);
        }
        return "index";
    }

    @GetMapping("/{id}/edit")
    public String editTaskForm(@PathVariable int id, Model model) {
        log.info("Accessing editTaskForm endpoint. Task ID: {}", id);
        model.addAttribute("editTask", taskService.getTaskById(id));

        return "edit";
    }

    @PutMapping
    public String updateTask(@ModelAttribute("editTask") Task task) {
        log.info("Updating task. Task ID: {}", task.getId());
        taskService.updateTask(task);

        return "redirect:/";
    }


    @PostMapping("/new")
    public String addTask(@RequestParam("description") String description,
                          @RequestParam("status") Status status,
                          Model model) {
        log.info("Adding a new task. Description: {}, Status: {}", description, status);

        if (description == null || description.isEmpty() || status == null ) {
            throw new RuntimeException("Invalid task data");
        }

        Task newTask = taskService.create(description, status);
        model.addAttribute("newTask", newTask);

        int all = taskService.getAllCount();
        int result;
        if (all%10 ==0){
            result = all/10;
        } else result =all/10+1;
        return tasks(model, result, 10);
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable Integer id){
        log.info("Deleting task. Task ID: {}", id);
        if (Objects.isNull(id) || id <= 0){
            throw new RuntimeException("Invalid id");
        }
        taskService.delete(id);

        return "redirect:/";
    }


}
