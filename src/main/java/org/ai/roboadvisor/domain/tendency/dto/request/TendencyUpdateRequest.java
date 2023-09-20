package org.ai.roboadvisor.domain.tendency.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ai.roboadvisor.domain.tendency.entity.Tendency;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TendencyUpdateRequest {

    @Schema(description = "사용자 정보: 닉네임", example = "testUser")
    @NotBlank
    private String nickname;

    @Schema(description = "투자 성향", example = "LION")
    @NotBlank
    private Tendency tendency;
}
