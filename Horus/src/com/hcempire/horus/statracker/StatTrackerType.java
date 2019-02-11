package com.hcempire.horus.statracker;

import com.hcempire.horus.Horus;

public enum StatTrackerType {
    WEAPON,
    ARMOR;

    private static Horus main = Horus.getInstance();

    public String getHeader() {
        return main.getLangFile().getString("STAT_TRACKER." + (this == WEAPON ? "KILLS" : "DEATHS") + ".HEADER");
    }

    public String getLine() {
        return main.getLangFile().getString("STAT_TRACKER." + (this == WEAPON ? "KILLS" : "DEATHS") + ".LINE");
    }

}
