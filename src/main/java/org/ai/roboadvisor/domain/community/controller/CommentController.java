package org.ai.roboadvisor.domain.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.community.dto.request.CommentDeleteRequest;
import org.ai.roboadvisor.domain.community.dto.request.CommentRequest;
import org.ai.roboadvisor.domain.community.dto.request.CommentUpdateRequest;
import org.ai.roboadvisor.domain.community.dto.response.CommentResponse;
import org.ai.roboadvisor.domain.community.service.CommentService;
import org.ai.roboadvisor.domain.community.swagger_annotation.comment.delete.*;
import org.ai.roboadvisor.domain.community.swagger_annotation.comment.save.*;
import org.ai.roboadvisor.domain.community.swagger_annotation.comment.update.*;
import org.ai.roboadvisor.global.common.dto.SuccessApiResponse;
import org.ai.roboadvisor.global.exception.SuccessCode;
import org.ai.roboadvisor.global.swagger_annotation.ApiResponse_Internal_Server_Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "community] comment", description = "댓글 작성, 수정, 삭제 API")
@RestController
@RequestMapping("/api/community/comment")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 작성", description = "댓글 작성 API")
    @save_CREATED
    @save_BAD_REQUEST
    @save_UNAUTHORIZED
    @ApiResponse_Internal_Server_Error
    @PostMapping("/{postId}")
    public ResponseEntity<SuccessApiResponse<CommentResponse>> save(@PathVariable("postId") Long postId,
                                                                    @RequestBody CommentRequest commentRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessApiResponse.success(SuccessCode.COMMENT_CREATED_SUCCESS,
                        commentService.save(postId, commentRequest)));
    }

    @Operation(summary = "댓글 수정", description = "댓글 수정 API")
    @update_OK
    @update_BAD_REQUEST
    @update_UNAUTHORIZED
    @ApiResponse_Internal_Server_Error
    @PutMapping("/{postId}")
    public ResponseEntity<SuccessApiResponse<CommentResponse>> update(@PathVariable("postId") Long postId,
                                                                      @RequestBody CommentUpdateRequest commentUpdateRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessApiResponse.success(SuccessCode.COMMENT_UPDATE_SUCCESS,
                        commentService.update(postId, commentUpdateRequest)));
    }

    @Operation(summary = "댓글 삭제", description = "댓글 삭제 API")
    @delete_OK
    @delete_BAD_REQUEST
    @delete_UNAUTHORIZED
    @ApiResponse_Internal_Server_Error
    @DeleteMapping("/{postId}")
    public ResponseEntity<SuccessApiResponse<CommentResponse>> delete(@PathVariable("postId") Long postId,
                                                                      @RequestBody CommentDeleteRequest commentDeleteRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessApiResponse.success(SuccessCode.COMMENT_DELETE_SUCCESS,
                        commentService.delete(postId, commentDeleteRequest)));
    }
}