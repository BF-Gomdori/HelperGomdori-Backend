package bf.multi.server.websocket.domain;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MessageDto {
    private MessageType type;
    private String sub; // 어디로 구독할 지?
    private String jwt;
    private Location location;
    private Timestamp time;

    public enum MessageType{ // 메시지 type을 다음 4가지 상황으로 정의
        ENTER, // 위치 정보 표시O
        QUIT, // 위치 정보 표시X
        HELP, // 도움 요청
        ACCEPT // 도움 수락
    }

    @Builder
    public MessageDto(MessageType type, String sub, String jwt, Location location, Timestamp time) {
        this.type = type;
        this.sub = sub;
        this.jwt = jwt;
        this.location = location;
        this.time = new Timestamp(System.currentTimeMillis());
    }
}
