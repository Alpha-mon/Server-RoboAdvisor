package org.ai.roboadvisor.domain.tendency.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ai.roboadvisor.domain.tendency.entity.Tendency;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TendencyFlaskResponse {

    @JsonProperty("recommended_stocks")
    private List<String> recommended_stocks;

    @JsonProperty("tendency")
    private Tendency tendency;
}
