package jetty;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class EndPointReply {
    public boolean success = false;
    public String errorDesc = "";
    public int errorCode = 0;
    public JsonObject data =  new JsonObject();


    public String toString() {
        Gson gson = new GsonBuilder()  .disableHtmlEscaping() .serializeNulls() .create();
        return gson.toJson(this, EndPointReply.class);
    }
}
