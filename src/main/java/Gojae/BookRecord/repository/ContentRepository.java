package Gojae.BookRecord.repository;

import Gojae.BookRecord.domain.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    // content 생성
    Content save(Content content);

    // id로 조회(Primary Key로 조회)
    Optional<Content> findById(Long id);

    // paging을 위한 findAll
    Page<Content> findAll(Pageable pageable);

}
