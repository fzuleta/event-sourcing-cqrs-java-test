package memory;

import com.google.gson.JsonObject;

import java.util.concurrent.ConcurrentHashMap;

public class MemoryCache {
    public static ConcurrentHashMap<String, JsonObject> users = new ConcurrentHashMap<>();

    public static void addUser(String id, JsonObject info) {
        users.put(id, info);
    }
    public static JsonObject getUser(String id) {
        if (id == null || id.trim().length() == 0) return null;
        return users.get(id);
    }
    public static JsonObject updateInfo(JsonObject info) {
        return users.put(info.get("id").getAsString(), info);
    }
}
