package bf.multi.server.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class GeoService {
    @Value("${naver-map.X-NCP-APIGW-API-KEY-ID}")
    String clientId;

    @Value("${naver-map.X-NCP-APIGW-API-KEY}")
    String clientSecret;

    /**
     * https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc
     * ?request=coordsToaddr
     * &coords=129.1133567,35.2982640
     * &sourcecrs=epsg:4326
     * &output=json
     * &orders=roadaddr
     */
    public String reverseGeocoding(Double y, Double x) {
        String location = "";
        try {
            String apiURL = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc"
                    + "?request=coordsToaddr"
                    + "&coords=" + y.toString() + "," + x.toString()
                    + "&sourcecrs=epsg:4326&output=json&orders=addr,roadaddr";

            // HTTP Header 생성
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-NCP-APIGW-API-KEY-ID", clientId);
            headers.add("X-NCP-APIGW-API-KEY", clientSecret);

            // HTTP 요청 보내기
            HttpEntity<MultiValueMap<String, String>> reverseGeocode = new HttpEntity<>(headers);
            RestTemplate rt = new RestTemplate();
            ResponseEntity<String> response = rt.exchange(
                    apiURL,
                    HttpMethod.GET,
                    reverseGeocode,
                    String.class
            );
            // responseBody 정보 꺼내기
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            if (!jsonNode.get("results").has(1)) { // roadaddr 없으면 지번주소로 가져오기
                location += jsonNode.get("results").get(0).get("region").get("area1").get("name").asText() + " ";
                location += jsonNode.get("results").get(0).get("region").get("area2").get("name").asText() + " ";
                location += jsonNode.get("results").get(0).get("region").get("area3").get("name").asText() + " ";
                location += jsonNode.get("results").get(0).get("land").get("number1").asText() + " ";
                location += jsonNode.get("results").get(0).get("land").get("number2").asText();
            } else { // addr, roadaddr 둘 다 있을 때 -> 도로명 주소로 가져옴
                location += jsonNode.get("results").get(1).get("region").get("area1").get("name").asText() + " ";
                location += jsonNode.get("results").get(1).get("region").get("area2").get("name").asText() + " ";
                location += jsonNode.get("results").get(1).get("land").get("name").asText() + " ";
                location += jsonNode.get("results").get(1).get("land").get("number1").asText() + " ";
                location += jsonNode.get("results").get(1).get("land").get("number2").asText();
            }
            return location;
        } catch (Exception e) {
            System.out.println(e);
        }
        return location;
    }
}
