package com.example.intermediate.scheduler;

import com.example.intermediate.domain.Post;
import com.example.intermediate.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Component
public class Scheduler {
    private final PostRepository postRepository;

    @Transactional
    //  초, 분, 시, 일, 월, 주 순서
    @Scheduled(cron = " 00 00 01 * * *")
    public void timoOutPostDelete() throws InterruptedException {
        System.out.println("test");
        //저장된 모든 포스트를 조회
        List<Post> postList = postRepository.findAll();


        boolean check = false;
        for (Post postOne : postList) {
            if (postOne.getComments().size() == 0) {
                System.out.println(" 게시글 " + postOne.getTitle() + "이 삭제되었습니다.");
                postRepository.delete(postOne);
                check = true;
            }

            if (check == false) {
                System.out.println("삭제할 게시글이 없습니다.");
            }
        }
    }
}
