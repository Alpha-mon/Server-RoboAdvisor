package org.ai.roboadvisor.domain.tendency.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.tendency.dto.TendencyDto;
import org.ai.roboadvisor.domain.tendency.dto.TendencyUpdateDto;
import org.ai.roboadvisor.domain.tendency.dto.response.TendencyUpdateResponse;
import org.ai.roboadvisor.domain.tendency.entity.StockKr;
import org.ai.roboadvisor.domain.tendency.entity.Tendency;
import org.ai.roboadvisor.domain.tendency.repository.StockKrRepository;
import org.ai.roboadvisor.domain.user.entity.User;
import org.ai.roboadvisor.domain.user.repository.UserRepository;
import org.ai.roboadvisor.global.exception.CustomException;
import org.ai.roboadvisor.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TendencyService {

    private final UserRepository userRepository;
    private final FlaskRecommendClient flaskRecommendClient;
    private final StockKrRepository stockKrRepository;

    @Transactional
    public TendencyUpdateResponse updateTendency(TendencyUpdateDto tendencyUpdateDto) {
        String userNickname = tendencyUpdateDto.getNickname();
        User user = userRepository.findUserByNickname(userNickname)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTED));

        Tendency updateTendency = tendencyUpdateDto.getTendency();
        checkTendencyIsValid(updateTendency);

        updateTendencyOfUser(user, updateTendency);

        // Flask 서버에서 추천 주식 종목 3개를 받아온다.
        List<String> getRecommendStocks = flaskRecommendClient
                .getStockData(new TendencyDto(updateTendency)).getRecommended_stocks();

        // 받아온 추천 종목 3개를, 주식 코드로 변환한다.
        StringBuilder updateStocks = new StringBuilder();
        for (int i = 0; i < getRecommendStocks.size(); i++) {
            StockKr stockKr = stockKrRepository.findByStockName(getRecommendStocks.get(i))
                    .orElse(null);
            if (stockKr != null && stockKr.getStockCode() != null) {
                if (i < getRecommendStocks.size() - 1) {
                    updateStocks.append(stockKr.getStockCode()).append(",");
                } else {
                    updateStocks.append(stockKr.getStockCode());
                }
            }
        }
        updateRecommendStocksOfUser(user, updateStocks.toString());

        return TendencyUpdateResponse.of(userNickname, updateTendency, getRecommendStocks);
    }

    private void checkTendencyIsValid(Tendency tendency) {
        if (tendency == Tendency.TYPE_NOT_EXISTS) {
            throw new CustomException(ErrorCode.TENDENCY_INPUT_INVALID);
        }
    }

    private void updateTendencyOfUser(User user, Tendency tendency) {
        user.setTendency(tendency);
    }

    private void updateRecommendStocksOfUser(User user, String stocks) {
        user.setRecommendStocks(stocks);
    }
}
