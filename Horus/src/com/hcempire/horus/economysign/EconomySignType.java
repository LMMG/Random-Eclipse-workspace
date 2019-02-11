package com.hcempire.horus.economysign;

import com.hcempire.horus.Horus;

import java.util.List;

public enum EconomySignType {
    BUY,
    SELL;

    private static Horus main = Horus.getInstance();

    public List<String> getSignText() {
        return main.getLangFile().getStringList("ECONOMY.SIGN." + name() + "_TEXT");
    }

}
