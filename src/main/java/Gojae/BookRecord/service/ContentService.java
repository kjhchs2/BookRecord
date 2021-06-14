package Gojae.BookRecord.service;

import Gojae.BookRecord.domain.Content;
import Gojae.BookRecord.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository ;

    // 1. Content 생성
    @Transactional
    public Content join(Content content){
        try {
            Content result = contentRepository.save(content);
            // Book 정보 생성 logback
            log.info("TABLE contents에 발췌문 정보[id:{}, member_id:{}, book_id:{}, " +
                     "extracted_page:{}, extracted_content:{}]가 생성되었습니다. (생성시점: {})",
                     result.getId(), result.getMemberId(), result.getBookId(), result.getExtractedPage(),
                     result.getExtractedContent(), result.getCreatedDate());
            return content;
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    // 2. id(PK)로 발췌문 찾기
    public Optional<Content> findContent(Long id) {
        try {
            return contentRepository.findById(id);
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    // 3. 발췌 페이지 번호 변경
    @Transactional
    public void extractedPageChange(Long id, Long extractedPage) {
        try{
            Content content = contentRepository.findById(id).get();
            content.setExtractedPage(extractedPage);
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    // 4. 발췌 내용 변경
    @Transactional
    public void extractedContentChange(Long id, String extractedContent) {
        try {
            Content content = contentRepository.findById(id).get();
            content.setExtractedContent(extractedContent);
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    // 5. 발췌문 정보(페이지 번호, 내용) 변경
    @Transactional
    public void contentInfoChange(Long id, Long extractedPage, String extractedContent) {
        try {
            extractedPageChange(id, extractedPage);
            extractedContentChange(id, extractedContent);
            Content findContent = contentRepository.findById(id).get();
            // 발췌문 정보 수정 logback
            log.info("TABLE contents의 id:{}의 발췌문 정보가 [extracted_page:{}, extracted_contents:{}] " +
                     "로 {}에 수정되었습니다. (생성시점: {})",
                     id, findContent.getExtractedPage(), findContent.getExtractedContent(),
                     findContent.getModifiedDate(), findContent.getCreatedDate());
        }catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    // 6. paging을 위한 발췌문 전체 찾기
    public Page<Content> findAll(Pageable pageable){
        try {
            return contentRepository.findAll(pageable);
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }

}
