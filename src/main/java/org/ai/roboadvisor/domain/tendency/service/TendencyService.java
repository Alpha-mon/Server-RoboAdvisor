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
        User user = userRepository.findUserByNickname(userNickname)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTED));

        Tendency updateTendency = tendencyUpdateDto.getTendency();
        checkTendencyIsValid(updateTendency);

        updateTendencyOfUser(user, updateTendency);
        return TendencyUpdateDto.of(userNickname, updateTendency);
    }

    private void checkTendencyIsValid(Tendency tendency) {
        if (tendency == Tendency.TYPE_NOT_EXISTS) {
            throw new CustomException(ErrorCode.TENDENCY_INPUT_INVALID);
        }
    }

    private void updateTendencyOfUser(User user, Tendency tendency) {
        user.setTendency(tendency);
    }
}
