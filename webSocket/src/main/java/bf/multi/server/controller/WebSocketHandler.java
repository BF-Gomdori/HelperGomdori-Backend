package bf.multi.server.controller;

import bf.multi.server.common.Message;
import bf.multi.server.common.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {
    /**
     * 기존 접속 사용자의 웹소켓 세션을 전부 관리하고 있어야함.
     * 세션아이디를 key로, 세션을 value로 저장하는 map 자료 구조로 정의
     */
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    // 웹소켓 연결 시
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        var sessionId = session.getId();
        sessions.put(sessionId, session); // 1) 세션 저장

        Message message = Message.builder()
                .sender(sessionId)
                .receiver("all")
                .build();
        message.newConnect();

        sessions.values().forEach(s -> { // 2) 모든 세션에 알림
            try{
                if(!s.getId().equals(sessionId)){
                    s.sendMessage(new TextMessage(Utils.getString(message)));
                }
            }catch (Exception e){
                // TODO: throw
            }
        });
    }

    // 양방향 데이터 통신
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        Message message = Utils.getObject(textMessage.getPayload());
        message.setSender(session.getId());

        // 1) 메시지를 전달할 타켓 상대방을 찾는다.
        WebSocketSession receiver = sessions.get(message.getReceiver());

        // 2) 타켓이 존재하고 연결된 상태라면, 메시지를 전송한다.
        if (receiver != null && receiver.isOpen()) {

            receiver.sendMessage(new TextMessage(Utils.getString(message)));
        }
    }

    // 소켓 연결 종료
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        var sessionId = session.getId();

        sessions.remove(sessionId); // 1) 세션 저장소에서 연결이 끊긴 사용자를 삭제한다.

        final Message message = new Message();
        message.closeConnect();
        message.setSender(sessionId);

        sessions.values().forEach(s -> { // 2) 다른 사용자들에게, 누군가 접속이 끊겼다고 알려준다.
            try {
                s.sendMessage(new TextMessage(Utils.getString(message)));
            } catch (Exception e) {
                //TODO: throw
            }
        });
    }

    // 소켓 통신 에러
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        //TODO:
    }

}
