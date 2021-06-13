package Gojae.BookRecord.controller;

import Gojae.BookRecord.domain.Book;
import Gojae.BookRecord.repository.BookRepository;
import Gojae.BookRecord.service.BookService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BookApiController {

    private final BookService bookService;
    private final BookRepository bookRepository;

    // Book API의 공통 Response 형식
    @Data
    @AllArgsConstructor
    static class ResponseBookDto {
        private Long id;
        private String title;
        private String author;
        private String publisher;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;
    }

    // 등록
    @PostMapping("/books/save")
    public ResponseBookDto saveBook(@RequestBody @Valid CreateBookRequest request) {
        try {
            Book book = new Book();
            book.setTitle(request.getTitle());
            book.setAuthor(request.getAuthor());
            book.setPublisher(request.getPublisher());
            Book result = bookService.join(book);
            // book 정보 생성 logback
            log.info("TABLE books에 책 정보[id:{}, title:{}, author:{}, publisher:{}]가 생성되었습니다. (생성시점: {})",
                     result.getId(), result.getTitle(), result.getAuthor(), result.getPublisher(), result.getCreatedDate());
            return new ResponseBookDto(result.getId(), result.getTitle(),
                                       result.getAuthor(), result.getPublisher(),
                                       result.getCreatedDate(), result.getModifiedDate());
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }
    // 등록 요청 class
    @Data
    static class CreateBookRequest {
        private String title;
        private String author;
        private String publisher;
    }

    // 수정 API
    @PutMapping("/books/update/{id}")
    public ResponseBookDto updateBook(@PathVariable("id") Long id,
                                               @RequestBody @Valid UpdateBookRequest request) {
        try {
            // id(PK)로 검색한 기존 책의 정보
            Book oldBook = bookRepository.findById(id).get();
            String oldTitle = oldBook.getTitle();
            String oldAuthor = oldBook.getAuthor();
            String oldPublisher = oldBook.getPublisher();
            // 수정하고자 하는 책 정보(제목, 저자, 출판사)가 이미 등록된 정보와 모두 같은 경우에는 수정하지 않 예외처리
            if (oldTitle.equals(request.title) && oldAuthor.equals(request.author) && oldPublisher.equals(request.publisher)) {
                throw new IllegalStateException("수정할 책 정보가 없습니다.");
            } else {
                bookService.titleChange(id, request.getTitle());
                bookService.authorChange(id, request.getAuthor());
                bookService.publisherChange(id, request.getPublisher());
                Book findBook = bookRepository.findById(id).get();
                // 책 정보 수보 logback
                log.info("TABLE books의 id:{}의 책 정보[title:{}, author:{}, publisher:{}]가 " +
                                "title:{}, author:{}, publisher:{}]로 {}에 수정되었습니다.",
                         id, oldTitle, oldAuthor, oldPublisher, findBook.getTitle(), findBook.getAuthor(),
                         findBook.getPublisher(), findBook.getModifiedDate());
                return new ResponseBookDto(findBook.getId(), findBook.getTitle(),
                                           findBook.getAuthor(), findBook.getPublisher(),
                                           findBook.getCreatedDate(), findBook.getModifiedDate());
            }
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }
    // 수정 요청 class
    @Data
    static class UpdateBookRequest {
        private String title;
        private String author;
        private String publisher;
    }

    // id(PK)로 조회 API
    @GetMapping("/books/check/{id}")
    public ResponseBookDto checkBook(@PathVariable Long id) {
        try {
            Book findBook = bookRepository.findById(id).get();
            return new ResponseBookDto(findBook.getId(), findBook.getTitle(),
                                       findBook.getAuthor(), findBook.getPublisher(),
                                       findBook.getCreatedDate(), findBook.getModifiedDate());
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    // 페이지 조회 API (booksPerPage 갯수만큼, page가 보여지도록)
    @GetMapping("/books/pages/size={booksPerPage}/page-index={page}")   //page 시작은 0
    public Page<ResponseBookDto> bookPages(@PathVariable Integer page, @PathVariable Integer booksPerPage) {
        try {
            // 페이징 및 id(PK)로 정렬하여 조회
            PageRequest pageRequest = PageRequest.of(page, booksPerPage, Sort.by("id"));
            Page<Book> bookList = bookRepository.findAll(pageRequest);
            return bookList.map(b -> new ResponseBookDto(b.getId(), b.getTitle(),
                                                         b.getAuthor(), b.getPublisher(),
                                                         b.getCreatedDate(), b.getModifiedDate()));
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }




}
