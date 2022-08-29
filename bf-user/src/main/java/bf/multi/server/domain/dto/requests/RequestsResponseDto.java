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

    private String message;

    private String location;

    private Timestamp requestTime;

    @Builder
    public RequestsResponseDto(Long id, String message, String location, Timestamp requestTime) {
        this.id = id;
        this.message = message;
        this.location = location;
        this.requestTime = requestTime;
    }

    public static RequestsResponseDto from(Requests requests) {

        return RequestsResponseDto
                .builder()
                .id(requests.getId())
                .message(requests.getMessage())
                .location(requests.getLocation())
                .requestTime(requests.getRequestTime())
                .build();
    }
}
