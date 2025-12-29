package com.example.springqnaapp.common.dto;

import com.example.springqnaapp.domain.Comment;

public record CommentResponseDto(
		Long userId,
		String comment
) {
	public static CommentResponseDto from(Comment comment) {
		return new  CommentResponseDto(
				comment.getUser().getId(),
				comment.getComment()
		);
	}
}