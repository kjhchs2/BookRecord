package Gojae.BookRecord.repository;

import Gojae.BookRecord.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // book 생성
    Book save(Book book);

    // id로 조회 (Primary Key로 조회)
    Optional<Book> findById(Long id);

    // Title로 조회 (book 등록시 중복여부 확인)
    List<Book> findAllByTitle(String title);

    // paging을 위한 findAll
    Page<Book> findAll(Pageable pageable);

}
