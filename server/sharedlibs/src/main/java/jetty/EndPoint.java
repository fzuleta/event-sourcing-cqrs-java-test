package jetty;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import common.Functions;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class EndPoint extends HttpServlet {
    public boolean showTrace = false;
    public String myEndPointName = "";

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);

        /*  Current time */
        LocalDateTime utcTime = LocalDateTime.now(ZoneOffset.UTC);

        EndPointReply endPointReply = new EndPointReply();

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .serializeNulls()
                .create();

        String str_request = IOUtils.toString(req.getInputStream(), "UTF-8");

        try {
            if (str_request == null || str_request.trim().length()==0){
                str_request = "{}";
            }

            JsonObject obj = gson.fromJson(str_request, JsonObject.class);

            this.doAction(gson, obj, endPointReply, utcTime);


        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            String str_response = gson.toJson(endPointReply, EndPointReply.class);

            if (showTrace) {
                Functions.trace(myEndPointName + "\n" + str_response);
            }

            resp.getOutputStream().print( str_response );

        }
    }

    protected void doAction(Gson gson, JsonObject obj, EndPointReply endPointReply, LocalDateTime utcTime) throws Exception {
        //All endpoints replace this
    }
}

