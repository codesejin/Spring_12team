package com.example.intermediate.repository;

import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.SubComment;
import com.example.intermediate.domain.SubCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCommentLikeRepository extends JpaRepository<SubCommentLike, Long> {
    int countLikeBySubCommentId(Long subCommentId);
    SubCommentLike findBySubCommentIdAndMemberId(Long subCommentId, Long memberId);
    List<SubCommentLike> findAllByMember(Member member);
}
