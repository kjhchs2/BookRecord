package Gojae.BookRecord.domain;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity(name="members")
@Getter @Setter
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotEmpty(message = "이름을 입력해주세요.")
    private String name;

    @Email
    @NotEmpty(message = "이메일을 입력해주세요.")
    private String email;

}
