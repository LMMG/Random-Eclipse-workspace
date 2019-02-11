/*
 * Decompiled with CFR 0_115.
 */
package com.parapvp.base.command.module;

import com.parapvp.base.BasePlugin;
import com.parapvp.base.command.BaseCommandModule;
import com.parapvp.base.command.module.chat.ToggleSoundsCommand;
import com.parapvp.base.command.module.essential.AutoRestartCommand;
import com.parapvp.base.command.module.essential.BiomeCommand;
import com.parapvp.base.command.module.essential.EnchantCommand;
import com.parapvp.base.command.module.essential.EntitiesCommand;
import com.parapvp.base.command.module.essential.FeedCommand;
import com.parapvp.base.command.module.essential.FlyCommand;
import com.parapvp.base.command.module.essential.FreezeCommand;
import com.parapvp.base.command.module.essential.GamemodeCommand;
import com.parapvp.base.command.module.essential.HatCommand;
import com.parapvp.base.command.module.essential.HealCommand;
import com.parapvp.base.command.module.essential.KillCommand;
import com.parapvp.base.command.module.essential.LagCommand;
import com.parapvp.base.command.module.essential.NameHistoryCommand;
import com.parapvp.base.command.module.essential.NoteCommand;
import com.parapvp.base.command.module.essential.PingCommand;
import com.parapvp.base.command.module.essential.PlayTimeCommand;
import com.parapvp.base.command.module.essential.PositionCommand;
import com.parapvp.base.command.module.essential.RemoveEntityCommand;
import com.parapvp.base.command.module.essential.RenameCommand;
import com.parapvp.base.command.module.essential.RepairCommand;
import com.parapvp.base.command.module.essential.ReportCommand;
import com.parapvp.base.command.module.essential.RulesCommand;
import com.parapvp.base.command.module.essential.SetMaxPlayersCommand;
import com.parapvp.base.command.module.essential.SpeedCommand;
import com.parapvp.base.command.module.essential.StopLagCommand;
import com.parapvp.base.command.module.essential.SudoCommand;
import com.parapvp.base.command.module.essential.UptimeCommand;
import com.parapvp.base.command.module.essential.VanishCommand;
import com.parapvp.base.command.module.essential.WhoisCommand;

public class EssentialModule
extends BaseCommandModule {
    public EssentialModule(BasePlugin plugin) {
        this.commands.add(new AutoRestartCommand(plugin));
        this.commands.add(new BiomeCommand());
        this.commands.add(new EnchantCommand());
        this.commands.add(new NoteCommand());
        this.commands.add(new EntitiesCommand());
        this.commands.add(new FeedCommand());
        this.commands.add(new FlyCommand());
        this.commands.add(new FreezeCommand(plugin));
        this.commands.add(new GamemodeCommand());
        this.commands.add(new HatCommand());
        this.commands.add(new HealCommand());
        this.commands.add(new KillCommand());
        this.commands.add(new LagCommand());
        this.commands.add(new VanishCommand());
        this.commands.add(new NameHistoryCommand(plugin));
        this.commands.add(new PingCommand());
        this.commands.add(new PlayTimeCommand(plugin));
        this.commands.add(new PositionCommand());
        this.commands.add(new RemoveEntityCommand());
        this.commands.add(new RenameCommand());
        this.commands.add(new ReportCommand());
        this.commands.add(new RepairCommand());
        this.commands.add(new RulesCommand(plugin));
        this.commands.add(new SetMaxPlayersCommand());
        this.commands.add(new ToggleSoundsCommand(plugin));
        this.commands.add(new SpeedCommand());
        this.commands.add(new StopLagCommand(plugin));
        this.commands.add(new SudoCommand());
        this.commands.add(new UptimeCommand());
        this.commands.add(new WhoisCommand(plugin));
    }
}

