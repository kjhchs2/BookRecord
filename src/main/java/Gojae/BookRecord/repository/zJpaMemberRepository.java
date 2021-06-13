//package Gojae.BookRecord.repository;
//
//import Gojae.BookRecord.domain.Member;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Repository;
//
//import javax.persistence.EntityManager;
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//@RequiredArgsConstructor
//public class JpaMemberRepository implements MemberRepository {
//
//    private final EntityManager em;
//
//    @Override
//    public Member save(Member member) {
//        em.persist(member);
//        return member;
//    }
//
//    @Override
//    public Member findById(Long id) {
//        return em.find(Member.class, id);
//    }
//
//    @Override
//    public Optional<Member> findByEmail(String email) {
//        List<Member> result = em.createQuery("select m from Member m where m.email = :email  ", Member.class)
//                        .setParameter("email", email)
//                        .getResultList();
//        return result.stream().findAny();
//    }
//
//    @Override
//    public Optional<Member> findByPages(Integer pages) {
//        return Optional.empty();
//    }
//
//    public List<Member> findAll() {
//        return em.createQuery("select m from Member m", Member.class)
//                .getResultList();
//    }
//
//
//}
//
//
//
//
//
//
//
//
