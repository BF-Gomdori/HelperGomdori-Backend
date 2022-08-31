package bf.multi.server.domain.dto.websocket;

import lombok.*;

@Data
@NoArgsConstructor
public class HelpeePingDto {
    private String photoLink;
    private String type;
    private String location;
    private String name;
    private String phone;
    private String gender;
    private Integer age;
    private HelpRequestDto helpRequest;

    @Builder

    public HelpeePingDto(String photoLink, String type,
                         String location, String name,
                         String phone, String gender, Integer age,
                         HelpRequestDto helpRequestDto) {
        this.photoLink = photoLink;
        this.type = type;
        this.location = location;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.age = age;
        this.helpRequest = helpRequestDto;
    }
}
