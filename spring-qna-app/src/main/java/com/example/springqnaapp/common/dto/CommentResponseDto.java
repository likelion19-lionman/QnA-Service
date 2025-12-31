package com.example.springqnaapp.common.dto;

import com.example.springqnaapp.domain.Comment;

public record CommentResponseDto(
        String username,
		String comment
) {
	public static CommentResponseDto from(Comment comment) {
		return new  CommentResponseDto(
                comment.getUser().getUsername(),
				comment.getComment()
		);
	}
}