package com.example.moviepj.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.example.moviepj.persistance.entity.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

@Component
public class Parser {

    public List<UserEntity> csvToUserEntity(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new org.apache.commons.csv.CSVParser(fileReader,
                     CSVFormat.EXCEL.withDelimiter(',')
                             .withSkipHeaderRecord(true)
                             .withTrim()
                             .withIgnoreEmptyLines(true)
                             .withQuote('"')
                             .withIgnoreHeaderCase()
                             .withHeader(
                                     "first_name", "last_name", "email", "password",
                                     "age", "address_name", "city_name", "country_name", "id"))) {

            List<UserEntity> listOfUsers = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                if (Integer.parseInt(csvRecord.get("age")) > 16) {
                    CityEntity city = new CityEntity(csvRecord.get("city_name"), csvRecord.get("country_name"));
                    AddressEntity address = new AddressEntity(csvRecord.get("address_name"), city);
                    UserEntity user = new UserEntity(csvRecord.get("first_name"),
                            csvRecord.get("last_name"), csvRecord.get("email"), csvRecord.get("password"), Integer.parseInt(csvRecord.get("age")),
                            address);
                    listOfUsers.add(user);
                }
            }
            return listOfUsers;
        } catch (IOException e) {
            throw new RuntimeException("failed to parse CSV file: " + e.getMessage());
        }
    }

    public List<MovieEntity> csvToMovieEntity(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new org.apache.commons.csv.CSVParser(fileReader,
                     CSVFormat.EXCEL.withDelimiter(',')
                             .withSkipHeaderRecord(true)
                             .withTrim()
                             .withIgnoreEmptyLines(true)
                             .withQuote('"')
                             .withIgnoreHeaderCase()
                             .withHeader(
                                     "Title", "critic_rating", "Year-Of-Publication",
                                     "Director", "Genre", "Studio", "Image-Url-S", "Image-Url-M", "Image-Url-L"
                             ))) {

            List<MovieEntity> listOfMovies = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                try {

                    DirectorEntity director = new DirectorEntity(csvRecord.get("Director"));
                    StudioEntity studio = new StudioEntity(csvRecord.get("Studio"));
                    GenreEntity genre = new GenreEntity(csvRecord.get("Genre"));
                    MovieEntity movie = new MovieEntity(csvRecord.get("Title"), Double.parseDouble(csvRecord.get("critic_rating")),
                            Integer.parseInt(csvRecord.get("Year-Of-Publication")), director, genre, studio, csvRecord.get("Image-Url-S"),
                            csvRecord.get("Image-Url-M"), csvRecord.get("Image-Url-L"));
                    listOfMovies.add(movie);
                } catch (Exception ignored) {
                }
            }
            return listOfMovies;

        } catch (IOException e) {
            throw new RuntimeException("failed to parse CSV file: " + e.getMessage());
        }
    }

    public List<ReviewEntity> csvToReviewEntity(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new org.apache.commons.csv.CSVParser(fileReader,
                     CSVFormat.EXCEL.withDelimiter(',')
                             .withSkipHeaderRecord(true)
                             .withTrim()
                             .withIgnoreEmptyLines(true)
                             .withQuote('"')
                             .withIgnoreHeaderCase()
                             .withHeader(
                                     "title", "score",
                                     "movie-id", "user-id"))) {

            List<ReviewEntity> listOfReviews = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                try {
                    MovieEntity movie = new MovieEntity(Long.parseLong(csvRecord.get("movie-id")));
                    UserEntity user = new UserEntity(Long.parseLong(csvRecord.get("user-id")));
                    ReviewEntity review = new ReviewEntity((csvRecord.get("title")),
                            Integer.parseInt(csvRecord.get("score")), movie, user);
                    listOfReviews.add(review);
                } catch (Exception ignored) {

                }
            }
            return listOfReviews;

        } catch (
                IOException e) {
            throw new RuntimeException("failed to parse CSV file: " + e.getMessage());
        }
    }
}

