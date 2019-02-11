/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.serialization.ConfigurationSerializable
 */
package com.parapvp.base.user;

import com.parapvp.base.user.ServerParticipator;
import java.util.Map;
import java.util.UUID;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class ConsoleUser
extends ServerParticipator
implements ConfigurationSerializable {
    public static final UUID CONSOLE_UUID = UUID.fromString("29f26148-4d55-4b4b-8e07-900fda686a67");
    String name = "CONSOLE";

    public ConsoleUser() {
        super(CONSOLE_UUID);
    }

    public ConsoleUser(Map<String, Object> map) {
        super(map);
    }

    @Override
    public String getName() {
        return this.name;
    }
}

