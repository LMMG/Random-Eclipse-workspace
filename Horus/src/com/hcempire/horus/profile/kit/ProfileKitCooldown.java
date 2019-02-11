package com.hcempire.horus.profile.kit;

import com.hcempire.horus.profile.kit.ability.ProfileKitAbility;
import com.hcempire.horus.util.DateUtil;
import lombok.Getter;

import java.text.DecimalFormat;

public class ProfileKitCooldown {

    private static final DecimalFormat SECONDS_FORMATTER = new DecimalFormat("#0.0");

    @Getter private final ProfileKitAbility ability;
    private final long duration;
    private final long createdAt;

    public ProfileKitCooldown(ProfileKitAbility ability, long duration) {
        this.ability = ability;
        this.duration = duration * 1000;
        this.createdAt = System.currentTimeMillis();
    }

    public boolean isFinished() {
        return ((createdAt + duration) - System.currentTimeMillis()) <= 0;
    }

    public String getTimeLeft() {
        if (((createdAt + duration) - System.currentTimeMillis()) >= 60000) {
            return DateUtil.readableTime((createdAt + duration) - System.currentTimeMillis()).trim();
        } else {
            return SECONDS_FORMATTER.format((((createdAt + duration) - System.currentTimeMillis()) / 1000.0f));
        }
    }


}
