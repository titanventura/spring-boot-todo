package com.learn_spring_boot.todo_app.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public List<Todo> allTodos() {
        return todoService.allTodos();
    }

    @GetMapping(path = "/{id}")
    public Todo getATodo(@PathVariable Long id) {
        return todoService.todoWithId(id).orElseThrow(TodoNotFoundException::new);
    }

    @PostMapping
    public ResponseEntity addATodo(@RequestBody Todo newTodo) {
        return new ResponseEntity(todoService.add(newTodo), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public Todo updateATodo(@PathVariable Long id, @RequestBody Todo updatedTodo) {
        return todoService.update(id, updatedTodo);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity removeATodo(@PathVariable Long id) {
        todoService.remove(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
