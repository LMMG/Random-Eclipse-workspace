package us.kirai.bunkers.game.managers;

import java.util.*;

import us.kirai.bunkers.Bunkers;
import us.kirai.bunkers.game.*;

public class DTRManager
{
    public HashMap<Team, Double> dtr;
    public static double STARTING_DTR = 5.0;
    
    public DTRManager() {
        this.dtr = new HashMap<Team, Double>();
    }
    
    public double getDTR(Team p) {
        return this.dtr.containsKey(p) ? this.dtr.get(p) : 5.0;
    }
    
    public String getDTRFormatted(Team t) {
        double dtr = this.getDTR(t);
        if (dtr <= 0.0) {
            return "§4§lRAIDABLE";
        }
        if (dtr >= 5.0) {
            return "§a§l" + dtr;
        }
        if (dtr <= 2.0 && dtr > 1.0) {
            return "§e§l" + dtr;
        }
        if (dtr <= 1.0) {
            return "§c§l" + dtr;
        }
        return "§2§l" + dtr;
    }
    
    public boolean isRaidable(Team t) {
        return this.getDTR(t) <= 0.0;
    }
    
    public void addDTR(Team p, double dtr) {
        double oldBal = this.getDTR(p);
        if (this.dtr.containsKey(p)) {
            this.dtr.remove(p);
        }
        this.dtr.put(p, oldBal + dtr);
    }
    
    public void removeDTR(Team p, double dtr) {
        double oldBal = this.getDTR(p);
        if (oldBal - dtr < 0.0) {
            return;
        }
        if (this.dtr.containsKey(p)) {
            this.dtr.remove(p);
        }
        this.dtr.put(p, oldBal - dtr);
    }
    
    
    
    public void setDTR(Team p, double dtr) {
        if (dtr < 0.0) {
            return;
        }
        if (this.dtr.containsKey(p)) {
            this.dtr.remove(p);
        }
        this.dtr.put(p, dtr);
    }
}
