package com.example.springqnaapp.service;

import com.example.springqnaapp.common.dto.QnaRequestDto;
import com.example.springqnaapp.common.dto.QnaResponseDto;
import com.example.springqnaapp.domain.Comment;
import org.springframework.data.domain.Page;

import java.util.List;

public interface QnaService {
	QnaResponseDto createQna(QnaRequestDto qnaRequestDto, String username);

	Page<QnaResponseDto> pagingQna(int page, int size, String username);

	List<Comment> retrieveQna(long qnaId, String username);

	Comment addComment(long qnaId, String comment, String username);

	void deleteQna(long qnaId, String username);
}
