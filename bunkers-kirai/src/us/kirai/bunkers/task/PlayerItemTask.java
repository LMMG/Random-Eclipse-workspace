package us.kirai.bunkers.task;

import org.bukkit.entity.*;

import us.kirai.bunkers.*;

public class PlayerItemTask implements DynamicTask
{
    private Player player;
    
    public PlayerItemTask(final Player p) {
        this.player = p;
    }
    
    @Override
    public void execute() {
        Bunkers.getInstance().getItemManager().giveStarterItems(this.player);
    }
}
