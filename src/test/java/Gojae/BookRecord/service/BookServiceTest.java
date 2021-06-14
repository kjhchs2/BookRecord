package Gojae.BookRecord.service;

import Gojae.BookRecord.domain.Book;
import Gojae.BookRecord.domain.Member;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class BookServiceTest {

    @Autowired private BookService bookService;

    @Test
    void 책_생성(){
        // given
        Book book = new Book();
        book.setTitle("과학콘서트");
        book.setAuthor("정재승");
        book.setPublisher("어크로스");
        // when
        Book result = bookService.join(book);
        // then
        assertThat(result.getTitle()).isEqualTo("과학콘서트");
        assertThat(result.getAuthor()).isEqualTo("정재승");
        assertThat(result.getPublisher()).isEqualTo("어크로스");
    }

    @Test
    void 책_중복생성_검증(){
        // given
        Book book1 = new Book();
        book1.setTitle("과학콘서트");
        book1.setAuthor("정재승");
        book1.setPublisher("어크로스");
        bookService.join(book1);
        Book book2 = new Book();
        book2.setTitle("과학콘서트");
        book2.setAuthor("정재승");
        book2.setPublisher("어크로스");
        // when
        try{
            bookService.join(book2);
        } catch (Exception e){
        //then
             assertThat(e.getMessage()).isEqualTo("이 책은 이미 등록돼있습니다.");
        }
    }

    @Test
    void 책_제목_변경() {
        // given
        Book book = new Book();
        book.setTitle("수학콘서트");
        book.setAuthor("정재승");
        book.setPublisher("어크로스");
        Book result = bookService.join(book);
        // when
        bookService.titleChange(result.getId(), "과학콘서트");
        // then
        assertThat(result.getTitle()).isEqualTo("과학콘서트");
    }
    @Test
    void 저자_변경() {
        // given
        Book book = new Book();
        book.setTitle("과학콘서트");
        book.setAuthor("정원승");
        book.setPublisher("어크로스");
        Book result = bookService.join(book);
        // when
        bookService.authorChange(result.getId(), "정재승");
        // then
        assertThat(result.getAuthor()).isEqualTo("정재승");
    }
    @Test
    void 출판사_변경() {
        // given
        Book book = new Book();
        book.setTitle("과학콘서트");
        book.setAuthor("정원승");
        book.setPublisher("더크로스");
        Book result = bookService.join(book);
        // when
        bookService.publisherChange(result.getId(), "어크로스");
        // then
        assertThat(result.getPublisher()).isEqualTo("어크로스");
    }

    @Test
    void 책_정보_모두_변경() {
        // given
        Book book = new Book();
        book.setTitle("과학콘서트");
        book.setAuthor("정원승");
        book.setPublisher("더크로스");
        Book result = bookService.join(book);
        // when
        bookService.bookInfoChange(result.getId(), "아몬드", "손원평", "창비");
        // then
        assertThat(result.getTitle()).isEqualTo("아몬드");
        assertThat(result.getAuthor()).isEqualTo("손원평");
        assertThat(result.getPublisher()).isEqualTo("창비");
    }

}
