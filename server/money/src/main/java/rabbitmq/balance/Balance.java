package rabbitmq.balance;

import com.google.gson.JsonObject;
import gameplay.GamePlay;
import jetty.EndPointReply;
import memory.MemoryCache;

import java.math.BigDecimal;
import static common.Functions.trace;

public class Balance {
    private JsonObject getUser(String userId){
        return MemoryCache.getUser(userId);
    }

    public EndPointReply createUser(JsonObject obj){
        EndPointReply ep = new EndPointReply();
        String id = obj.get("id").getAsString();
        JsonObject balance = obj.get("balance").getAsJsonObject();
        MemoryCache.addUser(id, balance);
        ep.success = true;
        return ep;
    }

    public EndPointReply getBalance(String userId){
        EndPointReply ep    = new EndPointReply();
        JsonObject user     = getUser(userId);
        if (user == null) return ep;

        ep.data.add("balance", user.get("balance").getAsJsonObject());
        ep.success = true;

        return ep;
    }

    public EndPointReply game_started(GamePlay gamePlay){
        return operateOnBalance(gamePlay, "subtract");
    }
    public EndPointReply gameWon(GamePlay gamePlay) {

        EndPointReply ep = operateOnBalance(gamePlay, "add");
        ep.data.addProperty("winAmount", gamePlay.winAmount);
        return ep;
    }

    public EndPointReply operateOnBalance(GamePlay gamePlay, String operation){
        EndPointReply ep        = new EndPointReply();
        JsonObject user         = getUser(gamePlay.userId);
        if (user == null) return ep;

        JsonObject balanceObj   = user.get("balance").getAsJsonObject();
        BigDecimal balance      = balanceObj.get("gold").getAsBigDecimal();
        BigDecimal betAmount    = gamePlay.betAmount;

        if (betAmount.compareTo(BigDecimal.ZERO) <=0) return ep;

        BigDecimal newBalance = BigDecimal.ZERO;
        switch (operation) {
            case "add":
                newBalance = balance.add( gamePlay.winAmount );
                break;

            case "subtract":
                newBalance = balance.subtract( gamePlay.betAmount );
                break;
        }

        if (newBalance.compareTo(BigDecimal.ZERO) <= 0) return ep;

        balanceObj.addProperty("gold", newBalance);
        user.add("balance", balanceObj);
        MemoryCache.updateInfo(user);

        ep.data.addProperty("balance", newBalance);
        ep.success = true;

        return ep;
    }
}
