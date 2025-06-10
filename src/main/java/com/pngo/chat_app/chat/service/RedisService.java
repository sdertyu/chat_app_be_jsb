package com.pngo.chat_app.chat.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RedisService {

    RedisTemplate<String, String> redisTemplate;

    final String ROOM_USERS_KEY_PREFIX = "chat:room:";

    public void saveUserInConversation(String conversationId, String userId) {
        // Save user in conversation
        redisTemplate.opsForSet().add(conversationId, userId);
    }

    // Thêm người dùng vào phòng chat
    public void addUserToRoom(String roomId, String userId) {
        String key = ROOM_USERS_KEY_PREFIX + roomId + ":users";
        redisTemplate.opsForSet().add(key, userId);
    }

    // Xóa người dùng khỏi phòng chat
    public void removeUserFromRoom(String roomId, String userId) {
        String key = ROOM_USERS_KEY_PREFIX + roomId + ":users";
        redisTemplate.opsForSet().remove(key, userId);
    }

    // Lấy danh sách người trong phòng chat
    public Set<String> getUsersInRoom(String roomId) {
        String key = ROOM_USERS_KEY_PREFIX + roomId + ":users";
        return redisTemplate.opsForSet().members(key);
    }

    // Kiểm tra xem người dùng có trong phòng không
    public boolean isUserInRoom(String roomId, String userId) {
        String key = ROOM_USERS_KEY_PREFIX + roomId + ":users";
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, userId));
    }
}
