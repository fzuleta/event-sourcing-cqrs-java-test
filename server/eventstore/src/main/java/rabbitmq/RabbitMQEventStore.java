package rabbitmq;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import common.StaticRef;
import jetty.EndPointReply;

public class RabbitMQEventStore extends RabbitMQ {

    protected void doInit() throws Exception {
        System.out.println(" [x] Awaiting RPC requests");

        createQueue(channel, "eventstore", false, false, false);
        createRPCConsumer("eventstore", this::parseEventStoreMessages);

        // create some dummy users
        try {
            StaticRef.odbManager.createDummyUsers();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    protected EndPointReply parseEventStoreMessages(String message) {
        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .serializeNulls()
                .create();

        EndPointReply endPointReply;

        JsonObject obj = gson.fromJson(message, JsonObject.class);
        String event = obj.has("event") ? obj.get("event").getAsString() : "";

        System.out.println(" [.] " + obj.toString() + "");

        switch (event){
            case "get_events_from_start_of_time":
                endPointReply = StaticRef.odbManager.getEventsFromStartOfTime(obj);
                break;
            case "store_event":
                endPointReply = StaticRef.odbManager.storeEvent(obj);
                break;

            default:
                endPointReply = new EndPointReply();
        }

        return endPointReply;
    }
}
