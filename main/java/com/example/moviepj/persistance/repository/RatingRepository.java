package com.example.moviepj.persistance.repository;

import com.example.moviepj.persistance.entity.ReviewEntity;
import org.hibernate.cfg.JPAIndexHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<ReviewEntity, Long> {
}
