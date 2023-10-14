package org.ai.roboadvisor.domain.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.community.dto.response.BoardResponse;
import org.ai.roboadvisor.domain.community.service.BoardService;
import org.ai.roboadvisor.domain.community.swagger_annotation.board.getAllPostsByType.*;
import org.ai.roboadvisor.domain.tendency.entity.Tendency;
import org.ai.roboadvisor.global.common.dto.SuccessApiResponse;
import org.ai.roboadvisor.global.exception.SuccessCode;
import org.ai.roboadvisor.global.swagger_annotation.ApiResponse_Internal_Server_Error;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "community] board API", description = "게시글 목록 불러오기 API")
@RestController
@RequestMapping("/api/community/board")
public class BoardController {

    private final BoardService boardService;

    private final int PAGE_SIZE = 10;

    @Operation(summary = "게시글 전체 조회", description = """
            투자 성향별로 게시글 목록을 10개씩 불러오는 API
             
            요청 예시는 다음과 같다: [api url]/api/community/board?tendency=LION&page=0
            """)
    @getAllPostsByType_OK
    @getAllPostsByType_BAD_REQUEST
    @ApiResponse_Internal_Server_Error
    @GetMapping()
    public ResponseEntity<SuccessApiResponse<List<BoardResponse>>> getAllPostsByType(
            @RequestParam("tendency") Tendency tendency, @PageableDefault(size = PAGE_SIZE, sort = {"id"},
            direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessApiResponse.success(SuccessCode.BOARD_ALL_VIEW_SUCCESS,
                        boardService.getAllPostsByTendency(tendency, pageable)));
    }

}
