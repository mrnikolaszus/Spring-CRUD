package com.javarush.controller;

import com.javarush.controller.TaskController;
import com.javarush.domain.Status;
import com.javarush.domain.Task;
import com.javarush.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Transactional
@Rollback
class TaskControllerTest {
    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void tasks() {
        Model model = Mockito.mock(Model.class);
        when(taskService.getAll(anyInt(), anyInt())).thenReturn(Collections.emptyList());
        String result = taskController.tasks(model, 1, 10);
        assertEquals("index", result);
        verify(taskService).getAll(0, 10);
        verify(model).addAttribute(eq("tasks"), any());
        verify(model).addAttribute(eq("current_page"), eq(1));

    }

    @Test
    void editTaskForm() {
        Model model = Mockito.mock(Model.class);
        when(taskService.getTaskById(1)).thenReturn(new Task());
        String result = taskController.editTaskForm(1, model);
        assertEquals("edit", result);
        verify(taskService).getTaskById(1);
        verify(model).addAttribute(eq("editTask"), any(Task.class));
        verifyNoMoreInteractions(model, taskService);
    }

    @Test
    void updateTask() {
        Task task = new Task();
        task.setId(1);
        String result = taskController.updateTask(task);
        assertEquals("redirect:/", result);
        verify(taskService).updateTask(task);
        verifyNoMoreInteractions(taskService);
    }

    @Test
    void addTask() {
        Model model = Mockito.mock(Model.class);
        when(taskService.create(anyString(), any(Status.class))).thenReturn(new Task());
        String result = taskController.addTask("Description", Status.DONE, model);
        assertEquals("index", result);
        verify(taskService).create("Description", Status.DONE);
        verify(model).addAttribute(eq("newTask"), any(Task.class));

    }

    @Test
    void delete() {
        String result = taskController.delete(1);
        assertEquals("redirect:/", result);
        verify(taskService).delete(1);
        verifyNoMoreInteractions(taskService);
    }
}
