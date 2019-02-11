package org.hcgames.icefyre.game.fight.cache;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import org.hcgames.icefyre.game.arena.Arena;
import org.hcgames.icefyre.game.fight.Fight;
import org.hcgames.icefyre.game.fight.FightType;
import org.hcgames.icefyre.game.fight.inventory.FightInventory;
import org.hcgames.icefyre.game.ladder.Ladder;
import org.hcgames.icefyre.profile.Profile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CachedFight {

    @Getter private final UUID uuid;
    @Getter private final Map<UUID, FightInventory> participants;
    @Getter private final String ladder;
    @Getter private final String arena;

    public UUID getUuid() {
        return uuid;
    }

    @Getter private final FightType type;
    @Getter private final long startedAt;
    @Getter private final long duration;
    @Getter private final boolean won;

    public CachedFight(UUID uuid, Map<UUID, FightInventory> participants, String ladder, String arena, FightType type, long startedAt, long duration, boolean won) {
        this.uuid = uuid;
        this.participants = participants;
        this.ladder = ladder;
        this.arena = arena;
        this.type = type;
        this.startedAt = startedAt;
        this.duration = duration;
        this.won = won;
    }

    public CachedFight(Fight fight, boolean won) {
        this(UUID.randomUUID(), fight, won);
    }

    public Map<UUID, FightInventory> getParticipants() {
        return participants;
    }

    private CachedFight(UUID uuid, Fight fight, boolean won) {
        this(uuid, new HashMap<>(), fight.getLadder().getName(), fight.getArena().getName(), fight.getType(), fight.getStart(), fight.getStart() == 0 ? 0 : System.currentTimeMillis() - fight.getStart(), won);

        for (Profile profile : fight.getParticipants()) {
            this.participants.put(profile.getPlayer().getUniqueId(), profile.getFightInventory());
        }
    }

    public FightType getType() {
        return type;
    }
}
