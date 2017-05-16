package memory;

import com.google.gson.JsonObject;
import common.Functions;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static common.Functions.trace;

public class MemoryCache {
    public static ConcurrentHashMap<String, JsonObject> users = new ConcurrentHashMap<>();

    public static void addUser(String id, JsonObject info) {
        users.put(id, info);
    }
    public static JsonObject getUser(String id) {
        if (id == null || id.trim().length() == 0) return null;
        return users.get(id);
    }
    public static JsonObject getRandomUser() {
        Set<String> myHashSet = users.keySet();
        int size = myHashSet.size();
        int item = Functions.randInt(0, size-1);
        int i = 0;
        String key = "";
        for(Object obj : myHashSet) {
            if (i == item) {
                key = (String) obj;
                break;
            }
            i++;
        }
        return users.get(key);
    }
}
