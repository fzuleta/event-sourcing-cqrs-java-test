package orientdb;

import com.google.gson.*;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import common.Functions;
import common.StaticRef;
import common.StaticReferences;
import entities.EventToStore;
import entities.User;
import jetty.EndPointReply;
import rabbitmq.RabbitMQEventStore;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static common.Functions.trace;

public class ODBEventStore extends ODBManager {
    public String rabbit_host = "localhost";

    public ODBEventStore(){
        odb_db                  = "events";
        classToCheckIfDBExists  = "Events";
        port                    = "11113";
        portRange               = "11113";
    }
    protected void initSteps(){
        db_action((OrientGraph graph)->{
            // start the rabbit
            StaticRef.rabbitMQ = new RabbitMQEventStore();
            StaticRef.rabbitMQ.host = rabbit_host;
            StaticRef.rabbitMQ.startAsync();

            return true;
        });
    }

    public void createDummyUsers() throws Exception {

        db_action((OrientGraph graph)-> {
            Iterable<Vertex> iv = graph.getVerticesOfClass(classToCheckIfDBExists);
            if (!iv.iterator().hasNext()) {
                for (int i = 0; i < 10; i++) {
                    JsonObject balance = new JsonObject();
                    balance.addProperty("gold", 10000);
                    User user = new User(
                            UUID.randomUUID().toString(),
                            i + "_" + Functions.generateString(new Random(), "qwertyuiopasdfghjklzxcvbnm", 10),
                            Functions.generateString(new Random(), "qwertyuiopasdfghjklzxcvbnm", 3) + "@" + Functions.generateString(new Random(), "qwertyuiopasdfghjklzxcvbnm", 3) + ".com",
                            "123456",
                            balance
                    );

                    JsonObject rabbitMessage = new JsonObject();
                    rabbitMessage.addProperty("event", "create_user");
                    rabbitMessage.add("user", user.toJsonObject());

                    new EventToStore("user_created", user.toJsonObject()).storeEvent(StaticRef.rabbitMQ);
                }
            }
            return true;
        });
    }

    public EndPointReply storeEvent(JsonObject obj){
        EndPointReply ep = new EndPointReply();
        Gson gson = new GsonBuilder()  .disableHtmlEscaping() .serializeNulls() .create();

        final EventToStore event = gson.fromJson(obj.get("eventToStore").getAsJsonObject().toString(), EventToStore.class);
        final long theTime = LocalDateTime.now(ZoneOffset.UTC).toEpochSecond(ZoneOffset.UTC);

        trace("Storing event: " + event.event);
        ep.success = db_action((OrientGraph graph)->{
            OrientVertex oEvent = graph.addVertex("class:Events");
            oEvent.setProperty("event", event.event);
            oEvent.setProperty("data", event.data.toString());
            oEvent.setProperty("date", theTime);
            oEvent.setProperty("published", event.published);

            graph.commit();

           return true;
        });

        return ep;
    }

    public EndPointReply getEventsFromStartOfTime(JsonObject obj){
        EndPointReply ep = new EndPointReply();
        Gson gson = new GsonBuilder()  .disableHtmlEscaping() .serializeNulls() .create();
        final JsonArray eventList = obj.has("eventList") && !obj.get("eventList").isJsonNull() ? obj.get("eventList").getAsJsonArray() : null;
        if (eventList == null) return ep;

        final JsonArray list = new JsonArray();
        ep.success = db_action((OrientGraph graph)->{
            Iterable<Vertex> iv = graph.getVerticesOfClass("Events");

            for (Vertex v: iv) {
                JsonElement event_name = new JsonPrimitive(v.getProperty("event").toString());
                if (eventList.contains(event_name)) {
                    JsonObject eventToStore = new JsonObject();
                    eventToStore.addProperty("event", v.getProperty("event").toString());
                    eventToStore.add("data", gson.fromJson(v.getProperty("data").toString(), JsonObject.class));
                    eventToStore.addProperty("date", (long) v.getProperty("date"));
                    eventToStore.addProperty("published", (boolean) v.getProperty("published"));
                    list.add(eventToStore);
                }
            }
            return true;
        });

        ep.data.add("events", list);
        return ep;
    }
}
