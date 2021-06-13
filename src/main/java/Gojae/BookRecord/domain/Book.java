package Gojae.BookRecord.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Entity(name="books")
@Getter @Setter
public class Book extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotEmpty(message = "책 제목을 입력해주세요.")
    private String title;

    @NotEmpty(message = "저자를 입력해주세요.")
    private String author;

    @NotEmpty(message = "출판사를 입력해주세요.")
    private String publisher;

}
