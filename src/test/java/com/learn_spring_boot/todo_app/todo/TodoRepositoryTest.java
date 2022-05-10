package com.learn_spring_boot.todo_app.todo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(
        properties = {
                "spring.datasource.url=jdbc:h2:mem:test_db",
                "spring.flyway.enabled=false",
                "spring.jpa.hibernate.ddl-auto=create-drop"
        }
)
public class TodoRepositoryTest {

    @Autowired
    TodoRepository todoRepository;


    @Test
    void shouldPersistTodoWhenSavedThroughRepository() {
        Todo todo = new Todo("Do the dishes !");

        Assertions.assertNull(todo.getId());
        todoRepository.save(todo);

        Assertions.assertNotNull(todo.getId());
        assertTrue(todoRepository.existsById(todo.getId()));
    }

    @Test
    void shouldGetAllTodosWhenMultipleTodosAreSaved() {
        List<Todo> todos = List.of(
                new Todo("Do something!"),
                new Todo("Do some other thing !")
        );

        todoRepository.saveAll(todos);

        List<Todo> all = todoRepository.findAll();
        assertEquals(todos, all);
    }

    @Test
    void todoShouldNotExistWhenRemovedThroughRepository() {
        Todo todo = new Todo("Do the homework !");
        todoRepository.save(todo);

        todoRepository.deleteById(todo.getId());
        System.out.println(todo);

        assertFalse(todoRepository.existsById(todo.getId()));
    }

    @Test
    void shouldUpdateTheTodoWhenSaveIsCalledAgainOnTheSameTodoInTheRepository() {
        String originalDescription = "Don't have any veggies for the week !";
        Todo todo = new Todo("Buy some veggies !", originalDescription);
        todoRepository.save(todo);
        String newDescription = "Haven't got any veggies for the week !";

        todo.setDescription(newDescription);
        todoRepository.save(todo);

        Optional<Todo> todoById = todoRepository.findById(todo.getId());
        assertTrue(todoById.isPresent());
        Todo changed = todoById.get();
        assertNotEquals(originalDescription, changed.getDescription());
        assertEquals(newDescription, changed.getDescription());
    }
}
