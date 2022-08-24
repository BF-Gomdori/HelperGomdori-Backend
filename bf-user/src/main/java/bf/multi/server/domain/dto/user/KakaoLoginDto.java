package bf.multi.server.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class KakaoLoginDto {
    @JsonProperty("access_token")
    private String accessToken;

    private String phone;
    private String name;
    private String intro;
    private Integer age;
}
