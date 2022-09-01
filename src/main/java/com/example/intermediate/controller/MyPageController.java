package com.example.intermediate.controller;

import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class MyPageController {

    private final MyPageService myPageService;

    @RequestMapping(value = "/api/auth/mypage/posts", method = RequestMethod.GET)
    public ResponseDto<?> getPosts(HttpServletRequest request) {
        return myPageService.getPosts(request);
    }
    @RequestMapping(value = "/api/auth/mypage/comments", method = RequestMethod.GET)
    public ResponseDto<?> getComments(HttpServletRequest request) {
        return myPageService.getComments(request);
    }

    @RequestMapping(value = "/api/auth/mypage/subcomments", method = RequestMethod.GET)
    public ResponseDto<?> getSubComments(HttpServletRequest request) {
        return myPageService.getSubComments(request);
    }

    @RequestMapping(value = "/api/auth/mypage/likes/post", method = RequestMethod.GET)
    public ResponseDto<?> getLikePosts(HttpServletRequest request) {
        return myPageService.getLikePosts(request);
    }

    @RequestMapping(value = "/api/auth/mypage/likes/comment", method = RequestMethod.GET)
    public ResponseDto<?> getLikeComments(HttpServletRequest request) {
        return myPageService.getLikeComments(request);
    }

    @RequestMapping(value = "/api/auth/mypage/likes/subcomment", method = RequestMethod.GET)
    public ResponseDto<?> getLikeSubComments(HttpServletRequest request) {
        return myPageService.getLikeSubComments(request);
    }

}
