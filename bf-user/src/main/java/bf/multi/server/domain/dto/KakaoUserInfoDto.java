package bf.multi.server.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoDto {
    private Long id;
    private String email;
    private String nickname;
    private String gender;
    private String thumbnailImage;

    public KakaoUserInfoDto(Long id, String email, String nickname, String gender, String thumbnailImage) {
        this.id = id;
        this.nickname = nickname;
        this.gender = gender;
        this.email = email;
        this.thumbnailImage = thumbnailImage;
    }
}
