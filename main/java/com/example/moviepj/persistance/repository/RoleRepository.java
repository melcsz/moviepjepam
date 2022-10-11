package com.example.moviepj.persistance.repository;

import com.example.moviepj.persistance.entity.RoleEntity;
import com.example.moviepj.service.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(Role name);
}