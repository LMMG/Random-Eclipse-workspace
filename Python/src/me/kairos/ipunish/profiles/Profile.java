package me.kairos.ipunish.profiles;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.kairos.ipunish.IPunish;

import java.util.LinkedHashSet;
import java.util.UUID;

@Getter
@Setter
public class Profile {

    // Ban duration, 0 means none, -1 means permanent
    private long banDurationMillis = 0L, muteDurationMillis = 0L;

    // Millisecond timestamps for current mute or ban, 0 means none.
    private long banStamp = 0L, muteStamp = 0L;

    // Reasons for current ban (may be null).
    private String banReason, muteReason;

    private LinkedHashSet<UUID> alts = new LinkedHashSet<>();
    private LinkedHashSet<String> ips = new LinkedHashSet<>();

    // The banner or muter UUID.
    private UUID muterUUID, bannerUUID;

    private final UUID uuid;
    private final String lastIP;

    @Setter(AccessLevel.NONE)
    private int kickCount, banCount, unbanCount, muteCount;

    public Profile(UUID uuid, String lastIP, int kickCount, int banCount, int unbanCount, int muteCount) {
        this.uuid = uuid;
        this.lastIP = lastIP;
        this.ips.add(lastIP);

        this.kickCount = kickCount;
        this.banCount = banCount;
        this.unbanCount = unbanCount;
        this.muteCount = muteCount;
    }

    public void incrementKickCount() {
        kickCount++;
    }

    public void incrementBanCount() {
        banCount++;
    }

    public void incrementUnbanCount() {
        unbanCount++;
    }

    public void incrementMuteCount() {
        muteCount++;
    }

    public long getBanRemaining() {
        if (banDurationMillis == 0L) {
            return 0L;
        } else if (banDurationMillis == PunishmentStatus.PERMANENT.getId()) {
            return (long) PunishmentStatus.PERMANENT.getId();
        } else {
            long remaining = (banStamp + banDurationMillis) - System.currentTimeMillis();
            if (remaining < 0L) {
                setBanDurationMillis(Profile.PunishmentStatus.NONE.getId());
                setBanStamp(Profile.PunishmentStatus.NONE.getId());
                IPunish.getInstance().getProfileManager().save(this);
            }

            return remaining;
        }
    }

    public long getMuteRemaining() {
        if (muteDurationMillis == 0L) {
            return 0L;
        } else if (muteDurationMillis == PunishmentStatus.PERMANENT.getId()) {
            return (long) PunishmentStatus.PERMANENT.getId();
        } else {
            long remaining = (muteStamp + muteDurationMillis) - System.currentTimeMillis();
            if (remaining < 0L) {
                setMuteDurationMillis(Profile.PunishmentStatus.NONE.getId());
                setMuteStamp(Profile.PunishmentStatus.NONE.getId());
                IPunish.getInstance().getProfileManager().save(this);
            }

            return remaining;
        }
    }

    public enum PunishmentStatus {

        NONE(0), PERMANENT(-1);

        @Getter
        public final int id;

        PunishmentStatus(int id) {
            this.id = id;
        }
    }
}
