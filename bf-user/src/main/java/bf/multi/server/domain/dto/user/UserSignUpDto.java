package bf.multi.server.domain.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignUpDto {

    private String username;
    private String email;
    private String photoLink;
    private String gender;
    private String phone;
    private Integer age;

    @Builder
    public UserSignUpDto(String username, String email, String photoLink, String gender, String phone, Integer age) {
        this.username = username;
        this.email = email;
        this.photoLink = photoLink;
        this.gender = gender;
        this.phone = phone;
        this.age = age;
    }
}
