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
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new IllegalArgumentException("Can't find user"));

        Qna qna = new Qna(user, qnaRequestDto.title());
        Comment comment = new Comment(qnaRequestDto.comment());

        qna.addComment(comment);
        user.addQna(qna);

        qnaRepository.save(qna);
        return new QnaResponseDto(
                qna.getTitle(),
                qna.getComments()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QnaResponseDto> retrieveQnaPage(Integer page, Integer size, String username) {
        Pageable pageable = PageRequest.of(page, size);

        Page<QnaResponseDto> qnaPage = qnaRepository
                .findByUserUsernameOrderByCreatedAtDesc(username, pageable)
                .map(QnaResponseDto::from);

        return qnaPage;
    }

    @Override
    @Transactional(readOnly = true)
    public QnaResponseDto retrieveQna(Long qnaId, String username) {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(() ->
                new IllegalArgumentException("Can't find Qna"));

        if (!qna.getUser().getUsername().equals(username))
            throw new RuntimeException("열람 권한이 없습니다.");

        String title = qna.getTitle();
        List<Comment> comments = qna.getComments();

        return new QnaResponseDto(title, comments);
    }


    @Override
    @Transactional
    public void delete(Long qnaId, String username) {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(() ->
                new IllegalArgumentException("Can't find Qna"));

        if (qna.getUser().getUsername().equals(username))
            throw new RuntimeException("삭제 권한이 없습니다.");

        qnaRepository.delete(qna);
    }
}
