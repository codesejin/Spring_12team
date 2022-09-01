package com.example.intermediate.repository;

import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    int countLikeBypostId(Long postId);
    PostLike findByPostIdAndMemberId(Long postId, Long memberId);
    List<PostLike> findByMember(Member member);
}
