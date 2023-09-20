package org.ai.roboadvisor.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.user.dto.request.SignInRequest;
import org.ai.roboadvisor.domain.user.dto.request.SignUpRequest;
import org.ai.roboadvisor.domain.user.dto.response.SignInResponse;
import org.ai.roboadvisor.domain.user.entity.Tendency;
import org.ai.roboadvisor.domain.user.entity.User;
import org.ai.roboadvisor.domain.user.repository.UserRepository;
import org.ai.roboadvisor.global.exception.CustomException;
import org.ai.roboadvisor.global.exception.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.ai.roboadvisor.global.exception.ErrorIntValue.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public int checkUserNickname(String nickname) {
        boolean checkNicknameInDb = checkNicknameIsDuplicated(nickname);
        if (checkNicknameInDb) {
            return NICKNAME_ALREADY_EXIST_IN_DB.getValue();
        }
        return SUCCESS.getValue();
    }

    public int signUp(SignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();
        String nickname = signUpRequest.getNickname();
        boolean checkEmailInDb = checkEmailIsDuplicated(email);
        boolean checkNicknameInDb = checkNicknameIsDuplicated(nickname);

        if (checkEmailInDb) {
            return EMAIL_ALREADY_EXIST_IN_DB.getValue();
        }
        if (checkNicknameInDb) {
            return NICKNAME_ALREADY_EXIST_IN_DB.getValue();
        }

        User user = SignUpRequest.toUserEntity(signUpRequest);
        try {
            userRepository.save(user);
        } catch (RuntimeException e) {
            log.error(">> Exception in save user entity: ", e);
            return INTERNAL_SERVER_ERROR.getValue();
        }
        return SUCCESS.getValue();
    }

    public SignInResponse signIn(SignInRequest signInRequest) {
        String email = signInRequest.getEmail();
        String password = signInRequest.getPassword();

        Optional<User> signInUser = userRepository.findUserByEmailAndPassword(email, password);
        if (signInUser.isEmpty()) {
            // 가입된 유저가 없다.
            throw new CustomException(ErrorCode.USER_NOT_EXISTED);
        } else {
            String userNickname = signInUser.get().getNickname();
            Tendency userTendency = signInUser.get().getTendency();
            return SignInResponse.of(userNickname, userTendency);
        }
    }

    public boolean checkEmailIsDuplicated(String email) {
        User user = userRepository.findUserByEmail(email).orElse(null);
        return user != null;
    }

    public boolean checkNicknameIsDuplicated(String nickname) {
        User user = userRepository.findUserByNickname(nickname).orElse(null);
        return user != null;
    }
}
