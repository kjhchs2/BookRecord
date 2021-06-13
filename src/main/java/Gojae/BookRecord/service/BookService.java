package Gojae.BookRecord.service;

import Gojae.BookRecord.domain.Book;
import Gojae.BookRecord.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository ;

    // 1. 책 생성
    @Transactional
    public Book join(Book book){
        try {
            validateBook(book);
            bookRepository.save(book);
            return book;
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }
    // 1-1. 중복 책 검증(제목, 저자, 출판사 모두 동일 시)
    private void validateBook(Book book) {
        List<Book> findBooks1 = bookRepository.findAllByTitle(book.getTitle());
        findBooks1.forEach(
            findBook -> {
                if (findBook.getAuthor().equals(book.getAuthor())
                    && findBook.getPublisher().equals(book.getPublisher())) {
                        throw new IllegalStateException("이 책은 이미 등록돼있습니다.");
                }
            });
    }

    // 2. 책 제목 변경
    @Transactional
    public void titleChange(Long id, String title) {
        try {
            Book book = bookRepository.findById(id).get();
            book.setTitle(title);
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    // 3. 저자 변경
    @Transactional
    public void authorChange(Long id, String author) {
        try {
            Book book = bookRepository.findById(id).get();
            book.setAuthor(author);
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    // 4. 출판사 변경
    @Transactional
    public void publisherChange(Long id, String publisher) {
        try {
            Book book = bookRepository.findById(id).get();
            book.setPublisher(publisher);
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }

}
