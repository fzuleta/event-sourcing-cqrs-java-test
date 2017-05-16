package jetty.endpoints.play;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import common.StaticReferences;
import entities.EventToStore;
import gameplay.GamePlay;
import jetty.EndPoint;
import jetty.EndPointReply;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static common.Functions.trace;

public class Play extends EndPoint {

    protected void doAction(Gson gson, JsonObject obj, EndPointReply endPointReply, LocalDateTime utcTime) throws Exception {
        String userId        = obj.has("userId") && !obj.get("userId").isJsonNull() ? obj.get("userId").getAsString(): null;
        BigDecimal betAmount;
        try {
            betAmount = obj.has("betAmount") && !obj.get("betAmount").isJsonNull() ? obj.get("betAmount").getAsBigDecimal() : BigDecimal.ZERO;
        } catch (Exception e) {
            betAmount = BigDecimal.ZERO;
        }
        if (betAmount.compareTo(BigDecimal.ZERO) <= 0) return;

        // create a gamePlay
        GamePlay gamePlay = new GamePlay(userId, BigDecimal.ZERO, betAmount);

        // prepare the rabbit message
        JsonObject rabbitMessage = new JsonObject();
        EndPointReply deductBalanceResp;
        EndPointReply increaseBalanceResp;

        // start the game
        rabbitMessage.addProperty("event", "game_started");
        rabbitMessage.add("gamePlay", gamePlay.toJsonObject());
        deductBalanceResp = gson.fromJson( StaticReferences.rabbitMQ.call("user.money", rabbitMessage.toString()), EndPointReply.class);

        endPointReply.success   = deductBalanceResp.success;
        endPointReply.data      = deductBalanceResp.data;
        if (!deductBalanceResp.success) {
            gamePlay.notEnoughBalance();
            return;
        }

        gamePlay.setBalance(deductBalanceResp.data.get("balance").getAsBigDecimal());
        gamePlay.balanceDeducted();

        //play
        boolean win = Math.random() > 0.5;

        if (win) {
            gamePlay.winAmount = betAmount.multiply(new BigDecimal(2));
            rabbitMessage.addProperty("event", "game_won");
            rabbitMessage.add("gamePlay", gamePlay.toJsonObject());
            increaseBalanceResp = gson.fromJson(StaticReferences.rabbitMQ.call("user.money", rabbitMessage.toString()), EndPointReply.class);
            endPointReply.data = increaseBalanceResp.data;

            gamePlay.setBalance(increaseBalanceResp.data.get("balance").getAsBigDecimal());
        }

        gamePlay.finish();

        JsonObject event = new JsonObject();
        event.addProperty("event", "user_played");
        event.add("gamePlay", gamePlay.toJsonObject());


        // EVENTSTORE user_played
        new EventToStore("user_played", gamePlay.toJsonObject()).storeEvent(StaticReferences.rabbitMQ);


    }
}
