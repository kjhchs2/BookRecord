package Gojae.BookRecord.service;

import Gojae.BookRecord.domain.Content;
import Gojae.BookRecord.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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

    // 2. 발췌 페이지 번호 변경
    @Transactional
    public void extractedPageChange(Long id, Long extractedPage) {
        try{
            Content content = contentRepository.findById(id).get();
            content.setExtractedPage(extractedPage);
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    // 3. 발췌 페이지 내용 변경
    @Transactional
    public void extractedContentChange(Long id, String extractedContent) {
        try {
            Content content = contentRepository.findById(id).get();
            content.setExtractedContent(extractedContent);
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }

}
