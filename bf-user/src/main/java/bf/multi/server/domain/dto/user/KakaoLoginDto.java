package bf.multi.server.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoLoginDto {
    @JsonProperty("access_token")
    private String accessToken;

    private String phone;
    private String name;
    private String intro;
    private Integer age;

    @Builder
    public KakaoLoginDto(String accessToken, String phone, String name, String intro, Integer age) {
        this.accessToken = accessToken;
        this.phone = phone;
        this.name = name;
        this.intro = intro;
        this.age = age;
    }
}
