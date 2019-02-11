/*
 * Decompiled with CFR 0_115.
 */
package com.parapvp.base.command.module;

import com.parapvp.base.BasePlugin;
import com.parapvp.base.command.BaseCommandModule;
import com.parapvp.base.command.module.chat.BroadcastCommand;
import com.parapvp.base.command.module.chat.BroadcastRawCommand;
import com.parapvp.base.command.module.chat.ClearChatCommand;
import com.parapvp.base.command.module.chat.DisableChatCommand;
import com.parapvp.base.command.module.chat.IgnoreCommand;
import com.parapvp.base.command.module.chat.MessageCommand;
import com.parapvp.base.command.module.chat.MessageSpyCommand;
import com.parapvp.base.command.module.chat.ReplyCommand;
import com.parapvp.base.command.module.chat.SlowChatCommand;
import com.parapvp.base.command.module.chat.StaffChatCommand;
import com.parapvp.base.command.module.chat.ToggleChatCommand;
import com.parapvp.base.command.module.chat.ToggleMessagesCommand;
import com.parapvp.base.command.module.chat.ToggleStaffChatCommand;
import java.util.Set;

public class ChatModule
extends BaseCommandModule {
    public ChatModule(BasePlugin plugin) {
        this.commands.add(new BroadcastCommand(plugin));
        this.commands.add(new BroadcastRawCommand());
        this.commands.add(new ClearChatCommand());
        this.commands.add(new DisableChatCommand(plugin));
        this.commands.add(new SlowChatCommand(plugin));
        this.commands.add(new StaffChatCommand(plugin));
        this.commands.add(new IgnoreCommand(plugin));
        this.commands.add(new MessageCommand(plugin));
        this.commands.add(new MessageSpyCommand(plugin));
        this.commands.add(new ReplyCommand(plugin));
        this.commands.add(new ToggleChatCommand(plugin));
        this.commands.add(new ToggleMessagesCommand(plugin));
        this.commands.add(new ToggleStaffChatCommand(plugin));
    }
}

