package bf.multi.server.domain.dto.websocket;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Location {
    private Double x;
    private Double y;

    @Builder
    public Location(Double x, Double y) {
        this.x = x;
        this.y = y;
    }
}
