package bf.multi.server.domain.dto.websocket;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MatchingDataDto {

    private HelperLocation helperLocation;
    private HelpeeLocation helpeeLocation;

    @Data
    @NoArgsConstructor
    public static class HelperLocation {
        private Double helperX;
        private Double helperY;

        @Builder
        public HelperLocation(Double helperX, Double helperY) {
            this.helperX = helperX;
            this.helperY = helperY;
        }
    }

    @Data
    @NoArgsConstructor
    public static class HelpeeLocation {
        private Double helpeeX;
        private Double helpeeY;

        @Builder
        public HelpeeLocation(Double helpeeX, Double helpeeY) {
            this.helpeeX = helpeeX;
            this.helpeeY = helpeeY;
        }
    }
    @Builder
    public MatchingDataDto(HelperLocation helperLocation, HelpeeLocation helpeeLocation) {
        this.helperLocation = helperLocation;
        this.helpeeLocation = helpeeLocation;
    }
}
