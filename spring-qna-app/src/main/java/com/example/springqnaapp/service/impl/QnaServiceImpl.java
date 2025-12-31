package com.example.springqnaapp.service.impl;

import com.example.springqnaapp.common.dto.QnaRequestDto;
import com.example.springqnaapp.common.dto.QnaResponseDto;
import com.example.springqnaapp.domain.Comment;
import com.example.springqnaapp.domain.Qna;
import com.example.springqnaapp.domain.User;
import com.example.springqnaapp.repository.QnaRepository;
import com.example.springqnaapp.repository.UserRepository;
import com.example.springqnaapp.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QnaServiceImpl implements QnaService {
	private final QnaRepository qnaRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional
	public QnaResponseDto createQna(
			QnaRequestDto qnaRequestDto,
			String username
	) {
		User user = userRepository.findByUsername(username)
		                          .orElseThrow(() ->
				                                       new IllegalArgumentException("Can't find user"));

        if (!user.hasRole("ROLE_USER"))
            throw new RuntimeException("권한이 없습니다.");

		Qna qna = new Qna(user, qnaRequestDto.title());
		Comment comment = new Comment(qnaRequestDto.comment());

		qna.addComment(comment);
		user.addQna(qna);

		qnaRepository.save(qna);
		return new QnaResponseDto(
				qna.getId(),
                qna.getUser().getUsername(),
				qna.getTitle()
		);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<QnaResponseDto> pagingQna(Integer page, Integer size, String username) {
		Pageable pageable = PageRequest.of(page, size,
		                                   Sort.by(Sort.Direction.DESC, "createdAt"));
		User user = userRepository.findByUsername(username)
		                          .orElseThrow(() -> new IllegalArgumentException("Can't find user"));

		return qnaRepository
				.findByUserId(user.getId(), pageable)
				.map(QnaResponseDto::from);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Comment> retrieveQna(Long qnaId, String username) {
		Qna qna = qnaRepository.findById(qnaId)
		                       .orElseThrow(() -> new IllegalArgumentException("Can't find Qna"));
		if (!qna.accessible(username)) throw new RuntimeException("열람 권한이 없습니다.");
		return qna.getComments();
	}

	@Override
	@Transactional
	public Comment addComment(Long id, String comment, String username) {
		Qna qna = qnaRepository.findById(id)
		                       .orElseThrow(() -> new IllegalArgumentException("Can't find Qna"));

        // accessible = true, commentable = true
		if (!(qna.accessible(username) && qna.commentable(username)))
			throw new RuntimeException("질문을 할 수 없습니다. 답변을 받은 후 질문을 다시 해주세요.");

		return qna.addComment(new Comment(comment));
	}

	@Override
	@Transactional
	public void deleteQna(Long qnaId, String username) {
		Qna qna = qnaRepository.findById(qnaId)
		                       .orElseThrow(() -> new IllegalArgumentException("Can't find Qna"));

		if (!qna.accessible(username))
			throw new RuntimeException("삭제 권한이 없습니다.");

		qnaRepository.deleteById(qnaId);
	}
}
