package com.example.springqnaapp.common.dto;

import jakarta.validation.constraints.NotEmpty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Q&A 게시글 작성 요청 데이터")
public record QnaRequestDto(

        @Schema(
                description = "Q&A 질문 제목",
                example = "배송 문의드립니다.",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotEmpty(message = "제목은 비어있지 않아야 합니다.")
        String title,

        @Schema(
                description = "Q&A 질문 상세 내용",
                example = "어제 주문했는데 언제쯤 받아볼 수 있을까요?",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotEmpty(message = "내용은 비어있지 않아야 합니다.")
        String comment
) { }