/*
 * Decompiled with CFR 0_115.
 */
package com.parapvp.base.command.module;

import com.parapvp.base.BasePlugin;
import com.parapvp.base.command.BaseCommandModule;
import com.parapvp.base.command.module.inventory.ClearInvCommand;
import com.parapvp.base.command.module.inventory.CopyInvCommand;
import com.parapvp.base.command.module.inventory.GiveCommand;
import com.parapvp.base.command.module.inventory.IdCommand;
import com.parapvp.base.command.module.inventory.InvSeeCommand;
import com.parapvp.base.command.module.inventory.ItemCommand;
import com.parapvp.base.command.module.inventory.MoreCommand;
import com.parapvp.base.command.module.inventory.SkullCommand;
import java.util.Set;

public class InventoryModule
extends BaseCommandModule {
    public InventoryModule(BasePlugin plugin) {
        this.commands.add(new ClearInvCommand());
        this.commands.add(new GiveCommand());
        this.commands.add(new IdCommand());
        this.commands.add(new InvSeeCommand(plugin));
        this.commands.add(new ItemCommand());
        this.commands.add(new MoreCommand());
        this.commands.add(new SkullCommand());
        this.commands.add(new CopyInvCommand());
    }
}

