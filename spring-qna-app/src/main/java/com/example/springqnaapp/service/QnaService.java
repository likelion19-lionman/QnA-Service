package com.example.springqnaapp.service;

import com.example.springqnaapp.common.dto.QnaRequestDto;
import com.example.springqnaapp.common.dto.QnaResponseDto;
import com.example.springqnaapp.domain.Comment;
import org.springframework.data.domain.Page;

import java.util.List;

public interface QnaService {
	QnaResponseDto createQna(QnaRequestDto qnaRequestDto, String username);

	Page<QnaResponseDto> retrieveQnaPage(Integer page, Integer size, String username);

	List<Comment> retrieveQna(Long id, String username);

	Comment qna(Long id, String comment, String username);

	void delete(Long id, String username);
}
