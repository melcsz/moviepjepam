package com.example.moviepj.persistance.repository;

import com.example.moviepj.persistance.entity.UserEntity;
import com.example.moviepj.service.model.UserWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {


    @Query("Select u.email from UserEntity u")
    List<String> getAllUserEmails();

    Optional<UserEntity> findById(Long id);

    UserEntity findByEmail(String email);

    @Query("SELECT new com.example.moviepj.service.model.UserWrapper(u.id," +
            "u.firstName) FROM UserEntity u")
    Page<UserWrapper> findAllWithPagination(Pageable pageable);

    Boolean existsByEmail(String em);

    UserEntity getByEmail(String username);

    void deleteById(Long id);
}

