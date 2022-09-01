package com.example.intermediate.controller;

import com.example.intermediate.controller.request.LikeRequestDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class LikeController {

    private final LikeService likeService;

    @RequestMapping(value = "/api/auth/post/like", method = RequestMethod.POST)
    public ResponseDto<?> doLike(@RequestBody LikeRequestDto requestDto, HttpServletRequest request) {
        return likeService.doLike(requestDto, request);
    }

    @RequestMapping(value = "/api/auth/post/like/{id}", method = RequestMethod.DELETE)
    public ResponseDto<?> cancelLike(@PathVariable Long id, HttpServletRequest request) {
        return likeService.cancelLike(id, request);
    }

    @RequestMapping(value = "/api/auth/comment/like", method = RequestMethod.POST)
    public ResponseDto<?> doCommentLike(@RequestBody LikeRequestDto requestDto, HttpServletRequest request) {
        return likeService.doCommentLike(requestDto, request);
    }

    @RequestMapping(value = "/api/auth/comment/like/{id}", method = RequestMethod.DELETE)
    public ResponseDto<?> cancelCommentLike(@PathVariable Long id, HttpServletRequest request) {
        return likeService.cancelCommentLike(id, request);
    }

    @RequestMapping(value = "/api/auth/subcomment/like", method = RequestMethod.POST)
    public ResponseDto<?> subCommentLike(@RequestBody LikeRequestDto requestDto, HttpServletRequest request) {
        return likeService.subCommentLike(requestDto, request);
    }
}
