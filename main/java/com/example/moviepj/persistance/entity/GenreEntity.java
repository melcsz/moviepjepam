package com.example.moviepj.persistance.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "genre", schema = "movie_copy",indexes =  {@Index(name = "IDX_MYGNR", columnList = "name")}
)
public class GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",length = 191)
    private String name;

    @OneToMany(mappedBy = "genre",fetch = FetchType.LAZY)
    private List<MovieEntity> movies;

    public GenreEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public GenreEntity() {
    }

    public GenreEntity(String genre) {
        this.name = genre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        GenreEntity genre = (GenreEntity) o;
        return Objects.equals(getName(), genre.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
