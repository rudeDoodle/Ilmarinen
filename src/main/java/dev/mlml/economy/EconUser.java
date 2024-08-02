package dev.mlml.economy;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class EconUser {
    private static final Logger logger = LoggerFactory.getLogger(EconUser.class);

    @Serialize
    private float money = 0;

    public void addMoney(float amount) {
        money += amount;
    }

    @Serialize
    private int games = 0;
    @Serialize
    private int wins = 0;
    @Serialize
    private float winnings = 0;
    @Serialize
    private float spent = 0;

    public void play(float amount) {
        games++;
        if (amount <= 0) {
            logger.warn("Tried to play with a negative amount");
            return;
        }
        spent += amount;
    }

    public void win(float amount) {
        wins++;
        if (amount <= 0) {
            logger.warn("Tried to win with a negative amount");
            return;
        }
        winnings += amount;
    }

    public float getWinRate() {
        return (float) wins / games;
    }

    public float getProfit() {
        return winnings - spent;
    }

    @Serialize
    private final String id;

    public EconUser(String id) {
        this.id = id;
    }
}
