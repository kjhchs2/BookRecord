package Gojae.BookRecord.repository;

import Gojae.BookRecord.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    //member 생성
    Member save(Member member);

    //Primary Key로 조회
    Optional<Member> findById(Long id);

    //email로 조회
    Optional<Member> findByEmail(String email);

    //paging을 위한 findAll
    Page<Member> findAll(Pageable pageable);

}
