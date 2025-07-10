package com.github.vadimher.library.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String isbn;

    public void setId(Long id) {
        this.id = id;
    }

    private String title;
    private String genre;
    private String description;
    private String author;
}