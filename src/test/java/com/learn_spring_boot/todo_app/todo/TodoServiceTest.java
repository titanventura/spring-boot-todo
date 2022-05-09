package com.learn_spring_boot.todo_app.todo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Test
    void shouldReturnAllTodosWhenGetAllTodosIsCalled() {
        // arrange

        // act
        todoService.allTodos();

        // assert
        verify(todoRepository).findAll();
    }


    @Test
    void shouldReturnTodoWithId_1_WhenQueriedForTodoWithId_1() {
        // arrange
        Todo expected = new Todo(1L, "Do the dished", "Dishes");
        when(todoRepository.findById(anyLong()))
                .thenReturn(Optional.of(expected));

        // act
        Optional<Todo> actual = todoService.todoWithId(1L);

        // assert
        verify(todoRepository).findById(anyLong());
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }
}
