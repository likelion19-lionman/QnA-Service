package com.example.springqnaapp.controller.docs;

import com.example.springqnaapp.common.dto.CommentResponseDto;
import com.example.springqnaapp.common.dto.QnaRequestDto;
import com.example.springqnaapp.common.dto.QnaResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Tag(name = "QnA", description = "QnA API")
@SecurityRequirement(name = "JWT_Auth")
public interface QnaController {

	@Operation(
			summary = "질의 생성",
			description = "사용자의 질문을 위한 API"
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "400",
			             description = "입력된 값들이 유효하지 않은 경우"),
			@ApiResponse(responseCode = "404",
			             description = "시스템에 등록되지 않은 사용자인 경우"),
			@ApiResponse(responseCode = "401",
			             description = "사용자 계정이 아닌 경우"),
			@ApiResponse(responseCode = "201",
			             description = "생성된 QnA 의 데이터 반환")
	})
	ResponseEntity<QnaResponseDto> query(
			QnaRequestDto requestDto,
			Principal principal
	);

	@Operation(
			summary = "QnA 목록 보기",
			description = "QnA 목록을 페이지 단위로 볼 수 있는 API"
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "400",
			             description = "입력된 값들이 유효하지 않은 경우"),
			@ApiResponse(responseCode = "404",
			             description = "시스템에 등록되지 않은 사용자인 경우"),
			@ApiResponse(responseCode = "200",
			             description = "QnA 페이지 목록 반환")
	})
	ResponseEntity<Page<QnaResponseDto>> pagingQna(
			Integer page,
			Integer size,
			Principal principal
	);

	@Operation(
			summary = "QnA 조회",
			description = "QnA 글을 볼 수 있는 API"
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "400",
			             description = "입력된 값들이 유효하지 않은 경우"),
			@ApiResponse(responseCode = "404",
			             description = "시스템에 등록되지 않은 사용자이거나 시스템에 등록되지 않은 QnA 글 인 경우"),
			@ApiResponse(responseCode = "401",
			             description = "해당 글을 볼 수 있는 자격이 안되는 경우"),
			@ApiResponse(responseCode = "200",
			             description = "QnA 제목, 댓글들 반환")
	})
	ResponseEntity<List<CommentResponseDto>> retrieveQna(
			Long qnaId,
			Principal principal
	);

	@Operation(
			summary = "답변",
			description = "QnA 에 댓글을 달 수 있는 API"
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "404",
			             description = "시스템에 등록되지 않은 사용자이거나 시스템에 등록되지 않은 QnA 글 인 경우"),
			@ApiResponse(responseCode = "401",
			             description = "해당 글에 댓글을 남길 자격이 안되는 경우"),
			@ApiResponse(responseCode = "400",
			             description = "입력된 값들이 유효하지 않은 경우"),
			@ApiResponse(responseCode = "412",
			             description = "답변이 달리지 않았는데도 불구하고 작성자가 답변을 연속해서 달 경우"),
			@ApiResponse(responseCode = "200",
			             description = "QnA 제목, 댓글들 반환")
	})
	ResponseEntity<CommentResponseDto> addComment(
			Long qnaId,
			String comment,
			Principal principal
	);


	@Operation(
			summary = "QnA 삭제",
			description = "QnA 삭제를 위한 API"
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204",
			             description = "QnA 삭제")
	})
	ResponseEntity<Void> deleteQna(
			@PathVariable("id") Long qnaId,
			Principal principal
	);
}
