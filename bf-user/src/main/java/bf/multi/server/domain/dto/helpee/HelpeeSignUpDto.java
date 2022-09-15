package bf.multi.server.domain.dto.helpee;

import bf.multi.server.domain.helpee.Helpee;
import bf.multi.server.domain.user.User;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

@Getter
@NoArgsConstructor
public class HelpeeSignUpDto {

    @JsonIgnore
    private User user;

    private String type;
    private String intro;

    @Data
    @NoArgsConstructor
    public static class SignUpInfo{
        private String type;
        private String intro;
        @Builder
        public SignUpInfo(String type, String intro) {
            this.type = type;
            this.intro = intro;
        }
    }

    @Builder
    public HelpeeSignUpDto(SignUpInfo signUpInfo) {
        SignUpInfo info = SignUpInfo.builder()
                .type(signUpInfo.getType())
                .intro(signUpInfo.getIntro())
                .build();
        this.type = info.getType();
        this.intro = info.getIntro();
    }

    /* User에 대한 Setter */
    public void setUser(User user) {
        this.user = user;
    }

    public Helpee toEntity() {
        return Helpee
                .builder()
                .user(user)
                .type(type)
                .intro(intro)
                .requestCount(0)
                .averageRate(0.0)
                .hearts(0)
                .build();
    }
}
