package Gojae.BookRecord.service;

import Gojae.BookRecord.domain.Content;
import Gojae.BookRecord.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository ;

    // 1. Content 생성
    @Transactional
    public Content join(Content content){
        try {
            contentRepository.save(content);
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

    // 5. paging을 위한 발췌문 전체 찾기
    public Page<Content> findAll(Pageable pageable){
        try {
            return contentRepository.findAll(pageable);
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }

}
