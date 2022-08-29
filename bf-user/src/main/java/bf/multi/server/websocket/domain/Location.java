package bf.multi.server.websocket.domain;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Location {
    private String x;
    private String y;

    @Builder
    public Location(String x, String y) {
        this.x = x;
        this.y = y;
    }
}
