package com.example.springqnaapp.common.dto;

import com.example.springqnaapp.domain.Comment;
import com.example.springqnaapp.domain.Qna;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({"title", "comments"}) // 응답 순서 정렬
public record QnaResponseDto(
        String title,
        List<Comment> comments
) {
    public static QnaResponseDto from(Qna qna) {
        return new QnaResponseDto(qna.getTitle(), qna.getComments());
    }
}
