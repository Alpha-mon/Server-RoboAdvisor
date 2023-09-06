package org.ai.roboadvisor.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.user.dto.request.SignInRequest;
import org.ai.roboadvisor.domain.user.dto.request.SignUpRequest;
import org.ai.roboadvisor.domain.user.entity.User;
import org.ai.roboadvisor.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public int signUp(SignUpRequest signUpRequest) {
        int SIGNUP_SUCCESS = 0;
        int INTERNAL_SERVER_ERROR = 1;
        int EMAIL_ALREADY_EXIST_IN_DB = 2;
        int NICKNAME_ALREADY_EXIST_IN_DB = 3;

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
        return SIGNUP_SUCCESS;
    }

    public int signIn(SignInRequest signInRequest) {
        int LOGIN_SUCCESS = 0;
        int USER_NOT_EXISTED = 1;

        String email = signInRequest.getEmail();
        String password = signInRequest.getPassword();

        Optional<User> signInUser = userRepository.findUserByEmailAndPassword(email, password);
        if (signInUser.isEmpty()) {
            // 가입된 유저가 없다.
            return USER_NOT_EXISTED;
        } else {
            return LOGIN_SUCCESS;
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
