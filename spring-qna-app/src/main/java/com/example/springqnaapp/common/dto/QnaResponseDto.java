package com.example.springqnaapp.common.dto;

import com.example.springqnaapp.domain.Qna;

public record QnaResponseDto(
		Long id,
        String username,
        String title
) {
    public static QnaResponseDto from(Qna qna) {
        return new QnaResponseDto(
				qna.getId(),
                qna.getUser().getUsername(),
				qna.getTitle()
        );
    }
}
