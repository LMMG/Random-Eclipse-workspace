package com.hcempire.horus.blockoperation.state.type;

import com.hcempire.horus.Horus;
import com.hcempire.horus.blockoperation.state.BlockOperationState;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.block.Furnace;

public class BlockOperationFurnaceState implements BlockOperationState {

    private static Horus main = Horus.getInstance();
    private static final String NAME = "BlockOperationFurnaceState";

    @Getter @Setter private Furnace furnace;

    public BlockOperationFurnaceState(Furnace furnace) {
        this.furnace = furnace;
    }

    @Override
    public Location getLocation() {
        return furnace.getLocation();
    }

    @Override
    public int getIncrease() {
        return main.getConfigFile().getInt("BLOCK_MODIFIER.FURNACE_COOK_INCREASE");
    }

    @Override
    public String getName() {
        return NAME;
    }
}
