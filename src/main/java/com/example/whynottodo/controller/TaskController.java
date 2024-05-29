package com.example.whynottodo.controller;

import com.example.whynottodo.DTO.TaskRequestDTO;
import com.example.whynottodo.model.User;
import com.example.whynottodo.service.TaskService;
import com.example.whynottodo.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TaskRequestDTO> createTask(@RequestBody TaskRequestDTO task) {
        return new ResponseEntity<>(taskService.createTask(task), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskRequestDTO> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok().body(taskService.getTaskById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskRequestDTO> updateTask(@PathVariable Long id, @RequestBody Task task) {
        return ResponseEntity.ok(taskService.updateTask(id, task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?>  deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/usertasks")
    public ResponseEntity<List<TaskRequestDTO>> getUserTasks(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        System.out.println(user);
        List<TaskRequestDTO> userTasks = taskService.findByUserId(user.getId());
        return ResponseEntity.ok(userTasks);
    }

    @GetMapping("/completed")
    public List<TaskRequestDTO> getCompletedTasks() {
        return taskService.filterTasksByComplete(true);
    }

    @GetMapping("/inprogress")
    public List<TaskRequestDTO> getNotCompletedTasks() {
        return taskService.filterTasksByComplete(false);
    }

    /*
    @PostMapping("/{taskId}/dependencies/{dependentTaskId}")
    public TaskDTO addDependency(@PathVariable(value = "taskId") Long taskId,
                              @PathVariable(value = "dependentTaskId") Long dependentTaskId) {
        return taskService.addDependency(taskId, dependentTaskId);
    }

    @GetMapping("/{taskId}/dependencies")
    public boolean checkDependencies(@PathVariable(value = "taskId") Long taskId) {
        return taskService.checkDependencies(taskId);
    }
    */
    @GetMapping("/search")
    public List<TaskRequestDTO> searchTasks(@RequestParam(value = "keyword") String keyword) {
        return taskService.searchTasks(keyword);
    }

    @GetMapping("/sort")
    public List<TaskRequestDTO> sortTasks(@RequestParam(value = "sortBy") String sortBy) {
        return taskService.sortTasks(sortBy);
    }


}

