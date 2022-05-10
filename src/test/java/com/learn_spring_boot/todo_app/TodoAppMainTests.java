package com.learn_spring_boot.todo_app;

import com.learn_spring_boot.todo_app.todo.Todo;
import com.learn_spring_boot.todo_app.todo.TodoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(
        properties = {
                "spring.datasource.url=jdbc:h2:mem:test_db",
                "spring.flyway.enabled=false",
                "spring.jpa.hibernate.ddl-auto=create-drop"
        }
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class TodoAppMainTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int randomPort;

    private String TODOS_RESOURCE_URL;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @BeforeEach
    void setUp() {
        todoRepository.saveAll(
                List.of(
                        new Todo(1L, "Explore Meteors !", null),
                        new Todo(2L, "Explore Pulsars !", null),
                        new Todo(3L, "Explore Quasars !", null)
                )
        );
        TODOS_RESOURCE_URL = "http://localhost:" + randomPort + "/todos";
    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "todos");
    }

    @Test
    void contextLoads() {
        assertNotNull(testRestTemplate);
        assertNotNull(todoRepository);
    }


    @Test
    void shouldReturn3TodosWhenTodosEndpointIsCalled() {
        ResponseEntity<Todo[]> responseEntity =
                testRestTemplate.getForEntity(TODOS_RESOURCE_URL, Todo[].class);


        Todo[] todos = responseEntity.getBody();

        assertThat(todos).isNotNull();
        assertThat(todos.length).isEqualTo(3);
    }

    @Test
    void shouldReturnTodoWithId1AndItsDetailsWhenGetRequestIsPlacedToIndividualTodoEndpointWith1AsPathVariable() {
        ResponseEntity<Todo> responseEntity = testRestTemplate.getForEntity(TODOS_RESOURCE_URL + "/" + 1, Todo.class);

        Todo todo = responseEntity.getBody();

        assertThat(todo).isNotNull();
        assertThat(todo).isEqualTo(new Todo(1L, "Explore Meteors !", null));
    }

    @Test
    void shouldCreateANewTodoWhenOneIsPostedToTodosEndpoint() {
        Todo request = new Todo("Explore anderomeda !");
        ResponseEntity<Todo> responseEntity = testRestTemplate.postForEntity(TODOS_RESOURCE_URL, request, Todo.class);

        Todo response = responseEntity.getBody();

        assertThat(response).isNotNull();
        assertThat(response.getText()).isEqualTo(request.getText());

        ResponseEntity<Todo[]> listResponseEntity =
                testRestTemplate.getForEntity(TODOS_RESOURCE_URL, Todo[].class);


        Todo[] todos = listResponseEntity.getBody();

        assertThat(todos).isNotNull();
        assertThat(todos.length).isEqualTo(4);
    }

    @Test
    void shouldUpdateTodoWithId2WhenPutRequestIsMadeToIndividualTodoEndpoint() {
        Todo toBeUpdated = new Todo("Updated Todo !");

        testRestTemplate.put(TODOS_RESOURCE_URL + "/" + 2, toBeUpdated);

        ResponseEntity<Todo> responseEntity = testRestTemplate.getForEntity(TODOS_RESOURCE_URL + "/" + 2, Todo.class);

        Todo todo = responseEntity.getBody();


        assertThat(todo).isNotNull();
        assertThat(todo.getText()).isEqualTo(toBeUpdated.getText());
    }

    @Test
    void shouldDeleteTodoWithId3WhenDeleteRequestIsMadeToIndividualTodoEndpoint() {

        testRestTemplate.delete(TODOS_RESOURCE_URL + "/" + 2);

        ResponseEntity<Todo> responseEntity = testRestTemplate.getForEntity(TODOS_RESOURCE_URL + "/" + 2, Todo.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);


        ResponseEntity<Todo[]> listResponseEntity =
                testRestTemplate.getForEntity(TODOS_RESOURCE_URL, Todo[].class);


        Todo[] todos = listResponseEntity.getBody();

        assertThat(todos).isNotNull();
        assertThat(todos.length).isEqualTo(2);
    }
}

