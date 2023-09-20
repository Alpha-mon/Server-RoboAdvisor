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

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final int SUCCESS = 0;
    private final int EMAIL_ALREADY_EXIST_IN_DB = -1;
    private final int NICKNAME_ALREADY_EXIST_IN_DB = -2;
    private final int USER_NOT_EXISTED = -3;
    private final int INTERNAL_SERVER_ERROR = -100;


    public int checkUserNickname(String nickname) {
        boolean checkNicknameInDb = checkNicknameIsDuplicated(nickname);
        if (checkNicknameInDb) {
            return NICKNAME_ALREADY_EXIST_IN_DB;
        }
        return SUCCESS;
    }

    public int signUp(SignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();
        String nickname = signUpRequest.getNickname();
        boolean checkEmailInDb = checkEmailIsDuplicated(email);
        boolean checkNicknameInDb = checkNicknameIsDuplicated(nickname);

        if (checkEmailInDb) {
            return EMAIL_ALREADY_EXIST_IN_DB;
        }
        if (checkNicknameInDb) {
            return NICKNAME_ALREADY_EXIST_IN_DB;
        }

        User user = SignUpRequest.toUserEntity(signUpRequest);
        try {
            userRepository.save(user);
        } catch (RuntimeException e) {
            log.error(">> Exception in save user entity: ", e);
            return INTERNAL_SERVER_ERROR;
        }
        return SUCCESS;
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
