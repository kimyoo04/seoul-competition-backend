package com.seoul_competition.senior_jobtraining.domain.comment.api;

import com.seoul_competition.senior_jobtraining.domain.comment.application.CommentService;
import com.seoul_competition.senior_jobtraining.domain.comment.dto.request.CommentSaveReqDto;
import com.seoul_competition.senior_jobtraining.domain.comment.dto.request.CommentUpdateReqDto;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {

  private final CommentService commentService;

  @PostMapping
  public ResponseEntity<Void> createComment(
      @RequestBody @Valid CommentSaveReqDto reqDto
  ) {
    commentService.create(reqDto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .build();
  }

  @PutMapping("/{commentId}")
  public ResponseEntity<Void> updateComment(
      @RequestBody @Valid CommentUpdateReqDto reqDto,
      @PathVariable Long commentId
  ) {
    commentService.update(commentId, reqDto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .build();
  }

  @DeleteMapping("/{commentId}")
  public ResponseEntity<Void> deleteComment(@PathVariable Long commentId,
      @RequestBody Map<String, String> password) {
    commentService.delete(commentId, password.get("password"));
    return ResponseEntity.noContent().build();
  }
}