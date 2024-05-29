package com.example.whynottodo.controller;

import com.example.whynottodo.DTO.TaskRequestDTO;
import com.example.whynottodo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/tasks")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminTaskController {
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskRequestDTO>> getAllTasks() {
        return new ResponseEntity<>(taskService.getAllTasks(), HttpStatus.OK);
    }




}
