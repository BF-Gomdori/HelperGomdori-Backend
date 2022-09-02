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
    private String FCMToken;
    private String phone;
    private String name;

    @Builder
    public KakaoLoginDto(String accessToken, String FCMToken, String phone, String name) {
        this.accessToken = accessToken;
        this.FCMToken = FCMToken;
        this.phone = phone;
        this.name = name;
    }
}
