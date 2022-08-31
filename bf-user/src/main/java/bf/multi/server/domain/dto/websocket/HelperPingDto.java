package bf.multi.server.domain.dto.websocket;

import bf.multi.server.domain.helper.Helper;
import bf.multi.server.domain.user.User;
import lombok.*;

import javax.persistence.Column;

@Data
@NoArgsConstructor
public class HelperPingDto {
    private String photoLink;
    private String name;
    private String phone;
    private String gender;
    private Integer age;
    private HelperInfo helperInfo;

    public HelperPingDto(User user, Helper helper){
        this.name = user.getUsername();
        this.photoLink = user.getPhotoLink();
        this.age = user.getAge();
        this.phone = user.getPhone();
        this.gender = user.getGender();
        this.helperInfo = HelperInfo.builder()
                .averageRate(helper.getAverageRate())
                .hearts(helper.getHearts())
                .helpCount(helper.getHelpCount())
                .build();
    }
    @Data
    @NoArgsConstructor
    public static class HelperInfo{
        private Integer helpCount;
        private Double averageRate;
        private Integer hearts;

        @Builder
        public HelperInfo(Integer helpCount, Double averageRate, Integer hearts) {
            this.helpCount = helpCount;
            this.averageRate = averageRate;
            this.hearts = hearts;
        }
    }

}
