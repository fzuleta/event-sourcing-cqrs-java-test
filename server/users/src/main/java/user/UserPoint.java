package user;

import com.google.gson.JsonObject;
import common.Functions;
import jetty.EndPointReply;
import memory.MemoryCache;

public class UserPoint {
    public EndPointReply createUser(JsonObject obj){
        EndPointReply ep = new EndPointReply();
        String id = obj.get("id").getAsString();
        JsonObject info = obj.get("info").getAsJsonObject();
        MemoryCache.addUser(id, info);
        ep.success = true;
        return ep;
    }
    public EndPointReply getARandomUser(){
        EndPointReply ep = new EndPointReply();
        ep.data = MemoryCache.getRandomUser();
        ep.success = true;
        return ep;
    }
}
