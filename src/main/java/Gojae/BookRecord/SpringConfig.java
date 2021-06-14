package Gojae.BookRecord;
import Gojae.BookRecord.repository.*;
import Gojae.BookRecord.service.BookService;
import Gojae.BookRecord.service.ContentService;
import Gojae.BookRecord.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final ContentRepository contentRepository;

    @Autowired
    public SpringConfig(MemberRepository memberRepository, BookRepository bookRepository, ContentRepository contentRepository) {
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
        this.contentRepository = contentRepository;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository);
    }
    @Bean
    public BookService bookService() { return new BookService(bookRepository);}
    @Bean
    public ContentService contentService() { return new ContentService(contentRepository);}

}
