package Gojae.BookRecord.service;

import Gojae.BookRecord.domain.Content;
import Gojae.BookRecord.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ContentServiceTest {

    @Autowired private ContentService contentService;

    @Test
    void 발췌_페이지_번호_변경() {
        // given
        Content content = new Content();
        content.setBookId(1L);
        content.setMemberId(1L);
        content.setExtractedPage(200L);
        content.setExtractedContent("여름이었다.");
        Content result = contentService.join(content);

        // when
        contentService.extractedPageChange(result.getId(), 50L);

        // then
        Assertions.assertThat(result.getExtractedPage()).isEqualTo(50L);
    }

    @Test
    void 발췌_내용_변경() {
        // given
        Content content = new Content();
        content.setBookId(1L);
        content.setMemberId(1L);
        content.setExtractedPage(200L);
        content.setExtractedContent("여름이었다.");
        Content result = contentService.join(content);

        // when
        contentService.extractedContentChange(result.getId(), "겨울이었다.");

        // then
        Assertions.assertThat(result.getExtractedContent()).isEqualTo("겨울이었다.");
    }

}
