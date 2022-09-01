package com.example.intermediate;

import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.Post;
import com.example.intermediate.repository.MemberRepository;
import com.example.intermediate.repository.PostRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoryTests {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    PostRepository postRepository;

    @Test
    public void createMembers() {
        for(int i = 1 ; i < 10; i++) {
            memberRepository.save(Member.builder()
                    .nickname("user"+i)
                    .password(passwordEncoder.encode("password")).build());
        }
    }

    @Test
    public void createPosts() {
        for(int i = 1; i < 30; i++) {
            Optional<Member> optionalMember = memberRepository.findById((long)(Math.random()*8+1));
            Member member = Member.builder()
                            .id(optionalMember.get().getId())
                                    .nickname(optionalMember.get().getNickname())
                                            .password(optionalMember.get().getPassword())
                                                    .build();
            postRepository.save(Post.builder()
                    .title("Title"+i)
                    .content("Content....."+i)
                            .imgUrl("url..."+i)
                    .member(member)
                    .build());
        }
    }

}
