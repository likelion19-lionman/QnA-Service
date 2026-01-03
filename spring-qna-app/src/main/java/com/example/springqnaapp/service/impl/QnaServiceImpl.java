package com.example.springqnaapp.service.impl;

import com.example.springqnaapp.common.dto.QnaRequestDto;
import com.example.springqnaapp.common.dto.QnaResponseDto;
import com.example.springqnaapp.common.exception.QnaNotFoundException;
import com.example.springqnaapp.common.exception.UnauthorizedException;
import com.example.springqnaapp.common.exception.UserNotFoundException;
import com.example.springqnaapp.domain.Comment;
import com.example.springqnaapp.domain.Qna;
import com.example.springqnaapp.domain.User;
import com.example.springqnaapp.repository.CommentRepository;
import com.example.springqnaapp.repository.QnaRepository;
import com.example.springqnaapp.repository.UserRepository;
import com.example.springqnaapp.service.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QnaServiceImpl implements QnaService {
	private final QnaRepository qnaRepository;
	private final UserRepository userRepository;
	private final CommentRepository commentRepository;

	@Override
	@Transactional
	public QnaResponseDto createQna(
			QnaRequestDto qnaRequestDto,
			String username
	) {
		User user = findUserOrThrow(username);

		if (!user.hasRole("ROLE_USER"))
			throw new UnauthorizedException("권한이 없습니다.");

		Qna qna = qnaRepository.save(new Qna(user, qnaRequestDto.title()));
		commentRepository.save(new Comment(qnaRequestDto.comment(), user, qna));

		return new QnaResponseDto(qna.getId(), qna.getUser().getUsername(), qna.getTitle());
	}

	@Override
	@Transactional(readOnly = true)
	public Page<QnaResponseDto> pagingQna(int page, int size, String username) {
		User user = findUserOrThrow(username);

		Pageable pageable = PageRequest.of(page, size,
		                                   Sort.by(Sort.Direction.DESC, "createdAt"));

		if (user.hasRole("ROLE_ADMIN"))
			return qnaRepository.findAll(pageable).map(QnaResponseDto::from);

		return qnaRepository
				.findByUserId(user.getId(), pageable)
				.map(QnaResponseDto::from);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Comment> retrieveQna(long qnaId, String username) {
		Qna qna = findQnaOrThrow(qnaId);

		if (!qna.accessible(username))
			throw new UnauthorizedException("열람 권한이 없습니다.");

		return qna.getComments();
	}

	@Override
	@Transactional
	public Comment addComment(long qnaId, String comment, String username) {
		Qna qna = findQnaOrThrow(qnaId);
		User curUser = findUserOrThrow(username);

		if (!qna.commentable(curUser))
			throw new UnauthorizedException("질문을 할 수 없습니다. 답변을 받은 후 질문을 다시 해주세요.");

		return commentRepository.save(new Comment(comment, curUser, qna));
	}

	@Override
	@Transactional
	public void deleteQna(long qnaId, String username) {
		Qna qna = findQnaOrThrow(qnaId);

		if (!qna.accessible(username))
			throw new UnauthorizedException("삭제 권한이 없습니다.");

		qnaRepository.deleteById(qnaId);
	}

	private User findUserOrThrow(String username) {
		return userRepository
				.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다."));
	}

	private Qna findQnaOrThrow(long qnaId) {
		return qnaRepository.findById(qnaId)
		             .orElseThrow(() -> new QnaNotFoundException("Can't find Qna"));
	}
}
