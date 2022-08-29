package bf.multi.server.domain.dto.requests;

import bf.multi.server.domain.helpee.Helpee;
import bf.multi.server.domain.requests.Requests;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Getter
@NoArgsConstructor
public class RequestsCreateDto {
    private Helpee helpee;

    private String requestType;

    private String requestDetail;

    private String location;

    private Timestamp requestTime;

    @Builder
    public RequestsCreateDto(String requestType, String requestDetail, String location, Timestamp requestTime) {
        this.requestType = requestType;
        this.requestDetail = requestDetail;
        this.location = location;
        this.requestTime = requestTime;
    }

    /* Helpee에 대한 Setter */
    public void setHelpee(Helpee helpee) {
        this.helpee = helpee;
    }

    public Requests toEntity() {
        return Requests
                .builder()
                .helpee(helpee)
                .requestType(requestType)
                .requestDetail(requestDetail)
                .location(location)
                .requestTime(requestTime)
                .build();
    }
}
