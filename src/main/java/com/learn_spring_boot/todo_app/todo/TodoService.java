package com.learn_spring_boot.todo_app.todo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> allTodos() {
        return todoRepository.findAll();
    }

    public Optional<Todo> todoWithId(long id) {
        return todoRepository.findById(id);
    }

    public Todo add(Todo newTodo) {
        return todoRepository.save(newTodo);
    }

    public void remove(Long id) {
        todoRepository.deleteById(id);
    }
}
