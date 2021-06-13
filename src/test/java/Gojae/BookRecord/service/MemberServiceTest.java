//package Gojae.BookRecord.service;
//
//import Gojae.BookRecord.domain.Member;
//import Gojae.BookRecord.repository.SpringDataJpaMemberRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
////@Transactional
//class MemberServiceTest {
//
//    @Autowired MemberService memberService;
//    @Autowired
//    SpringDataJpaMemberRepository memberRepository;
//
//    @Test
//    void create() throws Exception{
//        //given
//        Member member = new Member();
//        member.setName("spring");
//        member.setEmail("kkk@kkk.com");
//
//        //when
//        Member findMember = memberService.join(member);
//
//        //then
//        Member result = memberRepository.findById(findMember.getId()).get();
//        Assertions.assertThat(result.getName()).isEqualTo("spring");
//
//
//        Member member2 = new Member();
//        member2.setName("summer");
//        member2.setEmail("kkk@kkk.com");
//        try {
//            memberService.join(member2);
//            System.out.println("member2 = " + member2);
//        } catch (IllegalStateException e) {
//            System.out.println("e = " + e);
//            Assertions.assertThat(e.getMessage()).isEqualTo("이미 이 메일주소로 가입을 하셨습니다.");
//        }
//    }
//
//    @Test
//    void validateOverlapNameCreate(){
//
//
//    }
//
//}