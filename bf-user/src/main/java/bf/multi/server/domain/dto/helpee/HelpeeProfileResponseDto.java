package bf.multi.server.domain.dto.helpee;

import bf.multi.server.domain.dto.user.UserProfileResponseDto;
import bf.multi.server.domain.requests.Requests;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class HelpeeProfileResponseDto {

    Map<String, Double> requestsPercentMap;
    private UserProfileResponseDto userProfileResponseDto;

    @Builder
    public HelpeeProfileResponseDto(UserProfileResponseDto userProfileResponseDto, List<Requests> requests) {
        this.userProfileResponseDto = userProfileResponseDto;
        this.requestsPercentMap = MapOfRequestsPercentage.RequestsListToMap(requests);
    }

    @NoArgsConstructor
    static class MapOfRequestsPercentage {
        static Map<String, Double> RequestsListToMap(List<Requests> requestsList) {
            Map<String, Double> requestsPercentageMap = new HashMap<>();
            for (Requests requests : requestsList) {
                if (requestsPercentageMap.containsKey(requests.getRequestType())) {
                    requestsPercentageMap.put(requests.getRequestType(), requestsPercentageMap.get(requests.getRequestType() + 1));
                } else {
                    requestsPercentageMap.put(requests.getRequestType(), 1.0);
                }
            }

            int sum = 0;

            for (Map.Entry<String, Double> entry : requestsPercentageMap.entrySet()) {
                sum += entry.getValue();
            }

            for (Map.Entry<String, Double> entry : requestsPercentageMap.entrySet()) {
                entry.setValue(entry.getValue() / sum);
            }

            return requestsPercentageMap;
        }
    }
}
