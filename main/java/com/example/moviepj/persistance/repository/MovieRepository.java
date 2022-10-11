package com.example.moviepj.persistance.repository;

import com.example.moviepj.persistance.entity.MovieEntity;
import com.example.moviepj.service.model.MovieWrapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.Cacheable;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Long>, JpaSpecificationExecutor<MovieEntity> {

    MovieEntity getByUrlL(String urlL);

    @Query("SELECT m.id FROM MovieEntity m")
    List<Long> getAllMovieIds();

    @Query("SELECT m.title FROM MovieEntity m")
    List<String> getAllMovieTitles();

    @Query("SELECT m.urlL FROM MovieEntity m WHERE m.urlL NOT IN " +
            "(SELECT f.UrlL FROM FileEntity f WHERE " +
            "f.fileStatus = 'IN_PROGRESS' AND f.fileStatus = 'FINISHED')")
    List<String> findAllNotUploadedUrls();

    Optional<MovieEntity> findById(Long id);

    @Query("SELECT new com.example.moviepj.service.model.MovieWrapper(m.id," +
            "m.title, m.releaseYear) FROM MovieEntity m " +
            "LEFT JOIN m.studio s ON m.studio.id = s.id " +
            "LEFT JOIN m.genre g ON m.genre.id = g.id " +
            "LEFT JOIN m.director d ON m.director.id = d.id WHERE" +
            " (:studio = '' OR s.name = :studio) " +
            "AND (:genre = '' OR g.name = :genre)" +
            "AND (:director = '' OR d.fullName = :director)" +
            "AND (:title = '' OR m.title LIKE %:title)" +
            "GROUP BY m.id, m.title, m.releaseYear")
    Page<MovieWrapper> getMovies(String genre,
                                 String studio,
                                 String director,
                                 String title,
                                 Pageable pageable);


    @Query("SELECT new com.example.moviepj.service.model.MovieWrapper(m.id," +
            "m.title, m.releaseYear) FROM MovieEntity m WHERE m.title LIKE %:title")
    Page<MovieWrapper> getMovieByTitle(String title, Pageable pageable);

    @Query("SELECT new com.example.moviepj.service.model.MovieWrapper(m.id," +
            "m.title, m.releaseYear) FROM MovieEntity m ")
    Page<MovieWrapper> getAll(Pageable pageable);

    MovieEntity getById(Long id);


}
