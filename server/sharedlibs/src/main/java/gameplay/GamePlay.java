package gameplay;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.math.BigDecimal;
import java.util.UUID;

public class GamePlay {
    public GamePlayState state = GamePlayState.STARTED;

    public String id = UUID.randomUUID().toString();
    public String userId;
    public BigDecimal balance = BigDecimal.ZERO;
    public BigDecimal betAmount = BigDecimal.ZERO;
    public BigDecimal winAmount = BigDecimal.ZERO;

    public GamePlay(String userId, BigDecimal balance, BigDecimal betAmount){
        this.userId = userId;
        this.betAmount = betAmount;
        this.balance = balance;
    }
    public void setBalance(BigDecimal amount) {
        balance = amount;
    }
    public void balanceDeducted() {
        state = GamePlayState.BALANCE_DEDUCTED;
    }
    public void userNotFound() {
        state = GamePlayState.USER_NOT_FOUND;
    }
    public void notEnoughBalance() {
        state = GamePlayState.NOT_ENOUGH_BALANCE;
    }
    public void finish() { state = GamePlayState.FINISHED; }

    public enum GamePlayState {
        USER_NOT_FOUND,
        STARTED,
        BALANCE_DEDUCTED,
        NOT_ENOUGH_BALANCE,
        FINISHED
    }
    public String toString() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();
        return gson.toJson(this, GamePlay.class);
    }
    public JsonObject toJsonObject() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();
        return gson.fromJson( toString(), JsonObject.class);
    }
}
