package com.bizarrealex.aether.scoreboard.cooldown;

import com.bizarrealex.aether.scoreboard.Board;
import com.bizarrealex.aether.scoreboard.cooldown.BoardFormat;
import java.text.DecimalFormat;
import java.util.Set;
import org.apache.commons.lang.time.DurationFormatUtils;

public class BoardCooldown {
    private static final DecimalFormat SECONDS_FORMATTER = new DecimalFormat("#0.0");
    private final Board board;
    private final String id;
    private final double duration;
    private final long end;

    public BoardCooldown(Board board, String id, double duration) {
        this.board = board;
        this.id = id;
        this.duration = duration;
        this.end = (long)((double)System.currentTimeMillis() + duration * 1000.0);
        board.getCooldowns().add(this);
    }

    public String getFormattedString(BoardFormat format) {
        if (format == null) {
            throw new NullPointerException();
        }
        if (format == BoardFormat.SECONDS) {
            return SECONDS_FORMATTER.format((float)(this.end - System.currentTimeMillis()) / 1000.0f);
        }
        return DurationFormatUtils.formatDuration((long)(this.end - System.currentTimeMillis()), (String)"mm:ss");
    }

    public void cancel() {
        this.board.getCooldowns().remove(this);
    }

    public Board getBoard() {
        return this.board;
    }

    public String getId() {
        return this.id;
    }

    public double getDuration() {
        return this.duration;
    }

    public long getEnd() {
        return this.end;
    }
}

