/*
 * Decompiled with CFR 0_115.
 */
package com.parapvp.base.command.module;

import com.parapvp.base.BasePlugin;
import com.parapvp.base.command.BaseCommandModule;
import com.parapvp.base.command.module.teleport.BackCommand;
import com.parapvp.base.command.module.teleport.TeleportAllCommand;
import com.parapvp.base.command.module.teleport.TeleportCommand;
import com.parapvp.base.command.module.teleport.TeleportHereCommand;
import com.parapvp.base.command.module.teleport.TopCommand;
import com.parapvp.base.command.module.teleport.WorldCommand;
import java.util.Set;

public class TeleportModule
extends BaseCommandModule {
    public TeleportModule(BasePlugin plugin) {
        this.commands.add(new BackCommand(plugin));
        this.commands.add(new TeleportCommand());
        this.commands.add(new TeleportAllCommand());
        this.commands.add(new TeleportHereCommand());
        this.commands.add(new TopCommand());
        this.commands.add(new WorldCommand());
    }
}

