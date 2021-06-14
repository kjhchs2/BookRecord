package Gojae.BookRecord.domain;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity(name="members")
@Getter @Setter
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Primary Key
    public Long id;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @Email
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

}
