package org.ai.roboadvisor.domain.tendency.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.tendency.dto.TendencyUpdateDto;
import org.ai.roboadvisor.domain.tendency.entity.Tendency;
import org.ai.roboadvisor.domain.user.entity.User;
import org.ai.roboadvisor.domain.user.repository.UserRepository;
import org.ai.roboadvisor.global.exception.CustomException;
import org.ai.roboadvisor.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TendencyService {

    private final UserRepository userRepository;

    @Transactional
    public TendencyUpdateDto updateTendency(TendencyUpdateDto tendencyUpdateDto) {
        String userNickname = tendencyUpdateDto.getNickname();

        Tendency updateTendency = tendencyUpdateDto.getTendency();
        if (checkTendencyIsValid(updateTendency)) {
            throw new CustomException(ErrorCode.TENDENCY_INPUT_INVALID);
        }

        User user = userRepository.findUserByNickname(userNickname).orElse(null);
        if (user == null) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        user.setTendency(updateTendency);

        return TendencyUpdateDto.of(userNickname, updateTendency);
    }

    private boolean checkTendencyIsValid(Tendency tendency) {
        return tendency == Tendency.TYPE_NOT_EXISTS;
    }
}
