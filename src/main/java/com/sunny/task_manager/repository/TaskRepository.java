package com.sunny.task_manager.repository;

import com.sunny.task_manager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByCompleted(boolean completed);
}
