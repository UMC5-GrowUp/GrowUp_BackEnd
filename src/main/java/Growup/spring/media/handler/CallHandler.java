/*
 * (C) Copyright 2014 Kurento (http://kurento.org/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package Growup.spring.media.handler;

import Growup.spring.media.rooms.Room;
import Growup.spring.media.rooms.RoomManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

/**
 * Protocol handler for 1 to N video call communication.
 *
 * @author Boni Garcia (bgarcia@gsyc.es)
 * @since 5.0.0
 * @modifyBy  Jihwan Kim (fighter0628@gmail.com)
 */

public class CallHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(CallHandler.class);
    private static final Gson gson = new GsonBuilder().create();

    @Autowired
    private RoomManager roomManager;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println(session.getId());
        JsonObject jsonMessage = gson.fromJson(message.getPayload(), JsonObject.class);
        log.debug("Incoming message from session '{}': {}", session.getId(), jsonMessage);

        switch (jsonMessage.get("id").getAsString()) {
            case "presenter":
                try {
                    System.out.println("hello presenter");
                    presenter(session, jsonMessage);
                } catch (Throwable t) {
                    handleErrorResponse(t, session, "presenterResponse", jsonMessage);
                }
                break;
            case "viewer":
                try {
                    System.out.println("viewer session id : "+session.getId());
                    viewer(session, jsonMessage);
                } catch (Throwable t) {
                    handleErrorResponse(t, session, "viewerResponse", jsonMessage);
                }
                break;
            case "onIceCandidate": {
                System.out.println("hello onIceCandidate");
                System.out.println(jsonMessage);
                onIceCandidate(session, jsonMessage);
                break;
            }
            case "stop":
                stop(session, jsonMessage);
                break;
            default:
                break;
        }
    }

    private void onIceCandidate(WebSocketSession session, JsonObject jsonMessage) {

        JsonObject candidate = jsonMessage.get("candidate").getAsJsonObject();
        String roomId = jsonMessage.get("roomId").getAsString();

        Room room = roomManager.getRoom(roomId);
        room.addIceCandidate(session, candidate);
    }

    private void handleErrorResponse(Throwable throwable, WebSocketSession session, String responseId, JsonObject jsonMessage)
            throws IOException {

        stop(session, jsonMessage);
        log.error(throwable.getMessage(), throwable);
        JsonObject response = new JsonObject();
        response.addProperty("id", responseId);
        response.addProperty("response", "rejected");
        response.addProperty("message", throwable.getMessage());
        session.sendMessage(new TextMessage(response.toString()));
    }

    private synchronized void viewer(final WebSocketSession session, JsonObject jsonMessage)
            throws IOException {
        final String roomId = jsonMessage.get("roomId").getAsString();
        final String sessionId = session.getId();
        log.info("PARTICIPANT {}: trying to join room {}", sessionId, roomId);
        System.out.println("viewer session id : "+session.getId());
        Room room = roomManager.getRoom(roomId);
        room.joinViewer(session, jsonMessage);
    }

    private synchronized void presenter(final WebSocketSession session, JsonObject jsonMessage)
            throws Exception {
        final String sessionId = session.getId();
        log.info("PARTICIPANT {}: trying to create room", sessionId);
        System.out.println("presenter session id : "+session.getId());
        roomManager.createRoom(session, jsonMessage);
    }

    private synchronized void stop(WebSocketSession session, JsonObject jsonMessage) throws IOException {
        final String roomId = jsonMessage.get("roomId").getAsString();

        Room room = roomManager.getRoom(roomId);
        room.stop(session);
        roomManager.removeRoom(roomId);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session,@NonNull  CloseStatus status) throws Exception {
        System.out.println("connection closed");
        for(Room room : roomManager.getRooms())
            if(room.contains(session))
                room.stop(session);
    }
}
