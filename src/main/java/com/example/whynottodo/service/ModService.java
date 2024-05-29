package com.example.whynottodo.service;

import com.example.whynottodo.model.Task;
import com.example.whynottodo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModService {
    private TaskRepository taskRepository;

    /*public void approveTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new UserService.ResourceNotFoundException("task","id",taskId));
        task.setApproved(true);
        taskRepository.save(task);
    }
*/
    public void rejectTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }
}
