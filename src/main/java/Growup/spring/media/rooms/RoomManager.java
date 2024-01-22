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

package Growup.spring.media.rooms;

import com.google.gson.JsonObject;
import org.kurento.client.KurentoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Ivan Gracia (izanmail@gmail.com)
 * @since 4.3.1
 */
public class RoomManager {

    private final Logger log = LoggerFactory.getLogger(RoomManager.class);

    @Autowired
    private KurentoClient kurento;

    /**
     * key : presenter Id, value : Room
     */
    private final ConcurrentMap<String, Room> rooms = new ConcurrentHashMap<>();

    /**
     * Looks for a room in the active room list.
     *
     * @param roomId
     *          the name of the room
     * @return the room if it was already created, or a new one if it is the first time this room is
     *         accessed
     */
    public Room getRoom(String roomId) {
        log.debug("Searching for room {}", roomId);
        Room room = rooms.get(roomId);

        if (room == null)
            log.debug("Room {} not existent.", roomId);
        else
            log.debug("Room {} found!", roomId);
        return room;
    }

    public void createRoom(WebSocketSession session, JsonObject jsonMessage) throws Exception {
        System.out.println("hello createRoom");
        log.debug("Creating new room {}", session.getId());
        String roomId = jsonMessage.get("roomId").getAsString();
        if(rooms.containsKey(roomId))
            throw new Exception("이미 존재하는 RoomId입니다.");
        Room room = new Room(session, jsonMessage, kurento.createMediaPipeline());
        rooms.put(roomId, room);
        log.debug("Room {} created!", roomId);
        System.out.println(rooms);
    }

    public Collection<Room> getRooms(){
        return rooms.values();
    }

    public void removeRoom(String roomId) {
        rooms.remove(roomId);
    }
}
