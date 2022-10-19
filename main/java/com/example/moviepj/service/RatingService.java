package com.example.moviepj.service;

import com.example.moviepj.csv.Parser;
import com.example.moviepj.payload.request.ReviewRequest;
import com.example.moviepj.persistance.entity.MovieEntity;
import com.example.moviepj.persistance.entity.ReviewEntity;
import com.example.moviepj.persistance.entity.UserEntity;
import com.example.moviepj.persistance.entity.status.SubscriptionStatus;
import com.example.moviepj.persistance.repository.MovieRepository;
import com.example.moviepj.persistance.repository.RatingRepository;
import com.example.moviepj.persistance.repository.UserRepository;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final Parser parser;

    @Autowired
    public RatingService(RatingRepository ratingRepository, MovieRepository movieRepository, UserRepository userRepository, Parser parser) {
        this.ratingRepository = ratingRepository;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.parser = parser;
    }

    public void save(MultipartFile file) {
        try {
            System.out.printf(">>>>>>>>>>>>>Starting the CSV import %s%n", new Date());
            List<ReviewEntity> reviews = parser.csvToReviewEntity(file.getInputStream());
            List<MovieEntity> movies = movieRepository.findAll();
            List<UserEntity> users = userRepository.findAll();
            Map<Long, MovieEntity> movieMap = movies.stream()
                    .collect(Collectors.toMap(MovieEntity::getId, x -> x));
            Map<Long, UserEntity> userMap = users.stream()
                    .collect(Collectors.toMap(UserEntity::getId, x -> x));
            for (ReviewEntity review : reviews) {
                review.setMovie(movieMap.get(review.getMovie().getId()));
                review.setUser(userMap.get(review.getUser().getId()));
            }
            System.out.printf(">>>>>>>>>>>>>Still Going the CSV import %s%n", new Date());
            ratingRepository.saveAll(reviews);
            System.out.printf(">>>>>>>>>>>>>Ending the CSV import %s%n", new Date());
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }


    public ResponseEntity<?> addReview(ReviewRequest review) throws Exception {
        if(!movieRepository.existsById(review.getMovieId())){
            return ResponseEntity.badRequest().body(("no movie found"));
        }else{
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username;
            if(principal instanceof UserDetails){
                 username = ((UserDetails) principal).getUsername();
            }else{
                 username = principal.toString();
            }
            UserEntity user = userRepository.getByEmail(username);
            if(user.getSubscriptionStatus() != SubscriptionStatus.SUBSCRIBED) return ResponseEntity.badRequest().body("cannot leave review if you are not subscribed");
            MovieEntity movie = movieRepository.getById(review.getMovieId());
            ReviewEntity reviewEntity = new ReviewEntity(review.getTitle(), review.getScore(), movie, user);
            try {
                ratingRepository.save(reviewEntity);
                return ResponseEntity.ok(("success"));
            }catch (Exception e){
                return ResponseEntity.badRequest().body(("sad"));
            }
        }
    }

    public List<ReviewEntity> getReviews() {
        return ratingRepository.findAll();
    }
}

