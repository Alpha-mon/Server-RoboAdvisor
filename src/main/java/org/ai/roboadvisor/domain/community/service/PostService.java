package org.ai.roboadvisor.domain.community.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.community.dto.request.PostRequest;
import org.ai.roboadvisor.domain.community.dto.response.PostResponse;
import org.ai.roboadvisor.domain.community.entity.Post;
import org.ai.roboadvisor.domain.community.repository.PostRepository;
import org.ai.roboadvisor.global.exception.CustomException;
import org.ai.roboadvisor.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    private final int SUCCESS = 0;
    private final int TIME_INPUT_INVALID = -1;
    private final int INTERNAL_SERVER_ERROR = -100;

    @Transactional
    public PostResponse save(PostRequest postRequest) {
        LocalDateTime parsedTime = validateAndParseTime(postRequest.getTime());
        Post newPost = PostRequest.fromPostRequest(postRequest, parsedTime);

        // save
        savePost(newPost);

        return PostResponse.of(newPost.getId(), newPost.getType(), newPost.getNickname(),
                newPost.getContent(), newPost.getTime());
    }

    @Transactional
    public PostResponse update(Long postId, PostRequest postRequest) {
        Post existingPost = findExistingPostById(postId);
        LocalDateTime parsedTime = validateAndParseTime(postRequest.getTime());

        // validate if user has authority
        validateUserHasAuthority(postRequest, existingPost);

        // update
        updatePostEntity(existingPost, postRequest, parsedTime);

        return PostResponse.fromPostEntity(existingPost);
    }

    @Transactional
    public int delete(Long postId, PostRequest postRequest) {
        Post existingPost = findExistingPostById(postId);

        // validate if user has authority
        validateUserHasAuthority(postRequest, existingPost);

        try {
            postRepository.delete(existingPost);
            return SUCCESS;
        } catch (RuntimeException e) {
            log.error("e : ", e);
            return INTERNAL_SERVER_ERROR;
        }
    }

    private LocalDateTime validateAndParseTime(String time) {
        Optional<LocalDateTime> dateTimeOptional = parseStringToLocalDateTime(time);
        return dateTimeOptional.orElseThrow(() -> new CustomException(ErrorCode.TIME_INPUT_INVALID));
    }

    private Optional<LocalDateTime> parseStringToLocalDateTime(String timeString) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return Optional.of(LocalDateTime.parse(timeString, formatter));
        } catch (DateTimeParseException e) {
            log.error(">> Failed to parse date-time string.", e);
            return Optional.empty();
        }
    }

    private void savePost(Post post) {
        try {
            postRepository.save(post);
        } catch (Exception e) {
            log.error("Save error: ", e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private Post findExistingPostById(Long postId) {
        return postRepository.findPostById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    private void validateUserHasAuthority(PostRequest postRequest, Post existingPost) {
        if (!postRequest.getNickname().equals(existingPost.getNickname())) {
            throw new CustomException(ErrorCode.USER_HAS_NOT_AUTHORIZED);
        }
    }

    private void updatePostEntity(Post existingPost, PostRequest postRequest, LocalDateTime parsedTime) {
        existingPost.setType(postRequest.getType());
        existingPost.setContent(postRequest.getContent());
        existingPost.setTime(parsedTime);
    }
}
