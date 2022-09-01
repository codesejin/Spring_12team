package com.example.intermediate.controller.request;

import lombok.*;

@Setter
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubCommentRequestDto {
    private Long commentId;
    private String subComment;
}