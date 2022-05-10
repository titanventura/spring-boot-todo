package com.learn_spring_boot.todo_app.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/todos")
public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping(path = "")
    public List<Todo> allTodos() {
        return todoService.allTodos();
    }

    @GetMapping(path = "/{id}")
    public Todo getATodo(@PathVariable Long id) {
        return todoService.todoWithId(id).orElse(null);
    }

    @PostMapping
    public Todo addATodo(@RequestBody Todo newTodo) {
        return todoService.add(newTodo);
    }
}
