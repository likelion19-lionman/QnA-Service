package com.example.springqnaapp.common.dto;

import com.example.springqnaapp.domain.Comment;
import com.example.springqnaapp.domain.Qna;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

import java.util.List;

@JsonPropertyOrder({"title", "comments"}) // 응답 순서 정렬
@Builder
public record QnaResponseDto(String title,
                             List<Comment> comments) {

    public static QnaResponseDto from(Qna qna) {
        return QnaResponseDto.builder()
                .title(qna.getTitle())
                .comments(qna.getComments())
                .build();
    }
}
