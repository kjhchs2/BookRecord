package Gojae.BookRecord.controller;

import Gojae.BookRecord.domain.Content;
import Gojae.BookRecord.service.ContentService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class ContentApiController {

    private final ContentService contentService;

    // Content API의 공통 Response 형식
    @Data
    @AllArgsConstructor
    public static class ResponseContentDto {
        private Long id;
        private Long memberId;
        private Long bookId;
        private Long extractedPage;
        private String extractedContent;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;
    }

    // 등록
    @PostMapping("/contents")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseContentDto saveContent(@RequestBody @Valid CreateContentRequest request) {
        try {
            Content content = new Content();
            content.setMemberId(request.getMemberId());
            content.setBookId(request.getBookId());
            content.setExtractedPage(request.getExtractedPage());
            content.setExtractedContent(request.getExtractedContent());
            Content result = contentService.join(content);
            return new ResponseContentDto(result.getId(), result.getMemberId(),
                                          result.getBookId(), result.getExtractedPage(),
                                          result.getExtractedContent(),
                                          result.getCreatedDate(), result.getModifiedDate());
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    // 등록 요청 class
    @Data
    public static class CreateContentRequest {
        private Long memberId;
        private Long bookId;
        private Long extractedPage;
        private String extractedContent;
    }

    // 수정 API
    @PutMapping("/contents/{id}")
    public ResponseContentDto updateContent(@PathVariable("id") Long id,
                                               @RequestBody @Valid UpdateContentRequest request) {
        try {
            // id(PK)로 검색한 기존 발췌문 정보
            Content oldContent = contentService.findContent(id).get();
            Long oldExtractedPage = oldContent.getExtractedPage();
            String oldExtractedContent = oldContent.getExtractedContent();
            // 수정하고자 하는 발췌문 정보(페이지, 내용)가 이미 등록된 정보와 모두 같은 경우에는 수정하지 않고 예외처리
            if (oldExtractedPage.equals(request.getExtractedPage()) && oldExtractedContent.equals(request.getExtractedContent())) {
                throw new IllegalStateException("수정할 발췌문 정보가 없습니다.");
            } else {
                contentService.contentInfoChange(id, request.getExtractedPage(), request.getExtractedContent());
                Content findContent = contentService.findContent(id).get();
                return new ResponseContentDto(findContent.getId(), findContent.getMemberId(),
                        findContent.getBookId(), findContent.getExtractedPage(),
                        findContent.getExtractedContent(),
                        findContent.getCreatedDate(), findContent.getModifiedDate());
            }
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    // 수정 요청 class
    @Data
    public static class UpdateContentRequest {
        private Long extractedPage;
        private String extractedContent;
    }

    // id(PK)로 조회 API
    @GetMapping("/contents/{id}")
    public ResponseContentDto checkContent(@PathVariable Long id) {
        try {
            Content findContent = contentService.findContent(id).get();
            return new ResponseContentDto(findContent.getId(), findContent.getMemberId(),
                                          findContent.getBookId(), findContent.getExtractedPage(),
                                          findContent.getExtractedContent(),
                                          findContent.getCreatedDate(), findContent.getModifiedDate());
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    // 페이지 조회 API (contentsPerPage 갯수만큼, page가 보여지도록)
    @GetMapping("/contents")
    public Page<ResponseContentDto> contentPages(@RequestBody @Valid PagingContentRequest request) {
        try {
            // 페이징 및 id(PK)로 정렬하여 조회
            PageRequest pageRequest = PageRequest.of(request.getPage(), request.getContentsPerPage(), Sort.by("id"));
            Page<Content> contentList = contentService.findAll(pageRequest);
            return contentList.map(c -> new ResponseContentDto(c.getId(), c.getMemberId(),
                                                               c.getBookId(), c.getExtractedPage(), c.getExtractedContent(),
                                                               c.getCreatedDate(), c.getModifiedDate()));
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    // 페이지 조회 요청 class
    @Data
    public static class PagingContentRequest {
        private Integer contentsPerPage;
        private Integer page;
    }
}
