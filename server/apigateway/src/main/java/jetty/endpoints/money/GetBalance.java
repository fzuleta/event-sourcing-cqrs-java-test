package jetty.endpoints.money;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import common.StaticReferences;
import jetty.EndPoint;
import jetty.EndPointReply;

import java.time.LocalDateTime;

import static common.Functions.trace;

public class GetBalance extends EndPoint {
    protected void doAction(Gson gson, JsonObject obj, EndPointReply endPointReply, LocalDateTime utcTime) throws Exception {

        String userId = obj.has("userId") && !obj.get("userId").isJsonNull() ? obj.get("userId").getAsString(): null;

        JsonObject rabbitMessage = new JsonObject();
        rabbitMessage.addProperty("event", "get_balance");
        rabbitMessage.addProperty("userId", userId);

        EndPointReply ep = gson.fromJson(
                StaticReferences.rabbitMQ.call("user.money",
                        rabbitMessage.toString()), EndPointReply.class);

        endPointReply.data = ep.data;
        endPointReply.success = ep.success;
    }
}
