package org.ai.roboadvisor.domain.news.swagger_annotation;

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
        각 뉴스 기사 객체에는: title, link, dateTime, source가 존재하며, 설명은 다음과 같다.
                        
        title: 뉴스 기사의 제목, 
        link: 뉴스 기사 원본 링크, 
        dateTime: 뉴스 기사 작성 일자 및 시간, 
        source: 뉴스 기사 출처, 
                
        웹 크롤링을 해오는 사이트 원본 주소: https://finance.naver.com/item/news_news.nhn?code=161890&page=1&sm=title_entity_id.basic&clusterId=
                        
        위 주소를 참고하여, title과 link의 경우 <a> tag를 사용해서, title을 누르면 뉴스 기사로 이동하도록 작성해도 좋을 것 같아요.
                
        결과를 불러올때, 서버에서 dateTime이 최신 순으로 정렬해서 '10개'의 뉴스 기사 데이터를 전달합니다.  
        """,
        content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                examples = @ExampleObject(name = "example",
                        description = "정상 응답 예시",
                        value = """
                                 {
                                    "code": 200,
                                    "message": "뉴스 기사 크롤링에 성공하였습니다",
                                    "data": [
                                        {
                                            "title": "경동나비엔·포스코, 취약계층에 콘덴싱보일러 기부",
                                            "link": "https://finance.naver.com/item/news_read.naver?article_id=0004961075&office_id=008&code=005490&page=1&sm=title_entity_id.basic",
                                            "dateTime": "2023-11-13 17:26:00",
                                            "source": "머니투데이"
                                        },
                                        {
                                            "title": "인도네시아産 니켈도 IRA수준 혜택 추진…포스코·LG·LX '화색'",
                                            "link": "https://finance.naver.com/item/news_read.naver?article_id=0005214268&office_id=009&code=005490&page=1&sm=title_entity_id.basic",
                                            "dateTime": "2023-11-13 17:21:00",
                                            "source": "매일경제"
                                        },
                                        {
                                            "title": "창립 10주년 맞은 포스코1%나눔재단",
                                            "link": "https://finance.naver.com/item/news_read.naver?article_id=0004913476&office_id=015&code=005490&page=1&sm=title_entity_id.basic",
                                            "dateTime": "2023-11-13 16:15:00",
                                            "source": "한국경제"
                                        },
                                        {
                                            "title": "경동나비엔·포스코, 따뜻한 기부…\\"취약계층에 보일러를\\"",
                                            "link": "https://finance.naver.com/item/news_read.naver?article_id=0012206072&office_id=003&code=005490&page=1&sm=title_entity_id.basic",
                                            "dateTime": "2023-11-13 15:15:00",
                                            "source": "뉴시스"
                                        },
                                        {
                                            "title": "경동나비엔·포스코, 취약계층에 콘덴싱보일러 280여대 기증",
                                            "link": "https://finance.naver.com/item/news_read.naver?article_id=0004913397&office_id=015&code=005490&page=1&sm=title_entity_id.basic",
                                            "dateTime": "2023-11-13 14:29:00",
                                            "source": "한국경제"
                                        },
                                        {
                                            "title": "경동나비엔, 포스코와 취약계층에 콘덴싱보일러 기부",
                                            "link": "https://finance.naver.com/item/news_read.naver?article_id=0005213940&office_id=009&code=005490&page=1&sm=title_entity_id.basic",
                                            "dateTime": "2023-11-13 13:14:00",
                                            "source": "매일경제"
                                        },
                                        {
                                            "title": "\\"취약계층에 콘덴싱보일러 기부\\"…경동나비엔·포스코 '따뜻한 동행'",
                                            "link": "https://finance.naver.com/item/news_read.naver?article_id=0007170948&office_id=421&code=005490&page=1&sm=title_entity_id.basic",
                                            "dateTime": "2023-11-13 10:10:00",
                                            "source": "뉴스1"
                                        },
                                        {
                                            "title": "“숏커버링? 안 해도 이득”…에코프로·포스코 그룹 2차전지株 ‘공매도 ...",
                                            "link": "https://finance.naver.com/item/news_read.naver?article_id=0002223621&office_id=016&code=005490&page=1&sm=title_entity_id.basic",
                                            "dateTime": "2023-11-13 09:23:00",
                                            "source": "헤럴드경제"
                                        },
                                        {
                                            "title": "[수소경제, 기업이 뛴다]韓은 좁다…SK·포스코·롯데·고려아연 청정수소...",
                                            "link": "https://finance.naver.com/item/news_read.naver?article_id=0005339519&office_id=277&code=005490&page=1&sm=title_entity_id.basic",
                                            "dateTime": "2023-11-13 07:56:00",
                                            "source": "아시아경제"
                                        },
                                        {
                                            "title": "전기차 시대, 물 만난 포스코인터…광물부터 부품까지 접수",
                                            "link": "https://finance.naver.com/item/news_read.naver?article_id=0007170441&office_id=421&code=005490&page=1&sm=title_entity_id.basic",
                                            "dateTime": "2023-11-13 06:21:00",
                                            "source": "뉴스1"
                                        }
                                    ]
                                }
                                """
                )))
public @interface news_OK {
}

