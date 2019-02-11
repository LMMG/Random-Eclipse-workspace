package com.alexandeh.ekko.factions.claims;

import com.alexandeh.ekko.factions.Faction;
import com.alexandeh.ekko.profiles.Profile;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
@Getter @Setter public class ClaimProfile {

    private Player player;
    private Profile profile;
    private Faction faction;
    private boolean resetClicked;
    private ClaimPillar[] pillars;

    public ClaimProfile(Player player, Faction faction) {
        this.player = player;
        this.faction = faction;

        pillars = new ClaimPillar[2];

        profile = Profile.getByUuid(player.getUniqueId());
        profile.setClaimProfile(this);
    }

    public void removePillars() {
        for (ClaimPillar claimPillar : pillars) {
            if (claimPillar != null) {
                claimPillar.remove();
            }
        }
        pillars = new ClaimPillar[2];
    }

}
