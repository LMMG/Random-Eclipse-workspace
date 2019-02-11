/*
 * Decompiled with CFR 0_115.
 */
package com.parapvp.base.command;

import com.parapvp.base.command.BaseCommand;
import com.parapvp.base.command.BaseCommandModule;

public interface CommandManager {
    public boolean containsCommand(BaseCommand var1);

    public void registerAll(BaseCommandModule var1);

    public void registerCommand(BaseCommand var1);

    public void registerCommands(BaseCommand[] var1);

    public void unregisterCommand(BaseCommand var1);

    public BaseCommand getCommand(String var1);
}

