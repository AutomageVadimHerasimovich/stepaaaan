package com.example.libraryservice.repository;

import com.example.libraryservice.entity.BookStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookStatusRepository extends JpaRepository<BookStatusEntity, Long> {
}