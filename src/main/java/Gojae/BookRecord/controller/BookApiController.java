package Gojae.BookRecord.controller;

import Gojae.BookRecord.domain.Book;
import Gojae.BookRecord.service.BookService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class BookApiController {

    private final BookService bookService;

    // Book API의 공통 Response 형식
    @Data
    @AllArgsConstructor
    public static class ResponseBookDto {
        private Long id;
        private String title;
        private String author;
        private String publisher;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;
    }

    // 등록
    @PostMapping("/books")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseBookDto saveBook(@RequestBody @Valid CreateBookRequest request) {
        try {
            Book book = new Book();
            book.setTitle(request.getTitle());
            book.setAuthor(request.getAuthor());
            book.setPublisher(request.getPublisher());
            Book result = bookService.join(book);
            return new ResponseBookDto(result.getId(), result.getTitle(),
                                       result.getAuthor(), result.getPublisher(),
                                       result.getCreatedDate(), result.getModifiedDate());
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    // 등록 요청 class
    @Data
    public static class CreateBookRequest {
        private String title;
        private String author;
        private String publisher;
    }

    // 수정 API
    @PutMapping("/books/{id}")
    public ResponseBookDto updateBook(@PathVariable("id") Long id,
                                               @RequestBody @Valid UpdateBookRequest request) {
        try {
            // id(PK)로 검색한 기존 책의 정보
            Book oldBook = bookService.findBook(id).get();
            String oldTitle = oldBook.getTitle();
            String oldAuthor = oldBook.getAuthor();
            String oldPublisher = oldBook.getPublisher();
            // 수정하고자 하는 책 정보(제목, 저자, 출판사)가 이미 등록된 정보와 모두 같은 경우에는 수정하지 않 예외처리
            if (oldTitle.equals(request.getTitle()) && oldAuthor.equals(request.getAuthor()) && oldPublisher.equals(request.getPublisher())) {
                throw new IllegalStateException("수정할 책 정보가 없습니다.");
            } else {
                bookService.bookInfoChange(id, request.getTitle(), request.getAuthor(), request.getPublisher());
                Book findBook = bookService.findBook(id).get();
                return new ResponseBookDto(findBook.getId(), findBook.getTitle(),
                                           findBook.getAuthor(), findBook.getPublisher(),
                                           findBook.getCreatedDate(), findBook.getModifiedDate());
            }
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    // 수정 요청 class
    @Data
    public static class UpdateBookRequest {
        private String title;
        private String author;
        private String publisher;
    }

    // id(PK)로 조회 API
    @GetMapping("/books/{id}")
    public ResponseBookDto checkBook(@PathVariable Long id) {
        try {
            Book findBook = bookService.findBook(id).get();
            return new ResponseBookDto(findBook.getId(), findBook.getTitle(),
                                       findBook.getAuthor(), findBook.getPublisher(),
                                       findBook.getCreatedDate(), findBook.getModifiedDate());
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // 페이지 조회 API (booksPerPage 갯수만큼, page가 보여지도록)
    @GetMapping("/books")
    public Page<ResponseBookDto> bookPages(@RequestBody @Valid PagingBookRequest request) {
        try {
            // 페이징 및 id(PK)로 정렬하여 조회
            PageRequest pageRequest = PageRequest.of(request.getPage(), request.getBooksPerPage(), Sort.by("id"));
            Page<Book> bookList = bookService.findAll(pageRequest);
            return bookList.map(b -> new ResponseBookDto(b.getId(), b.getTitle(),
                                                         b.getAuthor(), b.getPublisher(),
                                                         b.getCreatedDate(), b.getModifiedDate()));
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    // 페이지 조회 요청 class
    @Data
    public static class PagingBookRequest {
        private Integer booksPerPage;
        private Integer page;
    }



}
