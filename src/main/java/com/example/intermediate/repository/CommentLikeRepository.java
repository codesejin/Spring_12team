package com.example.intermediate.repository;

import com.example.intermediate.domain.CommentLike;
import com.example.intermediate.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    int countLikeBycommentId(Long commentId);
    CommentLike findByCommentIdAndMemberId(Long commentId, Long memberId);
    List<CommentLike> findCommentLikeByMember(Member member);
}
