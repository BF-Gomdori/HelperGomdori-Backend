package bf.multi.server.websocket.domain;

import bf.multi.server.domain.helper.Helper;
import bf.multi.server.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HelperPingDto {
    private User user;
    private Helper helper;
}
