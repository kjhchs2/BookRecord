package Gojae.BookRecord.service;

import Gojae.BookRecord.domain.Content;
import Gojae.BookRecord.domain.Member;
import Gojae.BookRecord.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository ;

    // 1. 사용자 생성
    @Transactional
    public Member join(Member member){
        try {
            validateEmail(member);
            memberRepository.save(member);
            return member;
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }
    // 1-1. 중복 email 검증
    private void validateEmail(Member member) {
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(m-> {
                    throw new IllegalStateException("이미 이 메일주소로 가입을 하셨습니다.");
                });
    }

    // 2. id(PK)로 사용자 찾기
    public Optional<Member> findMember(Long id) {
        try {
            return memberRepository.findById(id);
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    // 3. 사용자 이름 변경
    @Transactional
    public void nameChange(Long id, String afterName) {
        try {
            Member member = memberRepository.findById(id).get();
            member.setName(afterName);
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }


    // 4. paging을 위한 책 전체 찾기
    public Page<Member> findAll(Pageable pageable){
        try {
            return memberRepository.findAll(pageable);
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }

}
