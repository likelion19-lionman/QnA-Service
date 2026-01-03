package com.example.springqnaapp.controller;

import com.example.springqnaapp.common.dto.CommentResponseDto;
import com.example.springqnaapp.common.dto.QnaRequestDto;
import com.example.springqnaapp.common.dto.QnaResponseDto;
import com.example.springqnaapp.service.QnaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/qna")
public class QnaController {

    private final QnaService qnaService;

    @PostMapping
    public ResponseEntity<?> query(
            @Valid @RequestBody QnaRequestDto requestDto,
            Principal principal
    ) {
        String username = principal.getName();
        QnaResponseDto responseDto = qnaService.createQna(requestDto, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<Page<QnaResponseDto>> pagingQna(
            @RequestParam(defaultValue = "0")
            Integer page,
            @RequestParam(defaultValue = "10")
            Integer size,
            Principal principal
    ) {
        String username = principal.getName();
        Page<QnaResponseDto> qnaPage = qnaService.pagingQna(page, size, username);
        return ResponseEntity.ok(qnaPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<CommentResponseDto>> retrieveQna(
            @PathVariable(value = "id") Long qnaId,
            Principal principal
    ) {
        String username = principal.getName();
        var comments = qnaService.retrieveQna(qnaId, username)
                                 .stream()
                                 .map(CommentResponseDto::from)
                                 .toList();
	    return ResponseEntity.ok(comments);
    }

    @PostMapping(
            value = "/{id}",
            consumes = "text/plain",
            produces = "application/json"
    )
	public ResponseEntity<?> addComment(
			@PathVariable(value = "id") Long qnaId,
			@RequestBody String comment,
			Principal principal
    ) {
        if (comment == null || comment.trim().isEmpty())
            throw new IllegalArgumentException("댓글 내용은 필수입니다.");
		String username = principal.getName();
		var result = qnaService.addComment(qnaId, comment, username);
		return ResponseEntity.ok(CommentResponseDto.from(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQna(
            @PathVariable("id") Long qnaId,
            Principal principal
    ) {
        String username = principal.getName();
        qnaService.deleteQna(qnaId, username);
        return ResponseEntity.noContent().build();
    }
}
