package net.nersa.kitmap.timer;

import com.doctordark.util.Config;

import lombok.Data;
import lombok.Getter;
import net.nersa.kitmap.HCF;
import net.nersa.kitmap.eventgame.EventTimer;
import net.nersa.kitmap.timer.type.*;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class TimerManager implements Listener {

	@Getter
	public final CombatTimer combatTimer;
	@Getter
	public final GappleTimer gappleTimer;
	@Getter
	public final TeleportTimer teleportTimer;
	@Getter
	public final ArcherTimer archerTimer;
	public final RestartTimer restartTimer;
	@Getter
	private final LogoutTimer logoutTimer;
	@Getter
	private final EnderPearlTimer enderPearlTimer;
	@Getter
	private final EventTimer eventTimer;
	@Getter
	private final PvpClassWarmupTimer pvpClassWarmupTimer;
	@Getter
	private final StuckTimer stuckTimer;
	@Getter
	private final Set<Timer> timers = new LinkedHashSet<>();
	private final JavaPlugin plugin;
	private Config config;

	public TimerManager(HCF plugin) {
		(this.plugin = plugin).getServer().getPluginManager().registerEvents(this, plugin);
		this.registerTimer(this.archerTimer = new ArcherTimer(plugin));
		this.registerTimer(this.enderPearlTimer = new EnderPearlTimer(plugin));
		this.registerTimer(this.logoutTimer = new LogoutTimer());
		this.registerTimer(this.gappleTimer = new GappleTimer(plugin));
		this.registerTimer(this.stuckTimer = new StuckTimer());
		this.registerTimer(this.combatTimer = new CombatTimer(plugin));
		this.registerTimer(this.teleportTimer = new TeleportTimer(plugin));
		this.registerTimer(this.eventTimer = new EventTimer(plugin));
		this.registerTimer(this.pvpClassWarmupTimer = new PvpClassWarmupTimer(plugin));
		this.registerTimer(this.restartTimer = new RestartTimer());
		this.reloadTimerData();
	}

	public void registerTimer(Timer timer) {
		this.timers.add(timer);
		if (timer instanceof Listener) {
			this.plugin.getServer().getPluginManager().registerEvents((Listener) timer, this.plugin);
		}
	}

	public void unregisterTimer(Timer timer) {
		this.timers.remove(timer);
	}

	/**
	 * Reloads the {@link Timer} data from storage.
	 */
	public void reloadTimerData() {
		this.config = new Config(plugin, "timers");
		for (Timer timer : this.timers) {
			timer.load(this.config);
		}
	}

	/**
	 * Saves the {@link Timer} data to storage.
	 */
	public void saveTimerData() {
		for (Timer timer : this.timers) {
			timer.onDisable(this.config);
		}

		this.config.save();
	}
}
