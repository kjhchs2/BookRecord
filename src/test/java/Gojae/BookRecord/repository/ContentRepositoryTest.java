package Gojae.BookRecord.repository;

import Gojae.BookRecord.domain.Book;
import Gojae.BookRecord.domain.Content;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ContentRepositoryTest {
    @Autowired
    private ContentRepository contentRepository;

    @Test
    public void 발췌문_저장() {
        // given
        Content content = new Content();
        content.setMemberId(1L);
        content.setBookId(1L);
        content.setExtractedPage(20L);
        content.setExtractedContent("발췌를 합시다.");
        // when
        contentRepository.save(content);
        // then
        Content result = contentRepository.findById(content.getId()).get();
        assertThat(result).isEqualTo(content);
    }

}
