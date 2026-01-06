package com.example.springqnaapp.controller.docs;

import com.example.springqnaapp.common.dto.EmailCodeRequestDto;
import com.example.springqnaapp.common.dto.EmailVerifyRequestDto;
import com.example.springqnaapp.common.dto.LoginRequestDto;
import com.example.springqnaapp.common.dto.LogoutRequestDto;
import com.example.springqnaapp.common.dto.RegisterRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;

import java.util.Map;

@Tag(name = "Auth", description = "사용자 관리 API")
public interface AuthController {
	@Operation(
			summary = "아이디 중복 체크",
			description = "회원가입을 위해 아이디 중복 체크를 합니다."
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
			@ApiResponse(responseCode = "200",
			             description = "사용 가능한 아이디 혹은 불가한 아이디의 참 거짓 값 반환",
			             content = @Content(mediaType = "application/json",
			                                schema = @Schema(implementation = Boolean.class),
			                                examples = @ExampleObject(
					                                name = "중복",
					                                value = "false"
			                                ))),
	})
	ResponseEntity<Boolean> checkDuplication(
			@Parameter(description = "사용자 아이디")
			String username
	);

	@Operation(
			summary = "이메일 인증코드 전송",
			description = "교차 검증을 위한 이메일 인증 코드 발송"
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
			@ApiResponse(responseCode = "409",
			             description = "현재 인증 진행중인 이메일이거나 가입된 이메일",
			             content = @Content(mediaType = "application/json",
			                                schema = @Schema(implementation = ProblemDetail.class),
			                                examples = @ExampleObject(
					                                value = """
											                {
											                    "type": "about:blank",
											                    "title": "Conflict",
											                    "status": 409,
											                    "detail": "이미 인증 진행중인 이메일이거나 가입된 이메일입니다.",
											                    "instance": "/api/auth/sendAuthCode"
											                }
											                """
			                                ))),
			@ApiResponse(responseCode = "503",
			             description = "메일 전송 서버가 통신 불가 상태",
			             content = @Content(mediaType = "application/json",
			                                schema = @Schema(implementation = ProblemDetail.class),
			                                examples = @ExampleObject(
					                                value = """
											                {
											                    "type": "about:blank",
											                    "title": "Service Unavailable",
											                    "status": 503,
											                    "detail": "메일 전송 서버가 통신 불가 상태입니다.",
											                    "instance": "/api/auth/sendAuthCode"
											                }
											                """
			                                ))),
			@ApiResponse(responseCode = "204",
			             description = "전송 완료")
	})
	ResponseEntity<Void> sendAuthCode(
			EmailCodeRequestDto emailCodeResponseDto
	);

	@Operation(
			summary = "이메일 인증코드 확인",
			description = "이메일 코드 인증 요청"
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
			@ApiResponse(responseCode = "200",
			             description = "이메일 코드 일치 혹은 불일치를 참 거짓 값으로 반환",
			             content = @Content(mediaType = "application/json",
			                                schema = @Schema(implementation = Boolean.class))),
			@ApiResponse(responseCode = "404",
			             description = "해당 이메일에 대해 인증코드를 요청한 내역이 없는 경우",
			             content = @Content(mediaType = "application/json",
			                                schema = @Schema(implementation = ProblemDetail.class),
			                                examples = @ExampleObject(
					                                value = """
											                {
											                    "type": "about:blank",
											                    "title": "Not Found",
											                    "status": 404,
											                    "detail": "인증코드를 요청한 내역이 없습니다.",
											                    "instance": "/api/auth/verifyAuthCode"
											                }
											                """
			                                ))),
			@ApiResponse(responseCode = "410",
			             description = "인증 시간이 만료되어 다시 전송해야 하는 경우",
			             content = @Content(mediaType = "application/json",
                                         schema = @Schema(implementation = ProblemDetail.class),
                                         examples = @ExampleObject(
                                                 value = """
                                                        {
                                                            "type": "about:blank",
                                                            "title": "Gone",
                                                            "status": 410,
                                                            "detail": "인증코드가 만료되었습니다.",
                                                            "instance": "/api/auth/verifyAuthCode"
                                                        }
                                                        """
                                         ))),
	})
	ResponseEntity<Boolean> verifyAuthCode(
			EmailVerifyRequestDto emailVerifyRequestDto
	);

	@Operation(
			summary = "회원가입",
			description = "사용자 회원가입을 위한 API"
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
			@ApiResponse(responseCode = "409",
			             description = "이미 있는 사용자인 경우",
			             content = @Content(mediaType = "application/json",
			                                schema = @Schema(implementation = ProblemDetail.class),
                                            examples = @ExampleObject(
                                                    value = """
											                {
											                    "type": "about:blank",
											                    "title": "Conflict",
											                    "status": 409,
											                    "detail": "이미 사용 중인 아이디입니다.",
											                    "instance": "/api/auth/register"
											                }
											                """
                                            ))),
			@ApiResponse(responseCode = "403",
			             description = "이메일 교차 검증이 안된 경우",
			             content = @Content(mediaType = "application/json",
			                                schema = @Schema(implementation = ProblemDetail.class),
                                            examples = @ExampleObject(
                                                    value = """
											                {
											                    "type": "about:blank",
											                    "title": "Forbidden",
											                    "status": 403,
											                    "detail": "이메일 인증이 완료되지 않았습니다.",
											                    "instance": "/api/auth/register"
											                }
											                """
                                            ))),
			@ApiResponse(responseCode = "200",
			             description = "쿠키로 액세스 토큰을 받으며, 응답 본문으로 갱신 토큰을 받습니다.",
			             content = @Content(mediaType = "text/plain",
			                                schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "404",
			             description = "자동 로그인 도중 회원 가입이 되지 않아 시스템에 해당 아이디가 등록이 안되어 있는 경우",
			             content = @Content(mediaType = "application/json",
			                                schema = @Schema(implementation = ProblemDetail.class),
                                            examples = @ExampleObject(
                                                        value = """
											                {
											                    "type": "about:blank",
											                    "title": "Not Found",
											                    "status": 404,
											                    "detail": "아이디를 찾을 수 없습니다.",
											                    "instance": "/api/auth/register"
											                }
											                """
                                            ))),
	})
	ResponseEntity<String> register(
			RegisterRequestDto registerRequestDto,
			HttpServletResponse response
	);

	@Operation(
			summary = "로그인",
			description = "사용자 로그인을 위한 API"
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
			@ApiResponse(responseCode = "200",
			             description = "쿠키로 액세스 토큰을 받으며, 응답 본문으로 갱신 토큰을 받습니다.",
			             content = @Content(mediaType = "text/plain",
			                                schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "404",
			             description = "시스템에 해당 아이디가 등록이 안되어 있는 경우",
			             content = @Content(mediaType = "application/json",
			                                schema = @Schema(implementation = ProblemDetail.class),
                                            examples = @ExampleObject(
                                                    value = """
											                {
											                    "type": "about:blank",
											                    "title": "Not Found",
											                    "status": 404,
											                    "detail": "존재하지 않는 회원입니다.",
											                    "instance": "/api/auth/login"
											                }
											                """
                                            ))),
	})
	ResponseEntity<String> login(
			LoginRequestDto loginRequestDto,
			HttpServletResponse response
	);

	@Operation(
			summary = "로그아웃",
			description = "사용자 로그아웃을 위한 API"
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
			@ApiResponse(responseCode = "204",
			             description = "토큰이 안전하게 삭제가 된 경우")
	})
	ResponseEntity<Void> logout(
			LogoutRequestDto request,
			HttpServletResponse response
	);

	@Operation(
			summary = "리프레시",
			description = "자동 토큰 갱신을 위한 API"
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
			@ApiResponse(responseCode = "401",
			             description = "갱신 토큰이 유효하지 않은 경우",
			             content = @Content(mediaType = "application/json",
			                                schema = @Schema(implementation = ProblemDetail.class),
			                                examples = @ExampleObject(
					                                value = """
											                {
											                    "type": "about:blank",
											                    "title": "UNAUTHORIZED",
											                    "status": 401,
											                    "detail": "토큰이 유효하지 않습니다.",
											                    "instance": "/api/auth/refresh"
											                }
											                """
			                                ))),
			@ApiResponse(responseCode = "204",
			             description = "접근 토큰이 안전하게 갱신된 경우")
	})
	ResponseEntity<Void> refresh(
			@Parameter(
					description = "갱신 토큰",
					example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
			)
			String refreshToken,
			HttpServletResponse response
	);

	@Operation(
			summary = "코드 재전송",
			description = "코드를 받지 못하거나 볼 수 없을 때 재요청을 위한 API"
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
			@ApiResponse(responseCode = "409",
			             description = "현재 인증 진행중인 이메일이거나 가입된 이메일",
			             content = @Content(mediaType = "application/json",
			                                schema = @Schema(implementation = ProblemDetail.class),
                                            examples = @ExampleObject(
                                                value = """
											                {
											                    "type": "about:blank",
											                    "title": "Conflict",
											                    "status": 409,
											                    "detail": "이미 인증 진행중인 이메일이거나 가입된 이메일입니다.",
											                    "instance": "/api/auth/email/resendAuthCode"
											                }
											                """
                                            ))),
			@ApiResponse(responseCode = "503",
			             description = "메일 전송 서버가 통신 불가 상태",
			             content = @Content(mediaType = "application/json",
			                                schema = @Schema(implementation = ProblemDetail.class),
                                            examples = @ExampleObject(
                                                value = """
											                {
											                    "type": "about:blank",
											                    "title": "Service Unavailable",
											                    "status": 503,
											                    "detail": "메일 전송 서버가 통신 불가 상태입니다.",
											                    "instance": "/api/auth/email/resendAuthCode"
											                }
											                """
                                            ))),
			@ApiResponse(responseCode = "204",
			             description = "전송 완료")
	})
	ResponseEntity<Void> resendAuthCode(
			EmailCodeRequestDto emailCodeRequestDto
	);
}
