package com.hcempire.horus.profile.cooldown;

import com.hcempire.horus.Horus;
import com.hcempire.horus.profile.kit.ability.ProfileKitAbility;
import com.hcempire.horus.util.DateUtil;
import lombok.Getter;
import org.apache.commons.lang.time.DurationFormatUtils;

import java.text.DecimalFormat;

public class ProfileCooldown {

    private static final DecimalFormat SECONDS_FORMATTER = new DecimalFormat("#0.0");

    @Getter private final ProfileCooldownType type;
    private final long duration;
    private final long createdAt;

    public ProfileCooldown(ProfileCooldownType type, long duration) {
        this.type = type;
        this.duration = duration * 1000;
        this.createdAt = System.currentTimeMillis();
    }

    public boolean isFinished() {
        return ((createdAt + duration) - System.currentTimeMillis()) <= 0;
    }

    public String getTimeLeft() {
        if (((createdAt + duration) - System.currentTimeMillis()) >= 60000 || type == ProfileCooldownType.SPAWN_TAG) {
            return DurationFormatUtils.formatDuration(createdAt + duration - System.currentTimeMillis(), "mm:ss");
        } else {
            return SECONDS_FORMATTER.format((((createdAt + duration) - System.currentTimeMillis()) / 1000.0f)) + "s";
        }
    }

}
