package bf.multi.server.domain.dto.user;

import bf.multi.server.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
public class UserProfileResponseDto {

    private Long id;

    private String username;

    private String photoLink;

    private String gender;

    private Integer age;

    private String intro;

    @Builder
    public UserProfileResponseDto(Long id, String username, String email, String password, String photoLink, String gender, String phone, Integer age, String intro, Timestamp startDate, Timestamp modifiedDate) {
        this.id = id;
        this.username = username;
        this.photoLink = photoLink;
        this.gender = gender;
        this.age = age;
        this.intro = intro;
    }

    public static UserProfileResponseDto from(User user) {
        return UserProfileResponseDto
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .photoLink(user.getPhotoLink())
                .gender(user.getGender())
                .age(user.getAge())
                .intro(user.getIntro())
                .build();
    }
}
