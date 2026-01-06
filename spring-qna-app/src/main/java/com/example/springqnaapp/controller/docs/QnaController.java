package com.example.springqnaapp.controller.docs;

import com.example.springqnaapp.common.dto.CommentResponseDto;
import com.example.springqnaapp.common.dto.QnaRequestDto;
import com.example.springqnaapp.common.dto.QnaResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Tag(name = "QnA", description = "QnA API")
@SecurityRequirement(name = "JWT_Auth")
public interface QnaController {

	@Operation(
			summary = "질의 생성",
			description = "사용자의 질문을 위한 API"
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "400",
			             description = "입력된 값들이 유효하지 않은 경우",
			             content = @Content(mediaType = "application/json",
			                                schema = @Schema(implementation = Map.class),
                                            examples = @ExampleObject(
                                                    name = "validation-error",
                                                    value = """
											                {
											                    "details": "(에러 메시지)",
											                }
											                """
                                             ))),
			@ApiResponse(responseCode = "404",
			             description = "시스템에 등록되지 않은 사용자인 경우",
			             content = @Content(mediaType = "application/json",
                                         schema = @Schema(implementation = ErrorResponse.class),
                                         examples = @ExampleObject(
                                                 value = """
											            {
											                "type": "about:blank",
											                "title": "Not Found",
											                "status": 404,
											                "detail": "등록되지 않은 사용자입니다.",
											                "instance": "/api/qna/query"
											            }
											            """
                                         ))),
			@ApiResponse(responseCode = "401",
			             description = "사용자 계정이 아닌 경우",
			             content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class),
                                            examples = @ExampleObject(
                                                    value = """
											                {
											                    "type": "about:blank",
											                    "title": "Unauthorized",
											                    "status": 401,
											                    "detail": "작성 권한이 없습니다.",
											                    "instance": "/api/qna/query"
											                }
											                """
                                            ))),
			@ApiResponse(responseCode = "201",
			             description = "생성된 QnA 의 데이터 반환",
			             content = @Content(mediaType = "application/json",
			                                schema = @Schema(implementation = QnaResponseDto.class)))
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
			             description = "입력된 값들이 유효하지 않은 경우",
			             content = @Content(mediaType = "application/json",
			                                schema = @Schema(implementation = Map.class),
                                                    examples = @ExampleObject(
                                                    name = "validation-error",
                                                    value = """
                                                            {
                                                                "details": "(에러 메시지)",
                                                            }
                                                            """
                                            ))),
			@ApiResponse(responseCode = "404",
			             description = "시스템에 등록되지 않은 사용자인 경우",
			             content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class),
                                            examples = @ExampleObject(
                                                    value = """
                                                            {
											                    "type": "about:blank",
											                    "title": "Not Found",
											                    "status": 404,
											                    "detail": "등록되지 않은 사용자입니다.",
											                    "instance": "/api/qna/pagingQna"
											                }
											                """
                                            ))),
			@ApiResponse(responseCode = "200",
			             description = "QnA 페이지 목록 반환",
			             content = @Content(mediaType = "application/json",
			                                schema = @Schema(implementation = Page.class)))
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
			             description = "입력된 값들이 유효하지 않은 경우",
			             content = @Content(mediaType = "application/json",
			                                schema = @Schema(implementation = Map.class),
                                            examples = @ExampleObject(
                                                    name = "validation-error",
                                                    value = """
											                {
											                    "details": "(에러 메시지)",
											                }
											                """
                                            ))),
			@ApiResponse(responseCode = "404",
			             description = "시스템에 등록되지 않은 사용자이거나 시스템에 등록되지 않은 QnA 글 인 경우",
			             content = @Content(mediaType = "application/json",
                                             schema = @Schema(implementation = ErrorResponse.class),
                                             examples = @ExampleObject(
                                                     value = """
                                                             {
                                                                "type": "about:blank",
                                                                "title": "Not Found",
                                                                "status": 404,
                                                                "detail": "등록되지 않은 사용자이거나 등록되지 않은 QnA입니다.",
                                                                "instance": "/api/qna/retrieveQna"
                                                             }
                                                             """
                                             ))),
			@ApiResponse(responseCode = "401",
			             description = "해당 글을 볼 수 있는 자격이 안되는 경우",
			             content = @Content(mediaType = "application/json",
                                             schema = @Schema(implementation = ErrorResponse.class),
                                             examples = @ExampleObject(
                                                        value = """
                                                            {
                                                                "type": "about:blank",
                                                                "title": "Unauthorized",
                                                                "status": 401,
                                                                "detail": "열람 권한이 없습니다.",
                                                                "instance": "/api/qna/retrieveQna"
                                                            }
                                                            """
                                             ))),
			@ApiResponse(responseCode = "200",
			             description = "QnA 제목, 댓글들 반환",
			             content = @Content(mediaType = "application/json",
			                                schema = @Schema(implementation = List.class)))
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
			             description = "시스템에 등록되지 않은 사용자이거나 시스템에 등록되지 않은 QnA 글 인 경우",
			             content = @Content(mediaType = "application/json",
                                             schema = @Schema(implementation = ErrorResponse.class),
                                             examples = @ExampleObject(
                                                     value = """
                                                            {
                                                                "type": "about:blank",
                                                                "title": "Not Found",
                                                                "status": 404,
                                                                "detail": "등록되지 않은 사용자이거나 등록되지 않은 QnA입니다.",
                                                                "instance": "/api/qna/addComment"
                                                            }
                                                            """
                                             ))),
			@ApiResponse(responseCode = "401",
			             description = "해당 글에 댓글을 남길 자격이 안되는 경우",
			             content = @Content(mediaType = "application/json",
                                             schema = @Schema(implementation = ErrorResponse.class),
                                             examples = @ExampleObject(
                                                     value = """
											                {
											                    "type": "about:blank",
											                    "title": "UNAUTHORIZED",
											                    "status": 401,
											                    "detail": "댓글 작성 권한이 없습니다.",
											                    "instance": "/api/qna/addComment"
											                }
											                """
                                            ))),
			@ApiResponse(responseCode = "400",
			             description = "입력된 값들이 유효하지 않은 경우",
			             content = @Content(mediaType = "application/json",
			                                schema = @Schema(implementation = Map.class),
                                            examples = @ExampleObject(
                                                    name = "validation-error",
                                                    value = """
											                {
											                    "details": "(에러 메시지)",
											                }
											                """

                                            ))),
			@ApiResponse(responseCode = "412",
			             description = "답변이 달리지 않았는데도 불구하고 작성자가 답변을 연속해서 달 경우",
			             content = @Content(mediaType = "application/json",
                                             schema = @Schema(implementation = ErrorResponse.class),
                                             examples = @ExampleObject(
                                                     value = """
											                {
											                    "type": "about:blank",
											                    "title": "Precondition Failed",
											                    "status": 412,
											                    "detail": "답변이 있어야 재답변을 달 수 있습니다",
											                    "instance": "/api/qna/addComment"
											                }
											                """
                                             ))),
			@ApiResponse(responseCode = "200",
			             description = "QnA 제목, 댓글들 반환",
			             content = @Content(mediaType = "application/json",
			                                schema = @Schema(implementation = CommentResponseDto.class)))
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
			Long qnaId,
			Principal principal
	);
}
