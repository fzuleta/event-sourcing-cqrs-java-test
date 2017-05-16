package entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.MessageProperties;
import common.StaticReferences;
import rabbitmq.RabbitMQ;

public class EventToStore {
    public String event = "";
    public JsonObject data = new JsonObject();
    public Long date = 0l;
    public boolean published = true;

    public EventToStore(String event, JsonObject data){
        this.event = event;
        this.data = data;
    }

    public void storeEvent(RabbitMQ rabbit) {
        JsonObject rabbitEventMessage = new JsonObject();
        rabbitEventMessage.addProperty("event", "store_event");
        rabbitEventMessage.add("eventToStore", toJsonObject());
        try {
            rabbit.basicPublish("", "eventstore", null, rabbitEventMessage.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        Gson gson = new GsonBuilder()  .disableHtmlEscaping() .serializeNulls() .create();
        return gson.toJson(this, EventToStore.class);
    }
    public JsonObject toJsonObject() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();
        return gson.fromJson( toString(), JsonObject.class);
    }
}
