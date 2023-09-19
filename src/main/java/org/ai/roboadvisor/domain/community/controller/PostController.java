package org.ai.roboadvisor.domain.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.community.dto.request.PostRequest;
import org.ai.roboadvisor.domain.community.dto.response.PostResponse;
import org.ai.roboadvisor.domain.community.service.PostService;
import org.ai.roboadvisor.global.common.dto.SuccessApiResponse;
import org.ai.roboadvisor.global.exception.CustomException;
import org.ai.roboadvisor.global.exception.ErrorCode;
import org.ai.roboadvisor.global.exception.SuccessCode;
import org.ai.roboadvisor.global.swagger_annotation.ApiResponse_Internal_Server_Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "community] post", description = "게시글 작성, 수정, 삭제 API")
@RestController
@RequestMapping("/api/community/post")
public class PostController {
    private final PostService postService;

    private final int SUCCESS = 0;
    private final int TIME_INPUT_INVALID = -1;
    private final int INTERNAL_SERVER_ERROR = -100;

    @Operation(summary = "게시글 작성", description = "게시글 작성 API")
    @ApiResponse(responseCode = "201", description = """
            정상 응답
                        
            data로 게시글 정보를 리턴하며, id는 게시글의 고유 번호이다.
            """,
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example",
                            description = "정상 응답 예시",
                            value = """
                                       {
                                             "code": 201,
                                             "message": "게시글이 정상적으로 등록되었습니다",
                                             "data": {
                                                 "id": 3,
                                                 "type": "SHEEP",
                                                 "nickname": "testUser",
                                                 "content": "안녕하세요",
                                                 "time": "2023-09-17 23:44:33"
                                             }
                                         }
                                    """
                    )))
    @ApiResponse(responseCode = "400", description = "시간 형식을 잘못 입력한 경우",
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example",
                            description = "시간 형식을 잘못 입력한 경우 예시",
                            value = """
                                       {
                                           "code": 400,
                                           "message": "time 형식을 yyyy-MM-dd HH:mm:ss으로 작성해 주세요",
                                           "data": null
                                       }
                                    """
                    )))
    @ApiResponse_Internal_Server_Error
    @PostMapping()
    public ResponseEntity<SuccessApiResponse<PostResponse>> savePost(@RequestBody PostRequest postRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessApiResponse.success(SuccessCode.POST_CREATED_SUCCESS,
                        postService.save(postRequest)));
    }

    @Operation(summary = "게시글 수정", description = "게시글 수정 API")
    @ApiResponse(responseCode = "200", description = """
            정상 응답
                        
            data로 게시글 정보를 리턴하며, id는 게시글의 고유 번호이다.
            """,
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example",
                            description = "정상 응답 예시",
                            value = """
                                       {
                                          "code": 200,
                                          "message": "게시글 수정이 정상적으로 처리되었습니다",
                                          "data": {
                                              "id": 3,
                                              "type": "LION",
                                              "nickname": "testUser",
                                              "content": "안녕하세요3333",
                                              "time": "2023-09-18 00:11:44"
                                          }
                                        }
                                    """
                    )))
    @ApiResponse(responseCode = "400", description = "시간 형식을 잘못 입력한 경우",
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example",
                            description = "시간 형식을 잘못 입력한 경우 예시",
                            value = """
                                       {
                                           "code": 400,
                                           "message": "time 형식을 yyyy-MM-dd HH:mm:ss으로 작성해 주세요",
                                           "data": null
                                       }
                                    """
                    )))
    @ApiResponse(responseCode = "401", description = "게시글 수정 권한이 없는 경우",
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example",
                            description = "시간 형식을 잘못 입력한 경우 예시",
                            value = """
                                       {
                                            "code": 401,
                                            "message": "게시글 수정 혹은 삭제 권한이 존재하지 않습니다",
                                            "data": null
                                        }
                                    """
                    )))
    @ApiResponse_Internal_Server_Error
    @PutMapping("/{postId}")
    public ResponseEntity<SuccessApiResponse<PostResponse>> update(@PathVariable("postId") Long postId, @RequestBody PostRequest postRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessApiResponse.success(SuccessCode.POST_UPDATE_SUCCESS,
                        postService.update(postId, postRequest)));
    }

    @Operation(summary = "게시글 삭제", description = "게시글 삭제 API")
    @ApiResponse(responseCode = "200", description = """
            정상 응답
                        
            게시글이 정상적으로 삭제된 경우
            """,
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example",
                            description = "정상 응답 예시",
                            value = """
                                       {
                                           "code": 200,
                                           "message": "게시글 삭제가 정상적으로 처리되었습니다",
                                           "data": null
                                       }
                                    """
                    )))
    @ApiResponse(responseCode = "400", description = "시간 형식을 잘못 입력한 경우",
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example",
                            description = "시간 형식을 잘못 입력한 경우 예시",
                            value = """
                                       {
                                           "code": 400,
                                           "message": "time 형식을 yyyy-MM-dd HH:mm:ss으로 작성해 주세요",
                                           "data": null
                                       }
                                    """
                    )))
    @ApiResponse(responseCode = "401", description = "게시글 삭제 권한이 없는 경우",
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example",
                            description = "시간 형식을 잘못 입력한 경우 예시",
                            value = """
                                       {
                                            "code": 401,
                                            "message": "게시글 수정 혹은 삭제 권한이 존재하지 않습니다",
                                            "data": null
                                        }
                                    """
                    )))
    @ApiResponse_Internal_Server_Error
    @DeleteMapping("/{postId}")
    public ResponseEntity<SuccessApiResponse<?>> delete(@PathVariable("postId") Long postId, @RequestBody PostRequest postRequest) {
        int result = postService.delete(postId, postRequest);
        if (result == SUCCESS) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(SuccessApiResponse.success(SuccessCode.POST_DELETE_SUCCESS));
        } else {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

}
