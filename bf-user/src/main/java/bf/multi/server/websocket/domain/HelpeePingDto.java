package bf.multi.server.websocket.domain;

import lombok.*;

@Data
@NoArgsConstructor
public class HelpeePingDto {
    private String photoLink;
    private String type;
    private String name;
    private HelpRequestDto helpRequestDto;

    @Builder
    public HelpeePingDto(String photoLink, String type, String name, HelpRequestDto helpRequestDto) {
        this.photoLink = photoLink;
        this.type = type;
        this.name = name;
        this.helpRequestDto = helpRequestDto;
    }
}
