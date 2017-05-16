package rabbitmq;

import com.google.gson.*;
import common.Functions;
import common.StaticReferences;
import entities.User;
import gameplay.GamePlay;
import jetty.EndPoint;
import jetty.EndPointReply;
import memory.MemoryCache;
import rabbitmq.balance.Balance;

import static common.Functions.trace;

public class RabbitMQBalance extends RabbitMQ {
    private String queue = "user.money";
    protected void doInit() throws Exception {
        System.out.println(" [x] Awaiting RPC requests");

        createQueue(channel, queue, false, false, false);
        createRPCConsumer(queue, this::parseMoneyMessages);

        restoreState();
    }

    private void restoreState() throws Exception {
        Gson gson = new GsonBuilder()  .disableHtmlEscaping() .serializeNulls() .create();
        JsonObject rabbitMessage = new JsonObject();

        /*
        Currently we are asking for the entire stack of messages
        but this is not practical, snapshots that load a file
        are a much better approach
        */

        JsonArray eventList = new JsonArray();
        eventList.add("user_created");

        rabbitMessage.addProperty("event", "get_events_from_start_of_time");
        rabbitMessage.add("eventList", eventList);
        EndPointReply resp = gson.fromJson( StaticReferences.rabbitMQ.call("eventstore", rabbitMessage.toString()), EndPointReply.class);

        if (!resp.success) {
            Functions.logger.error("Can't get state up to date");
            doStop();
            return;
        }
        JsonArray events = resp.data.get("events").getAsJsonArray();

        for (JsonElement evt: events) {
            if (evt.isJsonObject()) {
                JsonObject e = evt.getAsJsonObject();
                String event = e.get("event").getAsString();
                switch (event){
                    case "user_created":
                        // only add the data we need
                        String id           = e.get("data").getAsJsonObject().get("id").getAsString();
                        JsonObject info     = new JsonObject();
                        info.addProperty("id", id);
                        info.add("balance", e.get("data").getAsJsonObject().get("balance").getAsJsonObject());
                        MemoryCache.addUser(id, info);
                        trace("adding user: " + id);
                        break;
                }
            }
        }
    }

    protected EndPointReply parseMoneyMessages(String message) {
        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .serializeNulls()
                .create();

        EndPointReply endPointReply;

        JsonObject obj = gson.fromJson(message, JsonObject.class);
        String event = obj.has("event") ? obj.get("event").getAsString() : "";

        System.out.println(" [.] " + obj.toString() + "");

        Balance balance = new Balance();
        GamePlay gamePlay;
        switch (event){
            case "create_user":
                endPointReply = balance.createUser(obj);
                break;

            case "get_balance":
                endPointReply = balance.getBalance(obj.get("userId").getAsString());
                break;

            case "game_started":
                gamePlay = gson.fromJson(obj.get("gamePlay").getAsJsonObject().toString(), GamePlay.class);
                endPointReply = balance.game_started(gamePlay);
                break;

            case "game_won":
                gamePlay = gson.fromJson(obj.get("gamePlay").getAsJsonObject().toString(), GamePlay.class);
                endPointReply = balance.gameWon(gamePlay);
                break;

            default:
                endPointReply = new EndPointReply();
        }

        return endPointReply;
    }
}
