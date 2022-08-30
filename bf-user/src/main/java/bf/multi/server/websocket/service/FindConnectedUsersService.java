package bf.multi.server.websocket.service;

import bf.multi.server.domain.helps.Helps;
import bf.multi.server.domain.helps.HelpsRepository;
import bf.multi.server.domain.requests.Requests;
import bf.multi.server.domain.requests.RequestsRepository;
import bf.multi.server.security.JwtTokenProvider;
import bf.multi.server.websocket.domain.HelpRequestDto;
import bf.multi.server.websocket.domain.Location;
import bf.multi.server.websocket.domain.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FindConnectedUsersService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RequestsRepository requestsRepository;
    private final HelpsRepository helpsRepository;

    public List<Helps> loadHelpsBeforeConnectedUser(Timestamp now){
        return helpsRepository
                .findAllBySuccessIsFalseAndAcceptTimeBefore(now);
    }

    public List<Requests> loadRequestsBeforeConnectedUser(Timestamp now){
        return requestsRepository
                .findAllByCompleteIsFalseAndRequestTimeBefore(now);
    }

    public List<MessageDto> composeInitData(List<Helps> helpsList, List<Requests> requestsList){
        List<MessageDto> messageDtoList = new ArrayList<>();
        helpsList.forEach(list -> {
            MessageDto messageDto = MessageDto.builder()
                    .type(MessageDto.MessageType.ENTER).sub("main")
                    // TODO: JWT 설정 어케할까..
                    .jwt("jwt")
                    .location(Location.builder().x(list.getX()).y(list.getY()).build())
                    .helpRequest(null).time(list.getAcceptTime())
                    .build();
            messageDtoList.add(messageDto);
        });
        requestsList.forEach(list -> {
            MessageDto messageDto = MessageDto.builder()
                    .type(MessageDto.MessageType.HELP).sub("main").time(list.getRequestTime())
                    // TODO: JWT 설정 어케할까..
                    .jwt("jwt")
                    .location(Location.builder().x(list.getX()).y(list.getY()).build())
                    .helpRequest(HelpRequestDto.builder()
                            // TODO: JWT 설정 어케할까..
                            .helpeeJwt("helpeeJwt")
                            .requestType(list.getRequestType()).requestDetail(list.getRequestDetail())
                            .detailLocation(list.getLocation())
                            .build())
                    .build();
            messageDtoList.add(messageDto);
        });
        return messageDtoList;
    }
}