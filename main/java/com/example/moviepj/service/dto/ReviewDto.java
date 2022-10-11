package com.example.moviepj.service.dto;

import com.example.moviepj.persistance.entity.MovieEntity;
import com.example.moviepj.persistance.entity.ReviewEntity;
import com.example.moviepj.persistance.entity.UserEntity;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;

public class ReviewDto {

    private Long id;

    private String reviewTitle;

    private Integer score;

    private MovieDto movie;

    private UserDto user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public MovieDto getMovie() {
        return movie;
    }

    public void setMovie(MovieDto movie) {
        this.movie = movie;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public static List<ReviewDto> mapReviewEntityToReviewDto(List<ReviewEntity> review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setReviewTitle(review.get(0).getReviewTitle());
        reviewDto.setScore(review.get(0).getScore());
        List<ReviewDto> reviewDtos = new ArrayList<>();
        reviewDtos.add(reviewDto);
        return reviewDtos;
    }


}
