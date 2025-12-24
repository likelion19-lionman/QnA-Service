package com.example.springqnaapp.common.dto;

import jakarta.validation.constraints.NotEmpty;

public record QnaRequestDto(@NotEmpty(message = "제목은 비어있지 않아야 합니다.")
                            String title,

                            @NotEmpty(message = "내용은 비어있지 않아야 합니다.")
                            String comment
) { }