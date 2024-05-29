package com.example.whynottodo.controller;

import com.example.whynottodo.service.ModService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/moderator")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MODERATOR')")
public class ModController {
    private final ModService modService;
/*
    @PostMapping("/approve-task/{taskId}")
    public ResponseEntity<?> approveTask(@PathVariable Long taskId) {
        modService.approveTask(taskId);
        return ResponseEntity.ok().build();
    }
*/
    @DeleteMapping("/reject-task/{taskId}")
    public ResponseEntity<?> rejectTask(@PathVariable Long taskId) {
        modService.rejectTask(taskId);
        return ResponseEntity.ok().build();
    }
}
