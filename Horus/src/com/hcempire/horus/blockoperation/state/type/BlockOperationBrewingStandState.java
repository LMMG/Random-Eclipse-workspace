package com.hcempire.horus.blockoperation.state.type;

import com.hcempire.horus.Horus;
import com.hcempire.horus.blockoperation.state.BlockOperationState;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.block.BrewingStand;

public class BlockOperationBrewingStandState implements BlockOperationState {

    private static Horus main = Horus.getInstance();
    private static final String NAME = "BlockOperationBrewingStandState";

    @Getter @Setter private BrewingStand brewingStand;

    public BlockOperationBrewingStandState(BrewingStand brewingStand) {
        this.brewingStand = brewingStand;
    }

    @Override
    public Location getLocation() {
        return brewingStand.getLocation();
    }

    @Override
    public int getIncrease() {
        return main.getConfigFile().getInt("BLOCK_MODIFIER.BREWING_STAND_BREW_INCREASE");
    }

    @Override
    public String getName() {
        return NAME;
    }
}
