package com.example.intermediate.service;

import com.example.intermediate.controller.response.CommentResponseDto;
import com.example.intermediate.controller.response.PostResponseDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.controller.response.SubCommentResponseDto;
import com.example.intermediate.domain.*;
import com.example.intermediate.jwt.TokenProvider;
import com.example.intermediate.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final TokenProvider tokenProvider;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final SubCommentRepository subCommentRepository;
    private final SubCommentLikeRepository subCommentLikeRepository;


    @Transactional
    public ResponseDto<?> getPosts(HttpServletRequest request) {
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }
        List<Post> posts = postRepository.findAllByMember(member);
        List<PostResponseDto> postResponseDtoList = postResponseDtoListByMemberId(posts);
        return ResponseDto.success(postResponseDtoList);
    }

    @Transactional
    public ResponseDto<?> getComments(HttpServletRequest request) { // CommentResponseDTO에 post_id정보를 넣어야하지 않을까.
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }
        List<Comment> comments = commentRepository.findAllByMember(member);
        List<CommentResponseDto> commentResponseDtoList = commentResponseDtoListByMemberId(comments);
        return ResponseDto.success(commentResponseDtoList);
    }

    @Transactional
    public ResponseDto<?> getSubComments(HttpServletRequest request) { //
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }
        List<SubComment> subcomments = subCommentRepository.findAllByMember(member);
        List<SubCommentResponseDto> subCommentResponseDtoList = subCommentResponseDtoListByMemberId(subcomments);
        return ResponseDto.success(subCommentResponseDtoList);
    }
    @Transactional
    public ResponseDto<?> getLikePosts(HttpServletRequest request) {
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }
        List<PostLike> likesPost = postLikeRepository.findByMember(member);
        List<Post> posts = new ArrayList<>();
        for(int i=0;i<likesPost.size();i++){
            posts.add(likesPost.get(i).getPost());
        }
        List<PostResponseDto> postResponseDtoList = postResponseDtoListByMemberId(posts);
        return ResponseDto.success(postResponseDtoList);
    }

    public ResponseDto<?> getLikeComments(HttpServletRequest request) {
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }
        List<CommentLike> likesComment = commentLikeRepository.findCommentLikeByMember(member);
        List<Comment> comments = new ArrayList<>();
        for(int i=0;i<likesComment.size();i++){
            comments.add(likesComment.get(i).getComment());
        }
        List<CommentResponseDto> commentResponseDtoList = commentResponseDtoListByMemberId(comments);
        return ResponseDto.success(commentResponseDtoList);
    }

    @Transactional
    public ResponseDto<?> getLikeSubComments(HttpServletRequest request) {
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }
        List<SubCommentLike> likesSubComment = subCommentLikeRepository.findAllByMember(member);
        List<SubComment> subComments = new ArrayList<>();
        for(int i=0;i<subComments.size();i++){
            subComments.add(likesSubComment.get(i).getSubComment());
        }
        List<SubCommentResponseDto> subCommentResponseDtoList = subCommentResponseDtoListByMemberId(subComments);
        return ResponseDto.success(subCommentResponseDtoList);
    }

    private List<CommentResponseDto> commentResponseDtoListByMemberId(List<Comment> comments) {
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (Comment comment : comments) {
            int cntLikesComments = commentLikeRepository.countLikeBycommentId(comment.getId());
            List<SubComment> subCommentList = subCommentRepository.findAllByComment(comment);
            List<SubCommentResponseDto> subCommentResponseDtoList = subCommentResponseDtoListByMemberId(subCommentList);
            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .id(comment.getId())
                            .author(comment.getMember().getNickname())
                            .content(comment.getContent())
                            .subCommentResponseDtoList(subCommentResponseDtoList)
                            .cntLikes(cntLikesComments)
                            .createdAt(comment.getCreatedAt())
                            .modifiedAt(comment.getModifiedAt())
                            .build()
            );
        }
        return commentResponseDtoList;
    }
    private List<PostResponseDto> postResponseDtoListByMemberId(List<Post> posts) {
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        for (Post post : posts) {
            int cntLikesComments = postLikeRepository.countLikeBypostId(post.getId());
            List<Comment> commentList = commentRepository.findAllByPost(post);
            List<CommentResponseDto> commentResponseDtoList = commentResponseDtoListByMemberId(commentList);
            postResponseDtoList.add(
                    PostResponseDto.builder()
                            .id(post.getId())
                            .author(post.getMember().getNickname())
                            .title(post.getTitle())
                            .imgUrl(post.getImgUrl())
                            .content(post.getContent())
                            .cntLikes(cntLikesComments)
                            .commentResponseDtoList(commentResponseDtoList)
                            .createdAt(post.getCreatedAt())
                            .modifiedAt(post.getModifiedAt())
                            .build()
            );
        }
        return postResponseDtoList;
    }
    private List<SubCommentResponseDto> subCommentResponseDtoListByMemberId(List<SubComment> subcomments) {
        List<SubCommentResponseDto> subCommentResponseDtoList = new ArrayList<>();
        for (SubComment subComment : subcomments) {
            int cntLikesComments = subCommentLikeRepository.countLikeBySubCommentId(subComment.getId());
            subCommentResponseDtoList.add(
                    SubCommentResponseDto.builder()
                            .id(subComment.getId())
                            .author(subComment.getMember().getNickname())
                            .subComment(subComment.getSubComment())
                            .cntLikes(cntLikesComments)
                            .createdAt(subComment.getCreatedAt())
                            .modifiedAt(subComment.getModifiedAt())
                            .build()
            );
        }
        return subCommentResponseDtoList;
    }
    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

}

