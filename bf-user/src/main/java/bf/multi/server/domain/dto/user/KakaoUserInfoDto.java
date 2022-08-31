package bf.multi.server.domain.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoDto {
    private Long id;
    private String email;
    private String nickname;
    private String gender;
    private String age;
    private String thumbnailImage;

    @Builder
    public KakaoUserInfoDto(Long id, String email, String nickname, String age, String gender, String thumbnailImage) {
        this.id = id;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.thumbnailImage = thumbnailImage;
    }
}
