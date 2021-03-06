package org.ipvp.hcf.sotw;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.ipvp.hcf.HCF;

public class SotwTimer {

    @Getter
    private SotwRunnable sotwRunnable;

    HCF plugin;

    public boolean cancel() {
        if (this.sotwRunnable != null) {
            this.sotwRunnable.cancel();
            this.sotwRunnable = null;
            return true;
        }

        return false;
    }

    public void start(long millis) {
        if (this.sotwRunnable == null) {
            this.sotwRunnable = new SotwRunnable(this, millis);
            this.sotwRunnable.runTaskLater(HCF.getPlugin(), millis / 50L);
        }
    }

    public static class SotwRunnable extends BukkitRunnable {

        private SotwTimer sotwTimer;
        private long startMillis;
        private static long endMillis;

        @SuppressWarnings("static-access")
		public SotwRunnable(SotwTimer sotwTimer, long duration) {
            this.sotwTimer = sotwTimer;
            this.startMillis = System.currentTimeMillis();
            this.endMillis = this.startMillis + duration;
        }

        public long getRemaining() {
            return endMillis - System.currentTimeMillis();
        }

        @Override
        public void run() {
            Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "SOTW Protection is now over! Good Luck!");
            this.cancel();
            this.sotwTimer.sotwRunnable = null;
        }
    }

    public SotwRunnable getSotwRunnable() {
        return sotwRunnable;
    }

	public long getRemaining() {
        return SotwRunnable.endMillis - System.currentTimeMillis();
	}
}
