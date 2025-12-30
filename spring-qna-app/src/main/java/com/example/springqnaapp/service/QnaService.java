package com.example.springqnaapp.service;

import com.example.springqnaapp.common.dto.QnaRequestDto;
import com.example.springqnaapp.common.dto.QnaResponseDto;
import com.example.springqnaapp.domain.Comment;
import org.springframework.data.domain.Page;

import java.util.List;

public interface QnaService {
	QnaResponseDto createQna(QnaRequestDto qnaRequestDto, String username);

	Page<QnaResponseDto> pagingQna(Integer page, Integer size, String username);

	List<Comment> retrieveQna(Long id, String username);

	Comment addComment(Long id, String comment, String username);

	void deleteQna(Long id, String username);
}
