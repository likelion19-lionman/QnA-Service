package com.example.springqnaapp.common.dto;

import com.example.springqnaapp.domain.Qna;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Q&A 게시글 응답 데이터")
public record QnaResponseDto(

		@Schema(description = "게시글 고유 식별자(ID)", example = "1")
		Long id,

		@Schema(description = "작성자 아이디", example = "user1234")
		String username,

		@Schema(description = "게시글 제목", example = "배송 문의드립니다.")
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