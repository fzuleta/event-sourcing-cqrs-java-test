package user;

import com.google.gson.JsonObject;
import jetty.EndPointReply;
import memory.MemoryCache;

public class User {
    public EndPointReply createUser(JsonObject obj){
        EndPointReply ep = new EndPointReply();
        String id = obj.get("id").getAsString();
        JsonObject info = obj.get("info").getAsJsonObject();
        MemoryCache.addUser(id, info);
        ep.success = true;
        return ep;
    }
}
