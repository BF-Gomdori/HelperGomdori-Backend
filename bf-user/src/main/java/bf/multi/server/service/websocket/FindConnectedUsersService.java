package bf.multi.server.service.websocket;

import bf.multi.server.domain.helps.Helps;
import bf.multi.server.domain.helps.HelpsRepository;
import bf.multi.server.domain.requests.Requests;
import bf.multi.server.domain.requests.RequestsRepository;
import bf.multi.server.security.JwtTokenProvider;
import bf.multi.server.domain.dto.websocket.HelpRequestDto;
import bf.multi.server.domain.dto.websocket.Location;
import bf.multi.server.domain.dto.websocket.MessageDto;
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

    // DISCONNECT 됐을 때 helps, requests 처리
    public void disconnectDispose(String username){
        // helps랑 requests 찾아서 삭제
        Helps helps = helpsRepository.findDistinctTopBySuccessIsFalseAndHelper_User_UsernameOrderByAcceptTimeDesc(username);
        helpsRepository.deleteById(helps.getId());
        Requests requests = requestsRepository.findDistinctTopByCompleteIsFalseAndHelpee_User_UsernameOrderByRequestTimeDesc(username);
        requestsRepository.deleteById(requests.getId());
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