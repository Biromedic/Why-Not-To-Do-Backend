package com.example.whynottodo.repository;

import com.example.whynottodo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE t.description LIKE %:keyword%")
    List<Task> search(@Param("keyword") String keyword);

    /*
    @Query("SELECT t FROM Task t JOIN FETCH t.user")
    List<Task> findAllWithUsers();
    */
    List<Task> findByComplete(boolean completed);

    List<Task> findByUserId(long userId);
}
