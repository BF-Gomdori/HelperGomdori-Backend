package bf.multi.server.websocket.domain;

import bf.multi.server.domain.helps.Helps;
import bf.multi.server.domain.requests.Requests;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HelpMessage {
    private String helpJwt;
    private String message;
    private Location location;
    private Requests helpRequest;
    private Helps helpResponse;

    @Builder
    public HelpMessage(String helpJwt, String message, Location location, Requests helpRequest, Helps helpResponse) {
        this.helpJwt = helpJwt;
        this.message = message;
        this.location = location;
        this.helpRequest = helpRequest;
        this.helpResponse = helpResponse;
    }
}
