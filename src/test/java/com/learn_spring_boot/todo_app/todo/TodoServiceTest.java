package com.learn_spring_boot.todo_app.todo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
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
        List<Todo> expected = List.of(
                new Todo(1L, "Do the dishes", null),
                new Todo(2L, "Do some squats !", null)
        );
        when(todoRepository.findAll()).thenReturn(expected);

        List<Todo> todos = todoService.allTodos();

        verify(todoRepository, times(1)).findAll();
        assertEquals(expected, todos);
    }


    @Test
    void shouldReturnTodoWithId_1_WhenQueriedForTodoWithId_1() {
        Todo expected = new Todo(1L, "Do the dishes", "Dishes");
        when(todoRepository.findById(anyLong()))
                .thenReturn(Optional.of(expected));

        Optional<Todo> actual = todoService.todoWithId(1L);

        verify(todoRepository, times(1)).findById(1L);
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void shouldCreateNewTodoWhenAskedTodoService() {
        Todo newTodo = new Todo(1L, "Get the signatures !", "Get signatures for the petition.");
        when(todoRepository.save(any(Todo.class))).thenReturn(newTodo);

        Todo actual = todoService.add(newTodo);

        verify(todoRepository, times(1)).save(newTodo);
        assertEquals(newTodo, actual);
    }

    @Test
    void shouldUpdateExistingTodoWhenUpdateIsCalledOnService() {
        Todo updated = new Todo("Some Text !");
        when(todoRepository.getById(1L)).thenReturn(new Todo(1L, "Old todo", null));

        todoService.update(1L, updated);

        verify(todoRepository, times(1)).getById(1L);
        updated.setId(1L);
        verify(todoRepository, times(1)).save(updated);
    }

    @Test
    void shouldDeleteTodoWhenDeleteIsCalledOnService() {
        Todo todo = new Todo(1L, "Do Something !", null);

        todoService.remove(todo.getId());

        verify(todoRepository, times(1)).deleteById(todo.getId());
    }
}
