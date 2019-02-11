package me.kairos.ipunish.commands;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import me.kairos.ipunish.IPunish;
import me.kairos.ipunish.Utils;
import me.kairos.ipunish.profiles.Profile;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class HistoryCommand implements CommandExecutor, TabCompleter {

    private static final FastDateFormat FORMAT = FastDateFormat.getInstance("MM/dd/yyyy h:mma", Locale.ENGLISH);
    private static final String LINE = ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + Strings.repeat(' ', 64);

    private final IPunish plugin;

    public HistoryCommand(IPunish plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "/" + label + " <playerName>");
            return true;
        }

        String playerName;
        Profile profile;
        Player target = Bukkit.getPlayer(args[0]);
        if (target != null) {
            playerName = target.getName();
            profile = plugin.getProfileManager().getProfile(target.getUniqueId());
        } else {
            OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(args[0]);

            if (!offlineTarget.hasPlayedBefore()) {
                sender.sendMessage(Utils.getPlayerNotFoundMessage(args[0]));
                return true;
            }

            playerName = offlineTarget.getName();
            profile = plugin.getProfileManager().getProfile(offlineTarget.getUniqueId());
        }

        if (profile == null) {
            sender.sendMessage(Utils.getProfileNotFoundMessage(args[0]));
            return true;
        }

        sender.sendMessage(LINE);
        sender.sendMessage(ChatColor.YELLOW + "Name" + ChatColor.GRAY + ": " + playerName);
        sender.sendMessage(ChatColor.YELLOW + "Recent IP" + ChatColor.GRAY + ": " + profile.getLastIP());
        sender.sendMessage(ChatColor.YELLOW + "IPs" + ChatColor.GRAY + ": " + StringUtils.join(profile.getIps(), ", "));

        List<String> altNames;
        if (profile.getAlts().size() > 1) {
            altNames = Lists.transform(new ArrayList<>(profile.getAlts()), new Function<UUID, String>() {
                @Override
                public String apply(@Nullable UUID uuid) {
                    return Bukkit.getOfflinePlayer(uuid).getName();
                }
            });
        } else altNames = ImmutableList.of();
        sender.sendMessage(ChatColor.YELLOW + "Alts" + ChatColor.GRAY + ": " + (altNames.isEmpty() ? "N/A" : "[" + StringUtils.join(altNames, ", ") + "]"));

        boolean banned = profile.getBanRemaining() != Profile.PunishmentStatus.NONE.getId();
        sender.sendMessage(ChatColor.YELLOW + "Banned" + ChatColor.GRAY + ": " + (banned ? ChatColor.GREEN : ChatColor.RED) + banned);
        if (banned) {
            sender.sendMessage(ChatColor.YELLOW + "  At" + ChatColor.GRAY + ": " + FORMAT.format(profile.getBanStamp()));
            sender.sendMessage(ChatColor.YELLOW + "  Reason" + ChatColor.GRAY + ": " + Objects.firstNonNull(profile.getBanReason(), plugin.getConfiguration().getDefaultBanReason()));
            sender.sendMessage(ChatColor.YELLOW + "  By" + ChatColor.GRAY + ": " + Bukkit.getOfflinePlayer(profile.getBannerUUID()).getName());
            if (profile.getBanRemaining() != Profile.PunishmentStatus.PERMANENT.getId()) {
                sender.sendMessage(ChatColor.YELLOW + "  Expires in" + ChatColor.GRAY + ": " +
                        DurationFormatUtils.formatDurationWords(profile.getBanRemaining(), true, true));
            }
        }

        boolean muted = profile.getMuteRemaining() != Profile.PunishmentStatus.NONE.getId();
        sender.sendMessage(ChatColor.YELLOW + "Muted" + ChatColor.GRAY + ": " + (muted ? ChatColor.GREEN : ChatColor.RED) + muted);
        if (muted) {
            sender.sendMessage(ChatColor.YELLOW + "  At" + ChatColor.GRAY + ": " + FORMAT.format(profile.getMuteStamp()));
            sender.sendMessage(ChatColor.YELLOW + "  Reason" + ChatColor.GRAY + ": " + Objects.firstNonNull(profile.getMuteReason(), plugin.getConfiguration().getDefaultBanReason()));
            sender.sendMessage(ChatColor.YELLOW + "  By" + ChatColor.GRAY + ": " + Bukkit.getOfflinePlayer(profile.getMuterUUID()).getName());
            if (profile.getMuteRemaining() != Profile.PunishmentStatus.PERMANENT.getId()) {
                sender.sendMessage(ChatColor.YELLOW + "  Expires in" + ChatColor.GRAY + ": " +
                        DurationFormatUtils.formatDurationWords(profile.getMuteRemaining(), true, true));
            }
        }

        sender.sendMessage(ChatColor.YELLOW + "Bans" + ChatColor.GRAY + ": " + profile.getBanCount());
        sender.sendMessage(ChatColor.YELLOW + "Kicks" + ChatColor.GRAY + ": " + profile.getKickCount());
        sender.sendMessage(ChatColor.YELLOW + "Mutes" + ChatColor.GRAY + ": " + profile.getMuteCount());
        sender.sendMessage(ChatColor.YELLOW + "Unbans" + ChatColor.GRAY + ": " + profile.getUnbanCount());
        sender.sendMessage(LINE);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? null : Collections.emptyList();
    }
}
