package Gojae.BookRecord.service;

import Gojae.BookRecord.domain.Book;
import Gojae.BookRecord.domain.Member;
import Gojae.BookRecord.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;

    @Test
    void 사용자_생성(){
        // given
        Member member = new Member();
        member.setName("고재헌");
        member.setEmail("kjh@woowahan.com");
        // when
        Member result = memberService.join(member);
        // then
        Assertions.assertThat(result.getName()).isEqualTo("고재헌");
        Assertions.assertThat(result.getEmail()).isEqualTo("kjh@woowahan.com");
    }

    @Test
    void 사용자_생성_중복이메일_검증() {
        // given
        Member member1 = new Member();
        member1.setName("하정우");
        member1.setEmail("same@same.com");
        Member member2 = new Member();
        member2.setName("광철용");
        member2.setEmail("same@same.com");
        memberService.join(member1);
        // when
        try{
            memberService.join(member2);
        } catch (Exception e){
            // then
            Assertions.assertThat(e.getMessage()).isEqualTo("이미 이 메일주소로 가입을 하셨습니다.");
        }
    }

    @Test
    void 사용자_이름_변경() {
        // given
        Member member1 = new Member();
        member1.setName("하정우");
        member1.setEmail("actors@naver.com");
        Member result = memberService.join(member1);
        // when
        memberService.nameChange(result.getId(), "송강호");
        // then
        Assertions.assertThat(result.getName()).isEqualTo("송강호");
    }
}