package com.example.intermediate.controller.response;

import java.time.LocalDateTime;
import java.util.List;

import com.example.intermediate.controller.request.PostRequestDto;
import com.example.intermediate.domain.Comment;
import com.example.intermediate.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
  private Long id;
  private String title;
  private String content;
  private String author;
  private String imgUrl;
  private List<CommentResponseDto> commentResponseDtoList;
  private int cntLikes;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private int commentcnt;

}
