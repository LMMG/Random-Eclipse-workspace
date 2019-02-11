package com.hcempire.horus.profile.cooldown;

import com.hcempire.horus.Horus;

public enum ProfileCooldownType {
    ENDER_PEARL,
    SPAWN_TAG,
    LOGOUT;

    private static Horus main = Horus.getInstance();

    public int getDuration() {
        return main.getConfigFile().getInt("COOLDOWN." + name());
    }

    public String getMessage() {
        return main.getLangFile().getString("COOLDOWN." + name());
    }

}
