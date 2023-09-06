package org.ai.roboadvisor.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.user.dto.request.SignInRequest;
import org.ai.roboadvisor.domain.user.dto.request.SignUpRequest;
import org.ai.roboadvisor.domain.user.service.UserService;
import org.ai.roboadvisor.global.common.dto.SuccessApiResponse;
import org.ai.roboadvisor.global.exception.CustomException;
import org.ai.roboadvisor.global.exception.ErrorCode;
import org.ai.roboadvisor.global.exception.SuccessCode;
import org.ai.roboadvisor.global.swagger_annotation.ApiResponse_Internal_Server_Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "user", description = "회원가입 및 로그인 수행")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "회원 가입을 수행하는 로직")
    @ApiResponse(responseCode = "201", description = "회원가입에 성공한 경우",
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example",
                            description = "회원가입에 성공한 경우 응답 예시",
                            value = """
                                        {
                                            "code": 201,
                                            "message": "회원가입이 정상적으로 처리되었습니다",
                                            "data": null
                                        }
                                    """
                    )))
    @ApiResponse(responseCode = "400", description = "이메일이 중복된 경우",
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example",
                            description = "이메일이 중복된 경우 응답 예시",
                            value = """
                                       {
                                           "code": 400,
                                           "message": "이미 가입된 이메일입니다",
                                           "data": null
                                       }
                                    """
                    )))
    @ApiResponse(responseCode = "400", description = "닉네임이 중복된 경우",
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example",
                            description = "닉네임이 중복된 경우 응답 예시",
                            value = """
                                       {
                                           "code": 400,
                                           "message": "이미 가입된 닉네임입니다",
                                           "data": null
                                       }
                                    """
                    )))
    @ApiResponse_Internal_Server_Error
    @PostMapping("/signup")
    public ResponseEntity<SuccessApiResponse<?>> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        int SIGNUP_SUCCESS = 0;
        int INTERNAL_SERVER_ERROR = 1;
        int EMAIL_ALREADY_EXIST_IN_DB = 2;
        int NICKNAME_ALREADY_EXIST_IN_DB = 3;

        int result = userService.signUp(signUpRequest);

        if (result == SIGNUP_SUCCESS) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(SuccessApiResponse.success(SuccessCode.SIGNUP_SUCCESS));
        } else if (result == EMAIL_ALREADY_EXIST_IN_DB) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXIST_IN_DB);
        } else if (result == NICKNAME_ALREADY_EXIST_IN_DB) {
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXIST_IN_DB);
        } else {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "로그인", description = "로그인을 수행하는 로직")
    @ApiResponse(responseCode = "200", description = "로그인에 성공한 경우",
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example",
                            description = "로그인에 성공한 경우 응답 예시",
                            value = """
                                        {
                                            "code": 200,
                                            "message": "로그인에 성공하셨습니다",
                                            "data": null
                                        }
                                    """
                    )))
    @ApiResponse(responseCode = "400", description = "가입된 사용자의 정보가 존재하지 않는 경우",
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example",
                            description = "가입된 사용자의 정보가 존재하지 않는 경우 예시",
                            value = """
                                       {
                                           "code": 400,
                                           "message": "가입된 사용자의 정보가 존재하지 않습니다",
                                           "data": null
                                       }
                                    """
                    )))
    @PostMapping("/signin")
    public ResponseEntity<SuccessApiResponse<?>> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        int LOGIN_SUCCESS = 0;
        int USER_NOT_EXISTED = 1;

        int result = userService.signIn(signInRequest);
        if (result == LOGIN_SUCCESS) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(SuccessApiResponse.success(SuccessCode.LOGIN_SUCCESS));
        } else {
            throw new CustomException(ErrorCode.USER_NOT_EXISTED);
        }
    }


}
