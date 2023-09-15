package org.ai.roboadvisor.domain.tendency.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.tendency.dto.request.TendencyUpdateRequest;
import org.ai.roboadvisor.domain.user.entity.Tendency;
import org.ai.roboadvisor.domain.user.entity.User;
import org.ai.roboadvisor.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TendencyService {

    private final UserRepository userRepository;
    private final int SUCCESS = 0;
    private final int INVALID_TENDENCY_ERROR = -1;
    private final int INTERNAL_SERVER_ERROR = -100;

    @Transactional
    public int updateTendency(TendencyUpdateRequest tendencyUpdateRequest) {
        String userNickname = tendencyUpdateRequest.getNickname();

        Tendency tendency = tendencyUpdateRequest.getTendency();
        if (checkTendencyIsValid(tendency)) {
            return INVALID_TENDENCY_ERROR;
        }

        User user = userRepository.findUserByNickname(userNickname).orElse(null);
        if (user == null) {
            return INTERNAL_SERVER_ERROR;
        }
        user.setTendency(tendency);
        return SUCCESS;
    }

    private boolean checkTendencyIsValid(Tendency tendency) {
        return tendency == Tendency.TYPE_NOT_EXISTS;
    }
}
