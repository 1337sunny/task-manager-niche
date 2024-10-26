package com.sunny.task_manager.controller;

import com.sunny.task_manager.entity.Task;
import com.sunny.task_manager.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskRepository taskRepository;

    @GetMapping
    public String viewTasks(@RequestParam(value = "filter", required = false) String filter, Model model) {
        List<Task> tasks;

        if ("completed".equals(filter)) {
            tasks = taskRepository.findByCompleted(true);
        } else if ("pending".equals(filter)) {
            tasks = taskRepository.findByCompleted(false);
        } else {
            tasks = taskRepository.findAll();
        }

        model.addAttribute("tasks", tasks);
        return "task-list";
    }


    @GetMapping("/add")
    public String showAddTaskForm(Model model) {
        model.addAttribute("newTask", new Task());
        return "add-task";
    }

    @PostMapping("/add")
    public String addTask(@ModelAttribute("newTask") Task task) {
        task.setCompleted(false); // Default to not completed
        taskRepository.save(task);
        return "redirect:/tasks";
    }


    @GetMapping("/complete/{id}")
    public String markAsComplete(@PathVariable("id") Integer id) {
        Task task = taskRepository.findById(id).orElseThrow();
        task.setCompleted(!task.isCompleted());
        taskRepository.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{id}")
    public String showEditTaskForm(@PathVariable("id") Integer id, Model model) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id));
        model.addAttribute("task", task);
        return "edit-task";
    }

    @PostMapping("/edit/{id}")
    public String updateTask(@PathVariable("id") Integer id,
                             @RequestParam String task,
                             @RequestParam(required = false, defaultValue = "false") boolean completed,
                             Model model) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task existingTask = taskOptional.get();
            existingTask.setTask(task);
            existingTask.setCompleted(completed);
            taskRepository.save(existingTask);
            taskRepository.findAll();
        }
        return "redirect:/tasks";
    }

    // Delete a task by ID
    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable("id") Integer id) {
        taskRepository.deleteById(id);
        return "redirect:/tasks";
    }
}
