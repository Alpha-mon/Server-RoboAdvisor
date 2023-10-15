package org.ai.roboadvisor.domain.community.swagger_annotation.post.getPostById;

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
        특정 게시글을 조회하는 경우
                
        id: 게시글 고유 번호(식별 번호), tendency: 투자 성향, nickname: 게시글 작성자 닉네임,
                    
        content: 게시글 작성 내용, createdDateTime: 게시글 작성 시간, viewcount: 조회수
                
        comments : 댓글 Array
                
        commentId: 댓글 id, parentCommentId: 부모 댓글 id, 값이 null인 경우 자신이 최상위 댓글이다.
         null이 아니라면 대댓글이며, parentCommentId를 통해 부모 댓글 id를 확인할 수 있다.
                
        """,
        content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                examples = @ExampleObject(name = "example",
                        description = "정상 응답 예시",
                        value = """
                                 {
                                          "code": 200,
                                          "message": "게시글을 조회하는데 성공하셨습니다",
                                          "data": {
                                              "id": 1,
                                              "tendency": "LION",
                                              "nickname": "testUser",
                                              "content": "안녕하세요1",
                                              "createdDateTime": "2023-09-21 01:06:20",
                                              "viewCount": 50,
                                              "comments": [
                                                  {
                                                      "commentId": 1,
                                                      "parentCommentId": null,
                                                      "nickname": "testUser",
                                                      "content": "안녕하세요 댓글 1",
                                                      "createdDateTime": "2023-09-25 04:28:41"
                                                  },
                                                  {
                                                      "commentId": 2,
                                                      "parentCommentId": null,
                                                      "nickname": "testUser",
                                                      "content": "안녕하세요 댓글 2",
                                                      "createdDateTime": "2023-09-25 04:28:55"
                                                  },
                                                  {
                                                      "commentId": 14,
                                                      "parentCommentId": 1,
                                                      "nickname": "testUser",
                                                      "content": "test reply comment",
                                                      "createdDateTime": "2023-10-14 20:07:55"
                                                  },
                                                  {
                                                      "commentId": 15,
                                                      "parentCommentId": 1,
                                                      "nickname": "testUser",
                                                      "content": "fast update 13333",
                                                      "createdDateTime": "2023-10-14 20:09:47"
                                                  }
                                              ]
                                          }
                                      }
                                """
                )))
public @interface getPostById_OK {
}

