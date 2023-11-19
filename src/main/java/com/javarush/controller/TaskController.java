package com.javarush.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.javarush.domain.Task;
import com.javarush.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String tasks(Model model,
                            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit
                            ){

        var all = taskService.getAll((page - 1) * limit, limit);
        model.addAttribute("tasks", all);
        model.addAttribute("current_page", page);
        int totalPages = (int) Math.ceil(1.0 * taskService.getAllCount() / limit);
        if(totalPages>1){
            var pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().toList();
            model.addAttribute("page_numbers", pageNumbers);
        }
        return "tasks";
    }

    @PostMapping("/{id}")
    public String edit(Model model,
                     @PathVariable Integer id,
                     @RequestBody TaskInfo info){
        if (Objects.isNull(id) || id <= 0){
            throw new RuntimeException("Invalid id");
        }
        var task = taskService.edit(id, info.getDescription(), info.getStatus());

        return tasks(model, 1, 10);
    }

    @PostMapping("/")
    public void add(Model model,
                     @RequestBody TaskInfo info){
        var task = taskService.create(info.getDescription(), info.getStatus());
    }

    @DeleteMapping("/{id}")
    public String delete(Model model,
                         @PathVariable Integer id){
        if (Objects.isNull(id) || id <= 0){
            throw new RuntimeException("Invalid id");
        }
        taskService.delete(id);
        return tasks(model, 1, 10);

    }
}
