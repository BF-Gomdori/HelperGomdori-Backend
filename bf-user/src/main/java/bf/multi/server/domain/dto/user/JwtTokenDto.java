package bf.multi.server.domain.dto.user;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtTokenDto {
    private String token;
}
