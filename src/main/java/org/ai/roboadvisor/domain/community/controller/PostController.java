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

import static org.ai.roboadvisor.global.exception.ErrorIntValue.*;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "community] post", description = "게시글 작성, 수정, 삭제 API")
@RestController
@RequestMapping("/api/community/post")
public class PostController {
    private final PostService postService;

    @Operation(summary = "게시글 작성", description = "게시글 작성 API")
    @ApiResponse(responseCode = "201", description = """
            정상 응답. data로 게시글 정보를 리턴한다.
                        
            id: 게시글 고유 번호(식별 번호), tendency: 투자 성향, nickname: 게시글 작성자 닉네임,
                        
            content: 게시글 작성 내용, time: 게시글 작성 시간, viewcount: 조회수
            """,
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example",
                            description = "정상 응답 예시",
                            value = """
                                       {
                                           "code": 201,
                                           "message": "게시글이 정상적으로 등록되었습니다",
                                           "data": {
                                               "id": 1,
                                               "tendency": "SHEEP",
                                               "nickname": "testUser",
                                               "content": "안녕하세요",
                                               "time": "2023-09-21 01:06:19",
                                               "viewCount": 0
                                           }
                                       }
                                    """
                    )))
    @ApiResponse(responseCode = "400", description = "투자 성향이 잘못 입력된 경우",
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example",
                            description = "투자 성향이 잘못 입력된 경우 예시",
                            value = """
                                       {
                                           "code": 400,
                                           "message": "잘못된 투자 성향 형식이 입력되었습니다",
                                           "data": null
                                       }
                                    """
                    )))
    @ApiResponse_Internal_Server_Error
    @PostMapping()
    public ResponseEntity<SuccessApiResponse<PostResponse>> save(@RequestBody PostRequest postRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessApiResponse.success(SuccessCode.POST_CREATED_SUCCESS,
                        postService.save(postRequest)));
    }

    @Operation(summary = "게시글 수정", description = "게시글 수정 API")
    @ApiResponse(responseCode = "200", description = """
            정상 응답
                        
            게시글이 정상적으로 수정된 경우: 응답 객체는 '게시글 작성' 과 동일하다.
            """,
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example",
                            description = "정상 응답 예시",
                            value = """
                                      {
                                           "code": 200,
                                           "message": "게시글 수정이 정상적으로 처리되었습니다",
                                           "data": {
                                               "id": 1,
                                               "tendency": "LION",
                                               "nickname": "testUser",
                                               "content": "안녕하세요3333",
                                               "time": "2023-09-21 01:06:20",
                                               "viewCount": 0
                                           }
                                       }
                                    """
                    )))
    @ApiResponse(responseCode = "400", description = "투자 성향이 잘못 입력된 경우",
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example",
                            description = "투자 성향이 잘못 입력된 경우 예시",
                            value = """
                                       {
                                           "code": 400,
                                           "message": "잘못된 투자 성향 형식이 입력되었습니다",
                                           "data": null
                                       }
                                    """
                    )))
    @ApiResponse(responseCode = "401", description = "게시글 수정 권한이 없는 경우",
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example",
                            description = "게시글 수정 권한이 없는 경우 예시",
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
    @ApiResponse(responseCode = "401", description = "게시글 삭제 권한이 없는 경우",
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example",
                            description = "게시글 삭제 권한이 없는 예시",
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
        if (result == SUCCESS.getValue()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(SuccessApiResponse.success(SuccessCode.POST_DELETE_SUCCESS));
        } else {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

}
