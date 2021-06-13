package Gojae.BookRecord.controller;

import Gojae.BookRecord.domain.Book;
import Gojae.BookRecord.domain.Content;
import Gojae.BookRecord.repository.ContentRepository;
import Gojae.BookRecord.service.ContentService;
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
@Slf4j
@RequiredArgsConstructor
public class ContentApiController {

    private final ContentService contentService;
    private final ContentRepository contentRepository;

    // Content API의 공통 Response 형식
    @Data
    @AllArgsConstructor
    static class ResponseContentDto {
        private Long id;
        private Long memberId;
        private Long bookId;
        private Long extractedPage;
        private String extractedContent;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;
    }

    // 등록
    @PostMapping("/contents/save")
    public ResponseContentDto saveContent(@RequestBody @Valid CreateContentRequest request) {
        try {
            Content content = new Content();
            content.setMemberId(request.getMemberId());
            content.setBookId(request.getBookId());
            content.setExtractedPage(request.getExtractedPage());
            content.setExtractedContent(request.getExtractedContent());
            Content result = contentService.join(content);
            // Book 정보 생성 logback
            log.info("TABLE contents에 발췌문 정보[id:{}, member_id:{}, book_id:{}, extracted_page:{}, extracted_content:{}]가 생성되었습니다. (생성시점: {})",
                     result.getId(), result.getMemberId(), result.getBookId(), result.getExtractedPage(),
                     result.getExtractedContent(), result.getCreatedDate());
            return new ResponseContentDto(result.getId(), result.getMemberId(),
                                          result.getBookId(), result.getExtractedPage(),
                                          result.getExtractedContent(),
                                          result.getCreatedDate(), result.getModifiedDate());
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }
    // 등록 요청 class
    @Data
    static class CreateContentRequest {
        private Long memberId;
        private Long bookId;
        private Long extractedPage;
        private String extractedContent;
    }

    // 수정 API
    @PutMapping("/contents/update/{id}")
    public ResponseContentDto updateContent(@PathVariable("id") Long id,
                                               @RequestBody @Valid UpdateContentRequest request) {
        try {
            // id(PK)로 검색한 기존 발췌문 정보
            Content oldContent = contentRepository.findById(id).get();
            Long oldExtractedPage = oldContent.getExtractedPage();
            String oldExtractedContent = oldContent.getExtractedContent();
            // 수정하고자 하는 발췌문 정보(페이지, 내용)가 이미 등록된 정보와 모두 같은 경우에는 수정하지 않고 예외처리
            if (oldExtractedPage.equals(request.extractedPage) && oldExtractedContent.equals(request.extractedContent)) {
                throw new IllegalStateException("수정할 발췌문 정보가 없습니다.");
            } else {
                contentService.extractedPageChange(id, request.getExtractedPage());
                contentService.extractedContentChange(id, request.getExtractedContent());
                Content findContent = contentRepository.findById(id).get();
                // 발췌문 정보 수정 logback
                log.info("TABLE contents의 id:{}의 발췌문 정보[extracted_page:{}, extracted_contents:{}]가 " +
                                "[extracted_page:{}, extracted_contents:{}]로 {}에 수정되었습니다.",
                        id, oldExtractedPage, oldExtractedContent, findContent.getExtractedPage(),
                        findContent.getExtractedContent(), findContent.getModifiedDate());
                return new ResponseContentDto(findContent.getId(), findContent.getMemberId(),
                        findContent.getBookId(), findContent.getExtractedPage(),
                        findContent.getExtractedContent(),
                        findContent.getCreatedDate(), findContent.getModifiedDate());
            }
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }
    // 수정 요청 class
    @Data
    static class UpdateContentRequest {
        private Long extractedPage;
        private String extractedContent;
    }

    // id(PK)로 조회 API
    @GetMapping("/contents/check/{id}")
    public ResponseContentDto checkContent(@PathVariable Long id) {
        try {
            Content findContent = contentRepository.findById(id).get();
            return new ResponseContentDto(findContent.getId(), findContent.getMemberId(),
                                          findContent.getBookId(), findContent.getExtractedPage(),
                                          findContent.getExtractedContent(),
                                          findContent.getCreatedDate(), findContent.getModifiedDate());
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }


    // 페이지 조회 API (contentsPerPage 갯수만큼, page가 보여지도록)
    @GetMapping("/contents/pages/size={contentsPerPage}/page-index={page}")
    public Page<ResponseContentDto> contentPages(@PathVariable Integer page, @PathVariable Integer contentsPerPage) {
        try {
            // 페이징 및 id(PK)로 정렬하여 조회
            PageRequest pageRequest = PageRequest.of(page, contentsPerPage, Sort.by("id"));
            Page<Content> contentList = contentRepository.findAll(pageRequest);
            return contentList.map(c -> new ResponseContentDto(c.getId(), c.getMemberId(),
                                                               c.getBookId(), c.getExtractedPage(), c.getExtractedContent(),
                                                               c.getCreatedDate(), c.getModifiedDate()));
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }
}
