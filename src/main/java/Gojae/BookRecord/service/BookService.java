package Gojae.BookRecord.service;

import Gojae.BookRecord.domain.Book;
import Gojae.BookRecord.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository ;

    // 1. 책 생성
    @Transactional
    public Book join(Book book){
        try {
            validateBook(book);
            Book result = bookRepository.save(book);
            // book 정보 생성 logback
            log.info("TABLE books에 책 정보[id:{}, title:{}, author:{}, publisher:{}]가 생성되었습니다. (생성시점: {})",
                    result.getId(), result.getTitle(), result.getAuthor(), result.getPublisher(), result.getCreatedDate());
            return book;
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }
    // 1-1. 책 유효성 검사
    private void validateBook(Book book) {
        // 1-1-1. 중복 책 검증(제목, 저자, 출판사 모두 동일 시)
        // 책 제목으로 찾은 책 리스트 중에서
        List<Book> findBooks1 = bookRepository.findAllByTitle(book.getTitle());
        // 저자와, 출판사가 모두 같으면 같은 책으로 처리
        Optional<Book> result = findBooks1.stream().filter(findBook -> findBook.getAuthor().equals(book.getAuthor())
                    && findBook.getPublisher().equals(book.getPublisher())).findAny();
        if (result.isPresent()){
            throw new IllegalStateException("이 책은 이미 등록돼있습니다.");
        }
        // 1-1-2. 저자에 ','(쉼표)가 들어가면 저자 1명만 저장하도록 예외 처리
        if (book.getAuthor().contains(",")) {
            throw new IllegalStateException("저자는 한 명만 입력해주세요.");
        }
    }

    // 2. id(PK)로 책 찾기
    public Optional<Book> findBook(Long id) {
        try {
            return bookRepository.findById(id);
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    // 3. 책 제목 변경
    @Transactional
    public void titleChange(Long id, String title) {
        try {
            Book book = bookRepository.findById(id).get();
            book.setTitle(title);
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    // 4. 저자 변경
    @Transactional
    public void authorChange(Long id, String author) {
        try {
            Book book = bookRepository.findById(id).get();
            book.setAuthor(author);
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    // 5. 출판사 변경
    @Transactional
    public void publisherChange(Long id, String publisher) {
        try {
            Book book = bookRepository.findById(id).get();
            book.setPublisher(publisher);
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    // 6. 책 정보(제목, 저자, 출판사) 변경
    @Transactional
    public void bookInfoChange(Long id, String title, String author, String publisher) {
        try{
            titleChange(id, title);
            authorChange(id, author);
            publisherChange(id, publisher);
            Book findBook = bookRepository.findById(id).get();
            // 책 정보 수보 logback
            log.info("TABLE books의 id:{}의 책 정보가 [title:{}, author:{}, publisher:{}] " +
                     "로 {}에 수정되었습니다. (생성시점: {})",
                     id, title, author, publisher, findBook.getModifiedDate(),
                     findBook.getCreatedDate());
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    // 7. paging을 위한 책 전체 찾기
    public Page<Book> findAll(Pageable pageable){
        try {
            return bookRepository.findAll(pageable);
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }
}
