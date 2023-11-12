package org.ai.roboadvisor.domain.predict.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PriceRequest {

    @Schema(description = "댓글 작성자의 닉네임", example = "testUser")
    @NotBlank
    private String nickname;

    @Schema(description = "주식 종목 코드", example = "AAPL")
    @NotBlank
    private String ticker;
}
