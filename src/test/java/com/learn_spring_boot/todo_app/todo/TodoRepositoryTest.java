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
                "spring.flyway.enabled=false",
                "spring.jpa.hibernate.ddl-auto=create-drop"
        }
)
public class TodoRepositoryTest {

    @Autowired
    TodoRepository todoRepository;


    @Test
    void shouldPersistTodoWhenSavedThroughRepository() {
        // arrange
        Todo todo = new Todo("Do the dishes !", null);

        // act
        Assertions.assertNull(todo.getId());
        todoRepository.save(todo);

        // assert
        Assertions.assertNotNull(todo.getId());
        assertTrue(todoRepository.existsById(todo.getId()));
    }

    @Test
    void shouldGetAllTodosWhenMultipleTodosAreSaved() {
        // arrange
        List<Todo> todos = List.of(
                new Todo("Do something!", null),
                new Todo("Do some other thing !", null)
        );

        // act
        todoRepository.saveAll(todos);

        // assert
        List<Todo> all = todoRepository.findAll();
        assertEquals(todos, all);
    }

    @Test
    void todoShouldNotExistWhenRemovedThroughRepository() {
        // arrange
        Todo todo = new Todo("Do the homework !", null);
        todoRepository.save(todo);

        // act
        todoRepository.deleteById(todo.getId());
        System.out.println(todo);

        // assert
        assertFalse(todoRepository.existsById(todo.getId()));
    }

    @Test
    void shouldUpdateTheTodoWhenSaveIsCalledAgainOnTheSameTodoInTheRepository() {
        // arrange
        String originalDescription = "Don't have any veggies for the week !";
        Todo todo = new Todo("Buy some veggies !", originalDescription);
        todoRepository.save(todo);
        String newDescription = "Haven't got any veggies for the week !";

        // act
        todo.setDescription(newDescription);
        todoRepository.save(todo);

        // assert
        Optional<Todo> todoById = todoRepository.findById(todo.getId());
        assertTrue(todoById.isPresent());
        Todo changed = todoById.get();
        assertNotEquals(originalDescription, changed.getDescription());
        assertEquals(newDescription, changed.getDescription());
    }
}
