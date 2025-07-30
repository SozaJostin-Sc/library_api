package com.personal.library.library_api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @ManyToMany(mappedBy = "books")
    @JsonBackReference
    private List<Loans> loans;  // Relation with loans

    private String title;
    private String isbn;
    @Column(name = "publication_year")
    private Integer publicationYear;
    private Integer pages;
    private String language;

    @ManyToMany
    @JsonManagedReference
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors; // relation with authors

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Categories categoryId;

    private Integer stock;
    private Boolean status;
}
