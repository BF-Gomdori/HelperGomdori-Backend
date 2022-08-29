package bf.multi.server.domain.dto.helper;

import bf.multi.server.domain.helper.Helper;
import bf.multi.server.domain.helps.Helps;
import bf.multi.server.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class HelperResponseDto {

    private Long id;
    private User user;
    private Integer helpCount;
    private Double averageRate;
    private Integer hearts;

    @Builder
    public HelperResponseDto(Long id, Integer helpCount, Double averageRate, Integer hearts) {
        this.id = id;
        this.helpCount = helpCount;
        this.averageRate = averageRate;
        this.hearts = hearts;
    }

    public static HelperResponseDto from(Helper helper){
        return HelperResponseDto.builder()
                .id(helper.getId())
                .helpCount(helper.getHelpCount())
                .averageRate(helper.getAverageRate())
                .hearts(helper.getHearts())
                .build();
    }
}
