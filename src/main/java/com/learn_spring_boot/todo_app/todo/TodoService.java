package com.learn_spring_boot.todo_app.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> allTodos() {
        List<Todo> all = todoRepository.findAll();
        System.out.println(all);
        return all;
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

    public Todo update(long id, Todo updated) {
        Todo todoById = todoRepository.getById(id);
        todoById.setText(updated.getText());
        todoById.setDescription(updated.getDescription());
        todoRepository.save(todoById);
        return todoById;
    }
}
