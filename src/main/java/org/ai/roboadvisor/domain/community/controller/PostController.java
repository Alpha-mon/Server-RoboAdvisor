package org.ai.roboadvisor.domain.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.community.dto.request.PostDeleteRequest;
import org.ai.roboadvisor.domain.community.dto.request.PostRequest;
import org.ai.roboadvisor.domain.community.dto.response.PostDeleteResponse;
import org.ai.roboadvisor.domain.community.dto.response.PostResponse;
import org.ai.roboadvisor.domain.community.service.PostService;
import org.ai.roboadvisor.domain.community.swagger_annotation.post.delete.delete_OK;
import org.ai.roboadvisor.domain.community.swagger_annotation.post.delete.delete_UNAUTHORIZED;
import org.ai.roboadvisor.domain.community.swagger_annotation.post.getPostById.getPostById_BAD_REQUEST;
import org.ai.roboadvisor.domain.community.swagger_annotation.post.getPostById.getPostById_OK;
import org.ai.roboadvisor.domain.community.swagger_annotation.post.save.save_BAD_REQUEST;
import org.ai.roboadvisor.domain.community.swagger_annotation.post.save.save_CREATED;
import org.ai.roboadvisor.domain.community.swagger_annotation.post.update.update_BAD_REQUEST;
import org.ai.roboadvisor.domain.community.swagger_annotation.post.update.update_OK;
import org.ai.roboadvisor.domain.community.swagger_annotation.post.update.update_UNAUTHORIZED;
import org.ai.roboadvisor.global.common.dto.SuccessApiResponse;
import org.ai.roboadvisor.global.exception.SuccessCode;
import org.ai.roboadvisor.global.swagger_annotation.ApiResponse_Internal_Server_Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "community] post API", description = "게시글 CRUD API")
@RestController
@RequestMapping("/api/community/post")
public class PostController {
    private final PostService postService;

    @Operation(summary = "게시글 조회", description = "게시글 조회 API")
    @getPostById_OK
    @getPostById_BAD_REQUEST
    @ApiResponse_Internal_Server_Error
    @GetMapping("/{postId}")
    public ResponseEntity<SuccessApiResponse<PostResponse>> getPostById(@PathVariable("postId") Long postId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessApiResponse.success(SuccessCode.POST_VIEW_SUCCESS,
                        postService.getPostById(postId)));
    }

    @Operation(summary = "게시글 작성", description = "게시글 작성 API")
    @save_CREATED
    @save_BAD_REQUEST
    @ApiResponse_Internal_Server_Error
    @PostMapping()
    public ResponseEntity<SuccessApiResponse<PostResponse>> save(@RequestBody PostRequest postRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessApiResponse.success(SuccessCode.POST_CREATED_SUCCESS,
                        postService.save(postRequest)));
    }

    @Operation(summary = "게시글 수정", description = "게시글 수정 API")
    @update_OK
    @update_BAD_REQUEST
    @update_UNAUTHORIZED
    @ApiResponse_Internal_Server_Error
    @PatchMapping("/{postId}")
    public ResponseEntity<SuccessApiResponse<PostResponse>> update(@PathVariable("postId") Long postId, @RequestBody PostRequest postRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessApiResponse.success(SuccessCode.POST_UPDATE_SUCCESS,
                        postService.update(postId, postRequest)));
    }

    @Operation(summary = "게시글 삭제", description = "게시글 삭제 API")
    @delete_OK
    @delete_UNAUTHORIZED
    @ApiResponse_Internal_Server_Error
    @DeleteMapping("/{postId}")
    public ResponseEntity<SuccessApiResponse<PostDeleteResponse>> delete(@PathVariable("postId") Long postId,
                                                                         @RequestBody PostDeleteRequest postDeleteRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessApiResponse.success(SuccessCode.POST_DELETE_SUCCESS,
                        postService.delete(postId, postDeleteRequest)));
    }

}
