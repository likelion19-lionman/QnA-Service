package com.example.springqnaapp.controller;

import com.example.springqnaapp.common.dto.QnaRequestDto;
import com.example.springqnaapp.common.dto.QnaResponseDto;
import com.example.springqnaapp.service.QnaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/qna")
public class QnaController {

    private final QnaService qnaService;

    @PostMapping
    public ResponseEntity<?> query(@Valid QnaRequestDto requestDto,
                                   Principal principal,
                                   BindingResult bindingResult) {

        // 제목 혹은 내용이 비어있을 경우 오류 출력
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        String username = principal.getName();
        QnaResponseDto responseDto = qnaService.createQna(requestDto, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<?> retrieveQnas(@RequestParam(defaultValue = "0")
                                          @Min(value = 0, message = "페이지는 0 이상 값이어야 합니다.")
                                          Integer page,

                                          @RequestParam(defaultValue = "10")
                                          @Min(value = 1, message = "사이즈는 1 이상 값이어야 합니다.")
                                          Integer size,

                                          Principal principal) {

        String username = principal.getName();
        Page<QnaResponseDto> qnaPage = qnaService.retrieveQnaPage(page, size, username);
        return ResponseEntity.ok(qnaPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> retrieveQna(@PathVariable(value = "id") Long qnaId,
                                         Principal principal) {

        String username = principal.getName();
        QnaResponseDto response = qnaService.retrieveQna(qnaId, username);
        return ResponseEntity.ok(response);
    }

    // TODO: 질답
//    @PostMapping("/{id}")

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQna(@PathVariable("id") Long qnaId,
                                       Principal principal) {

        // qnaId 값이 null로 들어올 경우 삭제되는 값이 없도록 설정
        if (qnaId == null) {
            return ResponseEntity.noContent().build();
        }

        String username = principal.getName();
        qnaService.delete(qnaId, username);
        return ResponseEntity.noContent().build();
    }
}
