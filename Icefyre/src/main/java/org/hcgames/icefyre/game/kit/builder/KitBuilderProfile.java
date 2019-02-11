package org.hcgames.icefyre.game.kit.builder;

import lombok.Getter;
import lombok.Setter;
import org.hcgames.icefyre.game.ladder.Ladder;
import org.hcgames.icefyre.game.queue.Queue;
import org.hcgames.icefyre.profile.Profile;

public class KitBuilderProfile {

    @Getter private KitBuilder kitBuilder;
    @Getter private Ladder ladder;
    @Getter @Setter private KitBuilderProfileData data;

    public KitBuilderProfile(KitBuilder kitBuilder, Ladder ladder) {
        this.kitBuilder = kitBuilder;
        this.ladder = ladder;
    }

}
