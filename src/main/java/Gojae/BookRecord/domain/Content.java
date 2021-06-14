package Gojae.BookRecord.domain;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity(name="contents")
@Getter @Setter
public class Content extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Primary Key
    private Long id ;

    private Long memberId ;
    private Long bookId ;

    @NotNull(message = "발췌 페이지를 입력해주세요.")
    private Long extractedPage ;

    @NotBlank(message = "발췌 내용을 입력해주세요.")
    private String extractedContent ;

}
