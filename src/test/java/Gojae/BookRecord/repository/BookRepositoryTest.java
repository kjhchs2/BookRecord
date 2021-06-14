package Gojae.BookRecord.repository;

import Gojae.BookRecord.domain.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class BookRepositoryTest {

    @Autowired private BookRepository bookRepository;

    @Test
    public void 책_저장() {
        // given
        Book book = new Book();
        book.setTitle("과학콘서트");
        book.setAuthor("정재승");
        book.setPublisher("어크로스");
        // when
        bookRepository.save(book);
        // then
        Book result = bookRepository.findById(book.getId()).get();
        assertThat(result).isEqualTo(book);
    }

    @Test
    public void 책제목으로_전부찾기() {
        // given
        Book book1 = new Book();
        book1.setTitle("과학콘서트");
        book1.setAuthor("정재승");
        book1.setPublisher("어크로스");
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setTitle("과학콘서트");
        book2.setAuthor("고재헌");
        book2.setPublisher("민음사");
        bookRepository.save(book2);

        // when
        List<Book> result = bookRepository.findAllByTitle("과학콘서트");

        // then
        assertThat(result.size()).isEqualTo(2);
    }

}
