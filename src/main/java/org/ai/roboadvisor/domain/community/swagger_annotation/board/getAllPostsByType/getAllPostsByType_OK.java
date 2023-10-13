package org.ai.roboadvisor.domain.community.swagger_annotation.board.getAllPostsByType;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.ai.roboadvisor.global.common.dto.SuccessApiResponse;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ApiResponse(responseCode = "200", description = """
        게시글 전체 불러오기
                
        Pagination을 사용하여, page=0, 1, 2, ... 숫자를 증가시킴에 따라 연속해서 게시글을 가져올 수 있다.
                
        page 0, 1, 2, ... 마다 각각 10개의 게시글을 가져오도록 구현되어 있으며, 이는 수정이 가능하다. 
                
        요청 예시는 다음과 같다: [api url]/api/community/board?tendency=LION&page=0
                
        data Array 내에서 각 인덱스가 하나의 게시글 정보이다.
                
        id 값이 클 수록 나중에 작성된 게시글이다.
                
        id: 게시글 id, tendency: 투자 성향, nickname: 작성자 닉네임, content: 게시글 내용,
                
        createdDateTime: 게시글 작성 시간, viewCount: 게시글 조회수, commentCount: 게시글 내의 댓글 개수
        """,
        content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                examples =
                @ExampleObject(name = "example",
                        description = "정상 응답",
                        value = """
                                   {
                                       "code": 200,
                                       "message": "게시글을 불러오는데 성공하셨습니다",
                                       "data": [
                                           {
                                               "id": 1,
                                               "tendency": "LION",
                                               "nickname": "testUser",
                                               "content": "안녕하세요3333",
                                               "createdDateTime": "2023-09-21 01:06:20",
                                               "viewCount": 9,
                                               "commentCount": 2
                                           },
                                           {
                                               "id": 3,
                                               "tendency": "LION",
                                               "nickname": "testUser",
                                               "content": "안녕하세요",
                                               "createdDateTime": "2023-09-23 14:44:14",
                                               "viewCount": 0,
                                               "commentCount": 0
                                           },
                                           {
                                               "id": 4,
                                               "tendency": "LION",
                                               "nickname": "testUser",
                                               "content": "안녕하세요",
                                               "createdDateTime": "2023-09-23 14:44:15",
                                               "viewCount": 0,
                                               "commentCount": 0
                                           },
                                           {
                                               "id": 5,
                                               "tendency": "LION",
                                               "nickname": "testUser",
                                               "content": "안녕하세요",
                                               "createdDateTime": "2023-09-23 14:44:15",
                                               "viewCount": 0,
                                               "commentCount": 0
                                           },
                                           {
                                               "id": 6,
                                               "tendency": "LION",
                                               "nickname": "testUser",
                                               "content": "안녕하세요",
                                               "createdDateTime": "2023-09-23 14:44:16",
                                               "viewCount": 0,
                                               "commentCount": 0
                                           },
                                           {
                                               "id": 7,
                                               "tendency": "LION",
                                               "nickname": "testUser",
                                               "content": "안녕하세요",
                                               "createdDateTime": "2023-09-23 14:44:16",
                                               "viewCount": 0,
                                               "commentCount": 0
                                           },
                                           {
                                               "id": 8,
                                               "tendency": "LION",
                                               "nickname": "testUser",
                                               "content": "안녕하세요",
                                               "createdDateTime": "2023-09-23 14:44:17",
                                               "viewCount": 0,
                                               "commentCount": 0
                                           },
                                           {
                                               "id": 9,
                                               "tendency": "LION",
                                               "nickname": "testUser",
                                               "content": "안녕하세요",
                                               "createdDateTime": "2023-09-23 14:44:18",
                                               "viewCount": 0,
                                               "commentCount": 0
                                           }
                                       ]
                                   }
                                """
                )
        ))
public @interface getAllPostsByType_OK {
}

