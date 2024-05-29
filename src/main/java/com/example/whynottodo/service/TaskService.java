package com.example.whynottodo.service;
import com.example.whynottodo.DTO.TaskRequestDTO;
import com.example.whynottodo.model.User;
import com.example.whynottodo.repository.TaskRepository;
import com.example.whynottodo.model.Task;
import com.example.whynottodo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public TaskRequestDTO createTask(TaskRequestDTO task) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        System.out.println("\n Attempting to find user with username: " + username +"\n");

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            optionalUser = userRepository.findByEmail(username);
        }
        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException(
                "User not found with username or email: " + username)
        );

        Task taskToSave = mapper.map(task, Task.class);
        taskToSave.setUser(user);

        Task savedTask = taskRepository.save(taskToSave);
        return mapper.map(savedTask, TaskRequestDTO.class);
    }

    public List<TaskRequestDTO> getAllTasks() {
        List<Task> taskList =  taskRepository.findAll();
        if (taskList.isEmpty()){
            throw new NoSuchElementException("There is no task.");
        }
        return taskList.stream().map(task -> mapper.map(task, TaskRequestDTO.class)).toList();
    }

    public List<TaskRequestDTO> findByUserId(Long userId) {
        List<Task> tasks = taskRepository.findByUserId(userId);
        if (tasks.isEmpty()) {
            throw new NoSuchElementException("No tasks found for the user with ID: " + userId);
        }
        //tasks.forEach(task -> System.out.println("Task: " + task.getDescription()));
        return tasks.stream().map(task -> {
            //System.out.println("Mapped DTO: " + dto.getDescription());
            return mapper.map(task, TaskRequestDTO.class);
        }).collect(Collectors.toList());
    }
    public TaskRequestDTO getTaskById(Long id) {
        Optional<Task> task =  taskRepository.findById(id);
        if (task.isEmpty()){
            throw new ResourceNotFoundException("task","id",id);
        }
        return mapper.map(task, TaskRequestDTO.class);
    }



    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
            super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        }
    }

    public TaskRequestDTO updateTask(Long id, Task taskDetails) {
        Optional<Task> optionalTask = taskRepository.findById(id);

        if(optionalTask.isEmpty()){
            throw new ResourceNotFoundException("Task","id",id);
        }

        Task task = optionalTask.get();
        task.setDescription(taskDetails.getDescription());
        task.setComplete(taskDetails.isComplete());

        Task savedTask = taskRepository.save(task);

        return mapper.map(savedTask, TaskRequestDTO.class);
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        taskRepository.delete(task);
    }

    public List<TaskRequestDTO> searchTasks(String keyword) {
        List<Task> tasks = taskRepository.search(keyword);
        return tasks.stream().map(task -> mapper.map(task, TaskRequestDTO.class)).toList();
    }

    public List<TaskRequestDTO> filterTasksByComplete(boolean complete) {
        List<Task> tasks = taskRepository.findByComplete(complete);
        return tasks.stream().map(task -> mapper.map(tasks, TaskRequestDTO.class)).toList();
    }

    public List<TaskRequestDTO> sortTasks(String sortBy) {
        List<Task> tasks = taskRepository.findAll(Sort.by(sortBy));
        return tasks.stream().map(task -> mapper.map(task, TaskRequestDTO.class)).collect(Collectors.toList());
    }


    /*
    public List<Task> getAllTasksWithUsers() {
        return taskRepository.findAllWithUsers();
    }


    public TaskDTO addDependency(Long taskId, Long dependentTaskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));
        Task dependentTask = taskRepository.findById(dependentTaskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", dependentTaskId));

        task.getDependentTasks().add(dependentTask);
        Task savedTask = taskRepository.save(task);

        return mapper.map(savedTask, TaskDTO.class);
    }

    public boolean checkDependencies(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        return task.getDependentTasks().stream().allMatch(Task::isComplete);
    }
    */
}
