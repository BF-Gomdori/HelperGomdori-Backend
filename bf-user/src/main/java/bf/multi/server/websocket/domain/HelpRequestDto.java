package bf.multi.server.websocket.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HelpRequestDto {
    private String requestType;
    private String requestDetail;
    private String detailLocation;

    @Builder
    public HelpRequestDto(String requestType, String requestDetail, String detailLocation) {
        this.requestType = requestType;
        this.requestDetail = requestDetail;
        this.detailLocation = detailLocation;
    }
}
