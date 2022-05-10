package com.learn_spring_boot.todo_app.todo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "todos")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Todo implements Serializable {

    private static final long serialVersionUID = 42L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "text", nullable = false)
    @NotNull
    private String text;

    @Column(name = "description")
    private String description;

    public Todo(String text) {
        this.text = text;
    }

    public Todo(String text, String description) {
        this.text = text;
        this.description = description;
    }

}

