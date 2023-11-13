package org.ai.roboadvisor.domain.news.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.news.dto.NewsArticle;
import org.ai.roboadvisor.domain.news.service.NewsCrawlerService;
import org.ai.roboadvisor.domain.news.swagger_annotation.news_BAD_REQUEST;
import org.ai.roboadvisor.domain.news.swagger_annotation.news_OK;
import org.ai.roboadvisor.domain.news.swagger_annotation.news_UNAUTHORIZED;
import org.ai.roboadvisor.global.common.dto.SuccessApiResponse;
import org.ai.roboadvisor.global.exception.SuccessCode;
import org.ai.roboadvisor.global.swagger_annotation.ApiResponse_Internal_Server_Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "news", description = "주식 뉴스 웹 크롤링 API")
@RestController
@RequestMapping("/api/news")
public class NewsCrawlerController {

    private final NewsCrawlerService newsCrawlerService;

    // 사용자의 추천 주식 종목 코드에 기반해서, 결과를 가져온다.
    // 추천 종목 주식 코드가 없는 경우, null 리턴
    @Operation(summary = "뉴스 기사 웹 크롤링 API", description = """
            해당 API는 사용자의 데이터베이스에 추천 주식 종목이 존재한다는 전제로 실행된다.
                        
            즉, 사용자가 투자 성향 테스트를 실시한 이후에만 뉴스 기사 웹 크롤링 기능을 사용이 가능하다
            (사용자가 투자 성향 테스트를 실시한다 -> 투자 성향 테스트 이후 서버에서 Flask 서버로 요청을 보내서 추천 알고리즘을 통해 추천 
            주식 종목 3개를 사용자의 DB에 저장한다 -> 뉴스 기사 웹 크롤링 API를 통해 추천 주식 종목에 해당하는 뉴스 기사를 가져온다)

            웹 크롤링을 해오는 사이트 원본 주소: https://finance.naver.com/item/news_news.nhn?code=161890&page=1&sm=title_entity_id.basic&clusterId=
                        
            요청 포맷] GET: [요청 url]:8080/api/news?nickname=사용자의 닉네임, e.g) [요청 url]:8080/api/news?nickname=testUser 
            """)
    @GetMapping
    @news_OK
    @news_BAD_REQUEST
    @news_UNAUTHORIZED
    @ApiResponse_Internal_Server_Error
    public ResponseEntity<SuccessApiResponse<List<NewsArticle>>> getNewsFromRecommendedStocks(@RequestParam(value = "nickname", required = true) String nickname) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessApiResponse.success(SuccessCode.NEWS_DATA_GET_SUCCESS,
                        newsCrawlerService.getNewsFromRecommendedStocks(nickname)));
    }
}
