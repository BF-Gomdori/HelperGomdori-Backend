package bf.multi.server.domain.dto.firebase;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FCMMessageDto {
    private boolean validate_only;
    private Message message;

    @Builder
    public FCMMessageDto(boolean validate_only, Message message) {
        this.validate_only = validate_only;
        this.message = message;
    }

    @Getter
    @NoArgsConstructor
    public static class Message {
        private Notification notification; // 모든 mobile os를 아우를수 있는 Notification
        private String token; // 특정 device에 알림을 보내기위해 사용

        @Builder
        public Message(Notification notification, String token) {
            this.notification = notification;
            this.token = token;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Notification {
        private String title;
        private String body;
        private String image;

        @Builder
        public Notification(String title, String body, String image) {
            this.title = title;
            this.body = body;
            this.image = image;
        }
    }
}
