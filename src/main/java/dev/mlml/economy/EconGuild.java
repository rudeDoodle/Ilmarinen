package dev.mlml.economy;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class EconGuild {
    private static final Logger logger = LoggerFactory.getLogger(EconGuild.class);

    private int games;
    private int wins;
    private float winnings;
    private float spent;

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

    private final String id;

    public EconGuild(String id) {
        this.id = id;
    }
}
