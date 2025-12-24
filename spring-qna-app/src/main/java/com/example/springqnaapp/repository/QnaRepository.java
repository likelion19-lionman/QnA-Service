package com.example.springqnaapp.repository;

import com.example.springqnaapp.domain.Qna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaRepository extends JpaRepository<Qna, Long> {
    Page<Qna> findByUserUsernameOrderByCreatedAtDesc(String username, Pageable pageable);
}