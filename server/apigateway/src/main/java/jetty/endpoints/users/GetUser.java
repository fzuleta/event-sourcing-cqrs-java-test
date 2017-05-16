package jetty.endpoints.users;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import common.StaticReferences;
import jetty.EndPoint;
import jetty.EndPointReply;

import java.time.LocalDateTime;

import static common.Functions.trace;

public class GetUser extends EndPoint {
    protected void doAction(Gson gson, JsonObject obj, EndPointReply endPointReply, LocalDateTime utcTime) throws Exception {


        JsonObject rabbitMessage = new JsonObject();
        rabbitMessage.addProperty("event", "get_a_random_user");

        trace("Getting a random user");

        EndPointReply epUser = gson.fromJson(
                StaticReferences.rabbitMQ.call("user.user", rabbitMessage.toString()), EndPointReply.class);

        trace(epUser.data);

        JsonObject user = epUser.data;
        rabbitMessage.addProperty("event", "get_balance");
        rabbitMessage.addProperty("userId", user.get("id").getAsString());

        EndPointReply epBalance = gson.fromJson(
                StaticReferences.rabbitMQ.call("user.money", rabbitMessage.toString()), EndPointReply.class);


        user.add("balance", epBalance.data.get("balance").getAsJsonObject());

        endPointReply.data = user;
        endPointReply.success = epBalance.success;
    }
}
