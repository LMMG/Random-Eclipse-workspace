package server.wenjapvp.hcf.faction.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.doctordark.util.BukkitUtils;
import com.doctordark.util.GenericUtils;
import com.doctordark.util.JavaUtils;
import com.doctordark.util.PersistableLocation;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import server.wenjapvp.hcf.DateTimeFormats;
import server.wenjapvp.hcf.HCF;
import server.wenjapvp.hcf.config.ConfigurationService;
import server.wenjapvp.hcf.faction.FactionMember;
import server.wenjapvp.hcf.faction.event.cause.FactionLeaveCause;
import server.wenjapvp.hcf.faction.struct.Raidable;
import server.wenjapvp.hcf.faction.struct.RegenStatus;
import server.wenjapvp.hcf.faction.struct.Relation;
import server.wenjapvp.hcf.faction.struct.Role;
import server.wenjapvp.hcf.timer.type.TeleportTimer;
import server.wenjapvp.hcf.user.FactionUser;
import server.wenjapvp.hcf.faction.event.FactionDtrChangeEvent;
import server.wenjapvp.hcf.faction.event.PlayerJoinFactionEvent;
import server.wenjapvp.hcf.faction.event.PlayerJoinedFactionEvent;
import server.wenjapvp.hcf.faction.event.PlayerLeaveFactionEvent;
import server.wenjapvp.hcf.faction.event.PlayerLeftFactionEvent;

public class PlayerFaction extends ClaimableFaction implements Raidable {

    // The UUID is the Faction unique ID.
    protected final Map<UUID, Relation> requestedRelations = new HashMap<>();
    protected final Map<UUID, Relation> relations = new HashMap<>();

