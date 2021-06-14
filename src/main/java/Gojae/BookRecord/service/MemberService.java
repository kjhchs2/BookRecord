package Gojae.BookRecord.service;

import Gojae.BookRecord.domain.Content;
import Gojae.BookRecord.domain.Member;
import Gojae.BookRecord.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository ;

    // 1. 사용자 생성
    @Transactional
    public Member join(Member member){
        try {
            validateEmail(member);
            Member result = memberRepository.save(member);
            // member 정보 생성 logback
            log.info("TABLE members에 사용자 정보[id:{}, name:{}, email:{}]가 생성되었습니다. (생성시점: {})",
                    result.getId(), result.getName(), result.getEmail(), result.getCreatedDate());
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
            Member findMember = memberRepository.findById(id).get();
            findMember.setName(afterName);
            // 사용자 정보 수정 logback
            log.info("TABLE members의 id:{}의 사용자 정보가 [name:{}, email:{}] " +
                     "로 {}에 수정되었습니다. (생성시점: {})",
                     id, findMember.getName(), findMember.getEmail(),
                     findMember.getModifiedDate(), findMember.getCreatedDate());
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
