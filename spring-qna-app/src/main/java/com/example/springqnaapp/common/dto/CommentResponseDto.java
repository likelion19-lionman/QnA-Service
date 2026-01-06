package com.example.springqnaapp.common.dto;

import com.example.springqnaapp.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "댓글 응답 정보")
public record CommentResponseDto(

		@Schema(description = "작성자 이름",
		        example = "user123")
		String username,

		@Schema(description = "댓글 본문 내용",
		        example = "오늘 포스팅 정말 유익하네요! 잘 보고 갑니다.")
		String comment
) {
	public static CommentResponseDto from(Comment comment) {
		return new  CommentResponseDto(
                comment.getUser().getUsername(),
				comment.getComment()
		);
	}
}