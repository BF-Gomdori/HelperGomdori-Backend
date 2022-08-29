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

    private String message;

    private String location;

    private Timestamp requestTime;

    @Builder
    public RequestsCreateDto(String message, String location, Timestamp requestTime) {
        this.message = message;
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
                .message(message)
                .location(location)
                .requestTime(requestTime)
                .build();
    }
}
