package Gojae.BookRecord.repository;

import Gojae.BookRecord.domain.Book;
import Gojae.BookRecord.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void 사용자_저장() {
        // given
        Member member = new Member();
        member.setName("이승기");
        member.setEmail("lsg@lsg.com");
        // when
        memberRepository.save(member);
        // then
        Member result = memberRepository.findById(member.getId()).get();
        assertThat(result).isEqualTo(member);
    }

    @Test
    public void 이메일로_조회() {
        // given
        Member member = new Member();
        member.setName("이승기");
        member.setEmail("lsg@lsg.com");
        Member result1 = memberRepository.save(member);
        // when
        Member result2 = memberRepository.findByEmail(member.getEmail()).get();
        // then
        assertThat(result1).isEqualTo(result2);
    }
}
