package org.hcgames.icefyre.game.kit.builder;

import lombok.Getter;
import lombok.Setter;

public class KitBuilderProfileData {

    @Getter @Setter private String name;
    @Getter @Setter private int position;

    public KitBuilderProfileData(int position) {
        this.position = position;
    }
}
