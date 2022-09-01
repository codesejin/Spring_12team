package com.example.intermediate.service;

import com.example.intermediate.controller.request.LikeRequestDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.domain.*;
import com.example.intermediate.jwt.TokenProvider;
import com.example.intermediate.repository.CommentLikeRepository;
import com.example.intermediate.repository.PostLikeRepository;
import com.example.intermediate.repository.SubCommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final SubCommentLikeRepository subCommentLikeRepository;

    private final TokenProvider tokenProvider;
    private final PostService postService;
    private final CommentService commentService;
    private final SubCommentService subCommentService;

    @Transactional
    public ResponseDto<?> doLike(LikeRequestDto requestDto, HttpServletRequest request) {
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = postService.isPresentPost(requestDto.getId());
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        PostLike checkLike = postLikeRepository.findByPostIdAndMemberId(post.getId(), member.getId());
        if(null!=checkLike) {
            return ResponseDto.fail("ALREADY_DONE", "이미 좋아요를 하셨습니다.");
        }

        PostLike postLike = PostLike.builder()
                .member(member)
                .post(post)
                .build();
        postLikeRepository.save(postLike);
        return ResponseDto.success("success");     //좋아요 개수 넣기
    }


    @Transactional
    public ResponseDto<?> cancelLike(Long id, HttpServletRequest request) {
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        PostLike postLike = isPresentPostLike(id);
        if (null == postLike) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 좋아요 id 입니다.");
        }

        if (postLike.validateMember(member)) {      // 이 내용을 수행하기 전에 이미 사용자 검증... 프론트 단에서 좋아요 취소 버튼 활성화
            return ResponseDto.fail("BAD_REQUEST", "작성자만 취소할 수 있습니다.");
        }

        postLikeRepository.delete(postLike);
        return ResponseDto.success("success");      //좋아요 개수 넣기
    }

    @Transactional
    public ResponseDto<?> doCommentLike(LikeRequestDto requestDto, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Comment comment = commentService.isPresentComment(requestDto.getId());
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        CommentLike checkLike = commentLikeRepository.findByCommentIdAndMemberId(comment.getId(), member.getId());
        if(null!=checkLike) {
            return ResponseDto.fail("ALREADY_DONE", "이미 좋아요를 하셨습니다.");
        }
        CommentLike commentLike = CommentLike.builder()
                .member(member)
                .comment(comment)
                .build();

        commentLikeRepository.save(commentLike);
        return ResponseDto.success("success");     //좋아요 개수 넣기
    }

    @Transactional
    public ResponseDto<?> cancelCommentLike(Long id, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        CommentLike commentLike = isPresentCommentLike(id);
        if (null == commentLike) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 좋아요 id 입니다.");
        }

        if (commentLike.validateMember(member)) {       // 이 내용을 수행하기 전에 이미 사용자 검증... 프론트 단에서 좋아요 취소 버튼 활성화
            return ResponseDto.fail("BAD_REQUEST", "작성자만 취소할 수 있습니다.");
        }

        commentLikeRepository.delete(commentLike);
        return ResponseDto.success("success");      //좋아요 개수 넣기
    }


    @Transactional
    public ResponseDto<?> subCommentLike(LikeRequestDto requestDto, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        SubComment subComment = subCommentService.isPresentSubComment(requestDto.getId());
        if (null == subComment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        SubCommentLike checkLike = subCommentLikeRepository.findBySubCommentIdAndMemberId(subComment.getId(), member.getId());
        if(null!=checkLike) {
            return ResponseDto.fail("ALREADY_DONE", "이미 좋아요를 하셨습니다.");
        }

        SubCommentLike subCommentLike = SubCommentLike.builder()
                .member(member)
                .subComment(subComment)
                .build();
        subCommentLikeRepository.save(subCommentLike);
        return ResponseDto.success("success");     //좋아요 개수 넣기
    }

    @Transactional
    public ResponseDto<?> cancelSubCommentLike(Long id, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        SubCommentLike subCommentLike = isPresentSubCommentLike(id);
        if (null == subCommentLike) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 좋아요 id 입니다.");
        }

        if (subCommentLike.validateMember(member)) {       // 이 내용을 수행하기 전에 이미 사용자 검증... 프론트 단에서 좋아요 취소 버튼 활성화
            return ResponseDto.fail("BAD_REQUEST", "작성자만 취소할 수 있습니다.");
        }

        subCommentLikeRepository.delete(subCommentLike);
        return ResponseDto.success("success");      //좋아요 개수 넣기
    }

    @Transactional(readOnly = true)
    public PostLike isPresentPostLike(Long id) {
        Optional<PostLike> optionalLike = postLikeRepository.findById(id);
        return optionalLike.orElse(null);
    }

    @Transactional(readOnly = true)
    public CommentLike isPresentCommentLike(Long id) {
        Optional<CommentLike> optionalLike = commentLikeRepository.findById(id);
        return optionalLike.orElse(null);
    }

    @Transactional(readOnly = true)
    public SubCommentLike isPresentSubCommentLike(Long id) {
        Optional<SubCommentLike> optionalLike = subCommentLikeRepository.findById(id);
        return optionalLike.orElse(null);
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

}
