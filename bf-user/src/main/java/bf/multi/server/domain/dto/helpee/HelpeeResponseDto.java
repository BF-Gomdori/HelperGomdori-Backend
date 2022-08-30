package bf.multi.server.domain.dto.helpee;

import bf.multi.server.domain.helpee.Helpee;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class HelpeeResponseDto {
    private Long id;

    private String type;

    private Integer requestCount;

    private Double averageRate;

    private Integer hearts;

    @Builder
    public HelpeeResponseDto(Long id, String type, Integer requestCount, Double averageRate, Integer hearts) {
        this.id = id;
        this.type = type;
        this.requestCount = requestCount;
        this.averageRate = averageRate;
        this.hearts = hearts;
    }

    public static HelpeeResponseDto from(Helpee helpee) {

        return HelpeeResponseDto
                .builder()
                .id(helpee.getId())
                .type(helpee.getType())
                .requestCount(helpee.getRequestCount())
                .averageRate(helpee.getAverageRate())
                .hearts(helpee.getHearts())
                .build();
    }


}
