package entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class User {
    public String id = "";
    public String name = "";
    public String email = "";
    public String password = "";
    public JsonObject balance = new JsonObject();

    public User(String id, String name, String email, String password, JsonObject balance){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }

    public String toString() {
        Gson gson = new GsonBuilder()  .disableHtmlEscaping() .serializeNulls() .create();
        return gson.toJson(this, User.class);
    }
    public JsonObject toJsonObject() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();
        return gson.fromJson( toString(), JsonObject.class);
    }
}
