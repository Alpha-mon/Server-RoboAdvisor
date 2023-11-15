package org.ai.roboadvisor.domain.tendency.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ai.roboadvisor.domain.tendency.entity.Tendency;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TendencyUpdateResponse {

    private String nickname;
    private Tendency tendency;
    private List<String> recommendedStocks = new ArrayList<>();


    public static TendencyUpdateResponse of(String nickname, Tendency tendency, List<String> recommendedStocks) {
        return new TendencyUpdateResponse(nickname, tendency, recommendedStocks);
    }
}
