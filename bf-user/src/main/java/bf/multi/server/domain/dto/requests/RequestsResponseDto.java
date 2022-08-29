package bf.multi.server.domain.dto.requests;

import bf.multi.server.domain.requests.Requests;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
public class RequestsResponseDto {
    private Long id;

    private String requestType;

    private String requestDetail;

    private String location;

    private Timestamp requestTime;

    @Builder
    public RequestsResponseDto(Long id, String requestType, String requestDetail, String location, Timestamp requestTime) {
        this.id = id;
        this.requestType = requestType;
        this.requestDetail = requestDetail;
        this.location = location;
        this.requestTime = requestTime;
    }

    public static RequestsResponseDto from(Requests requests) {

        return RequestsResponseDto
                .builder()
                .id(requests.getId())
                .requestType(requests.getRequestType())
                .requestDetail(requests.getRequestDetail())
                .location(requests.getLocation())
                .requestTime(requests.getRequestTime())
                .build();
    }
}
