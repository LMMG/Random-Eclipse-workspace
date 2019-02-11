package com.hcempire.horus.blockoperation.state;

import com.hcempire.horus.Horus;
import org.bukkit.Location;


public interface BlockOperationState {

    String getName();
    int getIncrease();
    Location getLocation();

}
