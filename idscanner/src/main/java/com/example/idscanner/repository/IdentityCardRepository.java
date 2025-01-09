package com.example.idscanner.repository;

import com.example.idscanner.model.CnieData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentityCardRepository extends JpaRepository<CnieData, Long> {
}