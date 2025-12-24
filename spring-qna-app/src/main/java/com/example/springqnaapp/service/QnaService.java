package com.example.springqnaapp.service;

import com.example.springqnaapp.common.dto.QnaRequestDto;
import com.example.springqnaapp.common.dto.QnaResponseDto;
import org.springframework.data.domain.Page;

public interface QnaService {
    QnaResponseDto createQna(QnaRequestDto qnaRequestDto, String username);
    Page<QnaResponseDto> retrieveQnaPage(Integer page, Integer size, String username);
    QnaResponseDto retrieveQna(Long id, String username);
    void delete(Long id, String username);
}
