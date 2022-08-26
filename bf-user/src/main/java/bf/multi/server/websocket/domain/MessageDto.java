package bf.multi.server.websocket.domain;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private String senderId;
    private String receiverId;
    private String msg;

}