    protected final Map<UUID, FactionMember> members = new HashMap<>();
    protected final Set<String> invitedPlayerNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER); // we are storing names as offline lookups are slow

    protected PersistableLocation home;
    protected String announcement;
    protected boolean open;
    protected int balance;
    
    protected int lives;
    @Getter
    @Setter
    private transient UUID focused;

    public PlayerFaction(String name) {
        super(name);
    }

    public PlayerFaction(Map<String, Object> map) {
        super(map);

        for (Map.Entry<String, FactionMember> entry : GenericUtils.castMap(map.get("members"), String.class, FactionMember.class).entrySet()) {
            if (entry.getValue() != null) {
                this.members.put(UUID.fromString(entry.getKey()), entry.getValue());
            }
        }

        this.invitedPlayerNames.addAll(GenericUtils.createList(map.get("invitedPlayerNames"), String.class));

        Object object = map.get("home");
        if (object != null)
            this.home = ((PersistableLocation) object);

        object = map.get("announcement");
        if (object != null)
            this.announcement = (String) object;

        for (Map.Entry<String, String> entry : GenericUtils.castMap(map.get("relations"), String.class, String.class).entrySet()) {
            relations.put(UUID.fromString(entry.getKey()), Relation.valueOf(entry.getValue()));
        }

        for (Map.Entry<String, String> entry : GenericUtils.castMap(map.get("requestedRelations"), String.class, String.class).entrySet()) {
            requestedRelations.put(UUID.fromString(entry.getKey()), Relation.valueOf(entry.getValue()));
        }

        this.open = (Boolean) map.get("open");
        this.balance = (Integer) map.get("balance");
        this.lastDtrUpdateTimestamp = Long.parseLong((String) map.get("lastDtrUpdateTimestamp"));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();

        Map<String, String> relationSaveMap = new HashMap<>(relations.size());
        for (Map.Entry<UUID, Relation> entry : relations.entrySet()) {
            relationSaveMap.put(entry.getKey().toString(), entry.getValue().name());
        }
        map.put("relations", relationSaveMap);

        Map<String, String> requestedRelationsSaveMap = new HashMap<>(requestedRelations.size());
        for (Map.Entry<UUID, Relation> entry : requestedRelations.entrySet()) {
            requestedRelationsSaveMap.put(entry.getKey().toString(), entry.getValue().name());
        }
        map.put("requestedRelations", requestedRelationsSaveMap);

        Set<Map.Entry<UUID, FactionMember>> entrySet = this.members.entrySet();
        Map<String, FactionMember> saveMap = new LinkedHashMap<>(this.members.size());
        for (Map.Entry<UUID, FactionMember> entry : entrySet) {
            saveMap.put(entry.getKey().toString(), entry.getValue());
        }

        map.put("members", saveMap);
        map.put("invitedPlayerNames", new ArrayList<>(invitedPlayerNames));
        if (home != null)
            map.put("home", home);
        if (announcement != null)
            map.put("announcement", announcement);
        map.put("open", open);
        map.put("balance", balance);
        map.put("lastDtrUpdateTimestamp", Long.toString(lastDtrUpdateTimestamp));

        return map;
    }

    public boolean addMember(CommandSender sender, @Nullable Player player, UUID playerUUID, FactionMember factionMember) {
        if (members.containsKey(playerUUID)) {
            return false;
        }

        PlayerJoinFactionEvent eventPre = new PlayerJoinFactionEvent(sender, player, playerUUID, this);
        Bukkit.getPluginManager().callEvent(eventPre);
        if (eventPre.isCancelled()) {
            return false;
        }

        // Set the player as a member before calling the
        // event so we can change the scoreboard.
        lastDtrUpdateTimestamp = System.currentTimeMillis();
        invitedPlayerNames.remove(factionMember.getName());
        members.put(playerUUID, factionMember);
        Bukkit.getPluginManager().callEvent(new PlayerJoinedFactionEvent(sender, player, playerUUID, this));

        return true;
    }

    public boolean removeMember(CommandSender sender, @Nullable Player player, UUID playerUUID, boolean kick, boolean force) {
        if (!this.members.containsKey(playerUUID)) {
            return true;
        }

        // Call pre event.
        PlayerLeaveFactionEvent preEvent = new PlayerLeaveFactionEvent(sender, player, playerUUID, this, FactionLeaveCause.LEAVE, kick, false);
        Bukkit.getPluginManager().callEvent(preEvent);
        if (preEvent.isCancelled()) {
            return false;
        }

        this.members.remove(playerUUID);

        // Call after event.
        PlayerLeftFactionEvent event = new PlayerLeftFactionEvent(sender, player, playerUUID, this, FactionLeaveCause.LEAVE, kick, false);
        Bukkit.getPluginManager().callEvent(event);

        return true;
    }

    /**
     * Gets a list of faction UUIDs that are allied to this {@link PlayerFaction}.
     *
     * @return mutable list of UUIDs
     */
    public Collection<UUID> getAllied() {
        return Maps.filterValues(relations, new Predicate<Relation>() {
            @Override
            public boolean apply(@Nullable Relation relation) {
                return relation == Relation.ALLY;
            }
        }).keySet();
    }

    /**
     * Gets a list of {@link PlayerFaction}s that are allied to this {@link PlayerFaction}.
     *
     * @return mutable list of {@link PlayerFaction}s
     */
    public List<PlayerFaction> getAlliedFactions() {
        Collection<UUID> allied = getAllied();
        Iterator<UUID> iterator = allied.iterator();
        List<PlayerFaction> results = new ArrayList<>(allied.size());
        while (iterator.hasNext()) {
            Faction faction = HCF.getPlugin().getFactionManager().getFaction(iterator.next());
            if (faction instanceof PlayerFaction) {
                results.add((PlayerFaction) faction);
            } else
                iterator.remove();
        }

        return results;
    }

    public Map<UUID, Relation> getRequestedRelations() {
        return requestedRelations;
    }

    public Map<UUID, Relation> getRelations() {
        return relations;
    }

    /**
     * Gets the members in this {@link PlayerFaction}.
     * <p>
     * The key is the {@link UUID} of the member
     * </p>
     * <p>
     * The value is the {@link FactionMember}
     * </p>
     *
     * @return map of members.
     */
    public Map<UUID, FactionMember> getMembers() {
        return ImmutableMap.copyOf(members);
    }

    /**
     * Gets the online {@link Player}s in this {@link Faction}.
     *
     * @return set of online {@link Player}s
     */
    public Set<Player> getOnlinePlayers() {
        return getOnlinePlayers(null);
    }

    /**
     * Gets the online {@link Player}s in this {@link Faction} that are visible to a {@link CommandSender}.
     *
     * @param sender
     *            the {@link CommandSender} to get for
     * @return a set of online players visible to sender
     */
    public Set<Player> getOnlinePlayers(CommandSender sender) {
        Set<Map.Entry<UUID, FactionMember>> entrySet = getOnlineMembers(sender).entrySet();
        Set<Player> results = new HashSet<>(entrySet.size());
        for (Map.Entry<UUID, FactionMember> entry : entrySet) {
            results.add(Bukkit.getPlayer(entry.getKey()));
        }

        return results;
    }

    /**
     * Gets the online members in this {@link Faction}.
     * <p>
     * The key is the {@link UUID} of the member
     * </p>
     * <p>
     * The value is the {@link FactionMember}
     * </p>
     *
     * @return an immutable set of online members
     */
    public Map<UUID, FactionMember> getOnlineMembers() {
        return getOnlineMembers(null);
    }

    /**
     * Gets the online members in this {@link Faction} that are visible to a {@link CommandSender}.
     * <p>
     * The key is the {@link UUID} of the member
     * </p>
     * <p>
     * The value is the {@link FactionMember}
     * </p>
     *
     * @param sender
     *            the {@link CommandSender} to get for
     * @return a set of online members visible to sender
     */
    public Map<UUID, FactionMember> getOnlineMembers(CommandSender sender) {
        Player senderPlayer = sender instanceof Player ? ((Player) sender) : null;
        Map<UUID, FactionMember> results = new HashMap<>();
        for (Map.Entry<UUID, FactionMember> entry : members.entrySet()) {
            Player target = Bukkit.getPlayer(entry.getKey());
            if (target == null || (senderPlayer != null && !senderPlayer.canSee(target))) {
                continue;
            }

            results.put(entry.getKey(), entry.getValue());
        }

        return results;
    }

    /**
     * Gets the leading {@link FactionMember} of this {@link Faction}.
     *
     * @return the leading {@link FactionMember}
     */
    public FactionMember getLeader() {
        Map<UUID, FactionMember> members = this.members;
        for (Map.Entry<UUID, FactionMember> entry : members.entrySet()) {
            if (entry.getValue().getRole() == Role.LEADER) {
                return entry.getValue();
            }
        }

        return null;
    }
    
    public void setLives(int lives)
    {
      this.lives = lives;
    }

    public int getLives()
    {
      return this.lives;
    }
    
    /**
     * Gets the {@link FactionMember} with a specific name.
     *
     * @param memberName
     *            the id to search for
     * @return the {@link FactionMember} or null if is not a member
     * @deprecated uses hanging offline player method
     */
    @Deprecated
    public FactionMember getMember(String memberName) {
        UUID uuid = Bukkit.getOfflinePlayer(memberName).getUniqueId(); // TODO: breaking
        return uuid == null ? null : members.get(uuid);
    }

    /**
     * Gets the {@link FactionMember} of a {@link Player}.
     *
     * @param player
     *            the {@link Player} to get for
     * @return the {@link FactionMember} or null if is not a member
     */
    public FactionMember getMember(Player player) {
        return this.getMember(player.getUniqueId());
    }

    /**
     * Gets the {@link FactionMember} with a specific {@link UUID}.
     *
     * @param memberUUID
     *            the {@link UUID} to get for
     * @return the {@link FactionMember} or null if is not a member
     */
    public FactionMember getMember(UUID memberUUID) {
        return members.get(memberUUID);
    }

    /**
     * Gets the names of the players that have been invited to join this {@link PlayerFaction}.
     *
     * @return set of invited player names
     */
    public Set<String> getInvitedPlayerNames() {
        return invitedPlayerNames;
    }

    public Location getHome() {
        return home == null ? null : home.getLocation();
    }

    public void setHome(@Nullable Location home) {
        if (home == null && this.home != null) {
            TeleportTimer timer = HCF.getPlugin().getTimerManager().getTeleportTimer();
            for (Player player : getOnlinePlayers()) {
                Location destination = timer.getDestination(player);
                if (Objects.equal(destination, this.home.getLocation())) {
                    timer.clearCooldown(player);
                    player.sendMessage(ChatColor.RED + "Your faction home was unset, so your " + timer.getDisplayName() + ChatColor.RED + " timer has been cancelled");
                }
            }
        }

        this.home = home == null ? null : new PersistableLocation(home);
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(@Nullable String announcement) {
        this.announcement = announcement;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public double getMaximumDeathsUntilRaidable() {
        if (members.size() == 1) {
            return 1.1;
        }
        if (members.size() == 2){
            return 2.1;
        }

        return Math.min(ConfigurationService.MAXIMUM_DTR, members.size() * 0.9);
    }

    @Getter
    private long lastDtrUpdateTimestamp;

    /**
     * Updates the deaths until raidable value depending how much is gained every x seconds as set in configuration.
     */
    private void updateDeathsUntilRaidable() {
        if (this.getRegenStatus() == RegenStatus.REGENERATING) {
            long now = System.currentTimeMillis();
            long millisPassed = now - this.lastDtrUpdateTimestamp;
            if (millisPassed >= ConfigurationService.DTR_MILLIS_BETWEEN_UPDATES) {
                long remainder = millisPassed % ConfigurationService.DTR_MILLIS_BETWEEN_UPDATES; // the remaining time until the next update
                int multiplier = (int) (((double) millisPassed + remainder) / ConfigurationService.DTR_MILLIS_BETWEEN_UPDATES);
                double increase = multiplier * ConfigurationService.DTR_INCREMENT_BETWEEN_UPDATES;

                this.lastDtrUpdateTimestamp = now - remainder;
            }
        }
    }

    @Override
    public void setRemainingRegenerationTime(long millis) {
        long systemMillis = System.currentTimeMillis();

        // needs to be multiplied by 2 because as soon as they lose regeneration delay, the timestamp will update
        this.lastDtrUpdateTimestamp = systemMillis + (ConfigurationService.DTR_MILLIS_BETWEEN_UPDATES * 2);
    }

    public void printDetails(CommandSender sender) {
        String leaderName = null;
        HashSet allyNames = new HashSet(1);
        Iterator combinedKills = this.relations.entrySet().iterator();

        PlayerFaction playerFaction;
        while(combinedKills.hasNext()) {
            Map.Entry memberNames = (Map.Entry) combinedKills.next();
            Faction captainNames = HCF.getPlugin().getFactionManager().getFaction((UUID) memberNames.getKey());
            if(captainNames instanceof PlayerFaction) {
                playerFaction = (PlayerFaction) captainNames;
                allyNames.add(playerFaction.getDisplayName(sender) + ChatColor.GRAY + '[' + ChatColor.GRAY + playerFaction.getOnlinePlayers(sender).size() + ChatColor.GRAY + '/' + ChatColor.GRAY + playerFaction.members.size() + ChatColor.GRAY + ']');
            }
        }

        HashSet memberNames = new HashSet();
        HashSet<String> captainNames = new HashSet();
        HashSet<String> coleaderNames = new HashSet();
        Iterator playerFaction1 = this.members.entrySet().iterator();

        while(playerFaction1.hasNext()) {
            final Map.Entry entry = (Map.Entry) playerFaction1.next();
            final FactionMember factionMember = (FactionMember) entry.getValue();
            final Player target = factionMember.toOnlinePlayer();
            final FactionUser user = HCF.getPlugin().getUserManager().getUser((UUID) entry.getKey());
            int kills = user.getKills();
//            final Deathban deathban = user.getDeathban();
            ChatColor colour;
            final String memberName = factionMember.getName() +  ChatColor.GRAY + '[' +  ChatColor.GREEN + kills +  ChatColor.GRAY + ']';
            switch (factionMember.getRole()) {
                case LEADER: {
                    leaderName = memberName;
                    continue;
                }
                
                case COLEADER: {
                	coleaderNames.add(memberName);
                	continue;
                }
                
                case CAPTAIN: {
                    captainNames.add(memberName);
                    continue;
                }
                case MEMBER: {
                    memberNames.add(memberName);
                    continue;
                }
            }
        }

        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage(ChatColor.DARK_GREEN +  this.getDisplayName(sender) + ChatColor.GRAY + " [" + ChatColor.GRAY + this.getOnlinePlayers(sender).size() + ChatColor.GRAY + "/" + ChatColor.GRAY + this.members.size() + ChatColor.GRAY + "]" + ChatColor.YELLOW +  "  Home " + ((this.home == null) ? " None " : ChatColor.WHITE
        		+ ("(" + this.home.getLocation().getBlockX() + " | " + this.home.getLocation().getBlockZ() + ')')));
        if (!allyNames.isEmpty()) {
            sender.sendMessage(ChatColor.YELLOW + "  Allies: " + StringUtils.join((Iterable)allyNames, ChatColor.GRAY + ", "));
        }
        if (leaderName != null) {
            sender.sendMessage(ChatColor.YELLOW + "  Leader: " + ChatColor.GREEN + leaderName);
        }
        if (!coleaderNames.isEmpty()) {
        	sender.sendMessage(ChatColor.YELLOW + "  Co-Leaders: " + ChatColor.RED + StringUtils.join(coleaderNames, new StringBuilder().append(ChatColor.GRAY).append(", ").toString()));
        }
        if (!captainNames.isEmpty()) {
            sender.sendMessage(ChatColor.YELLOW + "  Officers: " + ChatColor.RED + StringUtils.join((Iterable)captainNames, ChatColor.GRAY + ", "));
        }
        if (!memberNames.isEmpty()) {
            sender.sendMessage(ChatColor.YELLOW + "  Members: " + ChatColor.RED + StringUtils.join((Iterable)memberNames, ChatColor.GRAY + ", "));
        }
        sender.sendMessage(ChatColor.YELLOW + "  Balance: " + ChatColor.BLUE + '$' + this.balance);
        sender.sendMessage(ChatColor.YELLOW + "  This faction was founded on " + ChatColor.BLUE + DateTimeFormats.DAY_MTH_YR_HR_MIN_AMPM.format(this.creationMillis));
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
    }

    private static final UUID[] EMPTY_UUID_ARRAY = {};

    /**
     * Sends a message to all online {@link FactionMember}s.
     *
     * @param message
     *            the message to send
     */
    public void broadcast(String message) {
        broadcast(message, EMPTY_UUID_ARRAY);
    }

    /**
     * Sends an array of messages to all online {@link FactionMember}s.
     *
     * @param messages
     *            the messages to send.
     */
    public void broadcast(String[] messages) {
        broadcast(messages, EMPTY_UUID_ARRAY);
    }

    /**
     * Sends a message to all online {@link FactionMember}s ignoring those selected in the var-args.
     *
     * @param message the message to send.
     * @param ignore  the {@link FactionMember} with {@link UUID}s not to send message to
     */
    public void broadcast(String message, @Nullable UUID... ignore) {
        this.broadcast(new String[]{message}, ignore);
    }

    /**
     * Sends an array of messages to all online {@link FactionMember}s ignoring those selected in the var-args.
     *
     * @param messages
     *            the message to send
     * @param ignore
     *            the {@link FactionMember} with {@link UUID}s not to send message to
     */
    public void broadcast(String[] messages, UUID... ignore) {
        Preconditions.checkNotNull(messages, "Messages cannot be null");
        Preconditions.checkArgument(messages.length > 0, "Message array cannot be empty");
        Collection<Player> players = getOnlinePlayers();
        Collection<UUID> ignores = ignore.length == 0 ? Collections.emptySet() : Sets.newHashSet(ignore);
        for (Player player : players) {
            if (!ignores.contains(player.getUniqueId())) {
                player.sendMessage(messages);
            }
        }
    }

    public long getLastDtrUpdateTimestamp() {
        return lastDtrUpdateTimestamp;
    }

	@Override
	public boolean isRaidable()
	{
		return false;
	}

	@Override
	public double getDeathsUntilRaidable()
	{
		return 0;
	}

	@Override
	public long getRemainingRegenerationTime()
	{
		return 0;
	}

	@Override
	public double setDeathsUntilRaidable(double deathsUntilRaidable)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RegenStatus getRegenStatus()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
