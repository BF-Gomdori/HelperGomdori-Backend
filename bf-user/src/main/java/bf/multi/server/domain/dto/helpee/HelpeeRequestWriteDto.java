package bf.multi.server.domain.dto.helpee;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class HelpeeRequestWriteDto {
    private String image;
    private String name;
    private String location;

    @Builder
    public HelpeeRequestWriteDto(String image, String name, String location) {
        this.image = image;
        this.name = name;
        this.location = location;
    }
}
