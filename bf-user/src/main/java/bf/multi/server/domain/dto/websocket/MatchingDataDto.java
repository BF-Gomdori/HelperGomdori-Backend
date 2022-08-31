package bf.multi.server.domain.dto.websocket;

import bf.multi.server.domain.helps.Helps;
import bf.multi.server.domain.requests.Requests;
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
    public static class HelperLocation{
        private String helperX;
        private String helperY;

        @Builder
        public HelperLocation(String helperX, String helperY) {
            this.helperX = helperX;
            this.helperY = helperY;
        }
    }

    @Data
    @NoArgsConstructor
    public static class HelpeeLocation{
        private String helpeeX;
        private String helpeeY;

        @Builder
        public HelpeeLocation(String helpeeX, String helpeeY) {
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
