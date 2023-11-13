package org.ai.roboadvisor.domain.news.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.news.dto.NewsArticle;
import org.ai.roboadvisor.domain.user.entity.User;
import org.ai.roboadvisor.domain.user.repository.UserRepository;
import org.ai.roboadvisor.global.exception.CustomException;
import org.ai.roboadvisor.global.exception.ErrorCode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class NewsCrawlerService {

    private final UserRepository userRepository;

//    private static final String NAVER_FINANCE_NEWS_URL = "https://finance.naver.com/item/news_news.nhn?";
//    private static String NAVER_FINANCE_NEWS_URL = "https://finance.naver.com/item/news_news.nhn?code=161890&page=1&sm=title_entity_id.basic&clusterId=";

    public List<NewsArticle> getNewsFromRecommendedStocks(String nickname) {
        // 1. 데이터베이스를 조회해서, 사용자의 추천 종목 코드를 가져온다.
        User user = userRepository.findUserByNickname(nickname)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTED));
        String recommendedStocks = user.getRecommendStocks();
        if (recommendedStocks == null) {
            throw new CustomException(ErrorCode.RECOMMENDED_STOCKS_NOT_EXISTED);
        }
        String[] stocks = recommendedStocks.split(",");

        // 2. 뉴스 기사 크롤링
        List<NewsArticle> articles = new ArrayList<>();
        // Use Jsoup to crawl news articles and populate the articles list
        try {
            for (String s : stocks) {
                String NAVER_FINANCE_NEWS_URL = "https://finance.naver.com/item/news_news.nhn?code=" + s +
                        "&page=1&sm=title_entity_id.basic&clusterId=";
                Document doc = Jsoup.connect(NAVER_FINANCE_NEWS_URL).get();

                // This selector might change depending on the actual structure of the BBC website
                // You might need to inspect the website and update the selector accordingly.

                // Get title of news
                Elements titles = doc.select(".title");
                List<String> titleResult = new ArrayList<>();

                titles.forEach(titleElement -> {
                    String titleText = titleElement.text();
                    titleText = titleText.replaceAll("\n", "");
                    titleResult.add(titleText);
                });
                System.out.println("titleResult.get(0) = " + titleResult.get(0));

                // Extracting news links
                Elements links = doc.select(".title");
                List<String> linkResult = new ArrayList<>();
                for (Element link : links) {
                    String href = link.select("a").first().attr("href");
                    linkResult.add("https://finance.naver.com" + href);
                }
                System.out.println("linkResult.get(0) = " + linkResult.get(0));

                // Extracting news dates
                Elements dates = doc.select(".date");
                List<String> dateResult = new ArrayList<>();
                for (Element date : dates) {
                    dateResult.add(date.text());
                }
                System.out.println("dateResult.get(0) = " + dateResult.get(0));

                // Extracting news sources
                Elements sources = doc.select(".info");
                List<String> sourceResult = new ArrayList<>();
                for (Element source : sources) {
                    sourceResult.add(source.text());
                }
                System.out.println("sourceResult.get(0) = " + sourceResult.get(0));

                int size = getSmallestListSize(titleResult, linkResult, dateResult, sourceResult);
                for (int i = 0; i < size; i++) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
                    LocalDateTime dateTime = LocalDateTime.parse(dateResult.get(i), formatter);

                    articles.add(new NewsArticle(titleResult.get(i), linkResult.get(i), dateTime,
                            sourceResult.get(i)));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        // dateTime에 따라 내림차순으로 정렬
        Collections.sort(articles, new Comparator<NewsArticle>() {
            @Override
            public int compare(NewsArticle a1, NewsArticle a2) {
                return a2.getDateTime().compareTo(a1.getDateTime());
            }
        });

        // 상위 10개의 기사만 가져옴
        List<NewsArticle> top10Articles = articles.subList(0, Math.min(10, articles.size()));

        // 결과 출력 (이 부분은 필요에 따라 조정)
//        for (NewsArticle article : top10Articles) {
//            System.out.println(article.getTitle() + " - " + article.getDateTime());
//        }
        return top10Articles;
    }

    public static int getSmallestListSize(List<?>... lists) {
        return Arrays.stream(lists)
                .mapToInt(List::size)
                .min()
                .orElse(0); // Or some other value indicating empty lists or error
    }

}
