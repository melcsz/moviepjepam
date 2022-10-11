package com.example.moviepj.service;

import com.example.moviepj.csv.Parser;
import com.example.moviepj.persistance.entity.GenreEntity;
import com.example.moviepj.persistance.entity.StudioEntity;
import com.example.moviepj.persistance.repository.MovieRepository;
import com.example.moviepj.persistance.entity.MovieEntity;
import com.example.moviepj.service.criteria.MovieSearchCriteria;
import com.example.moviepj.service.dto.MovieDto;
import com.example.moviepj.service.model.MovieWrapper;
import com.example.moviepj.service.model.QueryResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final Parser parser;

    @Autowired
    public MovieService(MovieRepository movieRepository, Parser parser) {
        this.movieRepository = movieRepository;
        this.parser = parser;
    }

    public void saveMovie(MultipartFile file) {
        try {
            System.out.printf(">>>>>>>>>>>>>Starting the CSV import %s%n", new Date());
            List<MovieEntity> movies = parser.csvToMovieEntity(file.getInputStream());
            List<String> existingMovie = movieRepository.getAllMovieTitles();
            movies.removeIf(movie -> existingMovie.contains(movie.getTitle()));
            Map<String, GenreEntity> genres = movies.stream()
                    .map(MovieEntity::getGenre)
                    .collect(Collectors.toMap(GenreEntity::getName, x -> x, (a1, a2) -> a1));
            Map<String, StudioEntity> studios = movies.stream()
                    .map(MovieEntity::getStudio)
                    .collect(Collectors.toMap(StudioEntity::getName, x -> x, (a1, a2) -> a1));

            for (MovieEntity movie : movies) {
                movie.setGenre(genres.get(movie.getGenre().getName()));
                movie.setStudio(studios.get(movie.getStudio().getName()));
            }

            movieRepository.saveAll(movies);
            System.out.printf(">>>>>>>>>>>>>Ending the CSV import %s%n", new Date());
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public QueryResponseWrapper<MovieDto> getMovies(MovieSearchCriteria criteria) {
        Page<MovieWrapper> content;
        Page<MovieDto> dtoContent;
        if (criteria.getDirector() == null &&
                criteria.getGenre() == null &&
                criteria.getStudio() == null &&
                criteria.getTitle() == null) {
            content = movieRepository.getAll(criteria.createPage());
            dtoContent = content.map(movieWrapper -> new MovieDto(movieWrapper.getId(), movieWrapper.getTitle(),movieWrapper.getReleaseYear()));
        }else if(criteria.getDirector() == null &&
                criteria.getGenre() == null &&
                criteria.getStudio() == null &&
                criteria.getTitle() != null){
            content = movieRepository.getMovieByTitle(criteria.getTitle(), criteria.createPage());
            dtoContent = content.map(movieWrapper -> new MovieDto(movieWrapper.getId(), movieWrapper.getTitle(),movieWrapper.getReleaseYear()));
        }
        else {
            content = movieRepository.getMovies(
                    criteria.getGenre() == null ? "" : criteria.getGenre(),
                    criteria.getStudio() == null ? "" : criteria.getStudio(),
                    criteria.getDirector() == null ? "" : criteria.getDirector(),
                    criteria.getTitle() == null ? "" : criteria.getTitle(),
                    criteria.createPage());
            dtoContent = content.map(movieWrapper -> new MovieDto(movieWrapper.getId(), movieWrapper.getTitle(),movieWrapper.getReleaseYear()));
        }
        return new QueryResponseWrapper<>(dtoContent.getTotalElements(), dtoContent.getContent());
    }
    
}
