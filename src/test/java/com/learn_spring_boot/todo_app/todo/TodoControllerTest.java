package com.learn_spring_boot.todo_app.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TodoService todoService;

    @Test
    void shouldReturnListOfTodosWhenTodosEndpointIsCalled() throws Exception {
        when(todoService.allTodos()).thenReturn(List.of(
                new Todo("Do something!"),
                new Todo("Do some other thing !")
        ));

        mockMvc.perform(MockMvcRequestBuilders.get("/todos"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2));
        verify(todoService, times(1)).allTodos();
    }

    @Test
    void shouldReturnASingleTodoWhenIndividualTodoEndpointIsCalled() throws Exception {
        Todo todo = new Todo(1L, "Something !", null);
        when(todoService.todoWithId(1L)).thenReturn(Optional.of(todo));


        mockMvc.perform(MockMvcRequestBuilders.get("/todos/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(todo.getId()))
                .andExpect(jsonPath("$.text").value(todo.getText()))
                .andExpect(jsonPath("$.description").value(todo.getDescription()));

        verify(todoService, times(1)).todoWithId(1L);
    }

    @Test
    void shouldSaveATodoAndReturnSuccessResponseWhenPostedToTodosEndpoint() throws Exception {
        when(todoService.add(any(Todo.class))).then((Answer<Todo>) invocation -> {
            Todo existing = invocation.getArgument(0);
            System.out.println(existing);
            return new Todo(1L, existing.getText(), existing.getDescription());
        });
        Todo todo = new Todo("My Todo !");

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/todos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(todo))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.text").value(todo.getText()))
                .andExpect(jsonPath("$.description").value(todo.getDescription()));
        verify(todoService, times(1)).add(todo);
    }

    @Test
    void shouldUpdateATodoAndReturnASuccessfulUpdatedTodoResponseWhenPutRequestIsMadeToIndividualTodosEndpoint() throws Exception {
        Todo previous = new Todo(1L, "Previous", null);
        Todo updated = new Todo("Updated", "Test updated !");
        when(todoService.update(previous.getId(), updated))
                .then((Answer<Todo>) invocation -> {
                    Long todoId = invocation.getArgument(0);
                    Todo passed = invocation.getArgument(1);
                    return new Todo(todoId, passed.getText(), passed.getDescription());
                });

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/todos/{id}", previous.getId())
                                .content(new ObjectMapper().writeValueAsString(updated))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(previous.getId()))
                .andExpect(jsonPath("$.text").value(updated.getText()))
                .andExpect(jsonPath("$.description").value(updated.getDescription()));

        verify(todoService, times(1)).update(previous.getId(), updated);
    }

    @Test
    void shouldDeleteTodoWhenDeleteRequestIsMadeToIndividualTodoDeleteEndpoint() throws Exception {
        Todo todo = new Todo(1L, "Todo to be deleted !", null);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/todos/{id}", todo.getId())
                )
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(todoService, times(1)).remove(todo.getId());
    }
}
