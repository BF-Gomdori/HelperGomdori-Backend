package bf.multi.server.websocket.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HelpRequestDto {
    private String helpeeJwt;
    private String requestType;
    private String requestDetail;
    private String detailLocation;

    @Builder
    public HelpRequestDto(String helpeeJwt, String requestType, String requestDetail, String detailLocation) {
        this.helpeeJwt = helpeeJwt;
        this.requestType = requestType;
        this.requestDetail = requestDetail;
        this.detailLocation = detailLocation;
    }
}
