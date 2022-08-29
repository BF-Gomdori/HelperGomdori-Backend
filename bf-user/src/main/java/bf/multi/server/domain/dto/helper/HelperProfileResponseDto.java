package bf.multi.server.domain.dto.helper;

import bf.multi.server.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
public class HelperProfileResponseDto {

    private Long userId;

    private String username;

    private String email;

    private String password;

    private String photoLink;

    private String gender;

    private String phone;

    private Integer age;

    private Timestamp startDate;

    private Timestamp modifiedDate;

    private Long helperId;

    private Integer helpCount;

    private Double averageRate;

    private Integer hearts;

    @Builder
    public HelperProfileResponseDto(Long userId, String username, String email, String password, String photoLink, String gender, String phone, Integer age, Timestamp startDate, Timestamp modifiedDate, Long helperId, Integer helpCount, Double averageRate, Integer hearts) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.photoLink = photoLink;
        this.gender = gender;
        this.phone = phone;
        this.age = age;
        this.startDate = startDate;
        this.modifiedDate = modifiedDate;
        this.helperId = helperId;
        this.helpCount = helpCount;
        this.averageRate = averageRate;
        this.hearts = hearts;
    }

    public static HelperProfileResponseDto from(User user) {

        return HelperProfileResponseDto
                .builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .photoLink(user.getPhotoLink())
                .gender(user.getGender())
                .phone(user.getPhone())
                .age(user.getAge())
                .startDate(user.getStartDate())
                .modifiedDate(user.getModifiedDate())
                .helperId(user.getHelper().getId())
                .helpCount(user.getHelper().getHelpCount())
                .averageRate(user.getHelper().getAverageRate())
                .hearts(user.getHelper().getHearts())
                .build();
    }
}
