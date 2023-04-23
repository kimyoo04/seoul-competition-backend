package com.seoul_competition.senior_jobtraining.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PostUpdateReqDto(
    @NotBlank(message = "비밀번호를 입력해주세요.") String password,
    @NotBlank(message = "게시글 제목을 입력해주세요.") String title,
    @NotBlank(message = "게시글 본문을 입력해주세요.") String content) {

}
