package fun.reallyisnt.oms.core.modules.dms.commands;

import fun.reallyisnt.oms.common.Config;
import fun.reallyisnt.oms.core.OMSCore;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class Message extends Command {
    protected Message() {
        super("message", "Messages a player", "/msg <player> <message>", Arrays.asList("msg", "w", "wisper", "pm"));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (args.length < 1) {
            return false;
        }

        String displayName = sender.getName();
        String playerPrefix = "";

        //if (sender instanceof Player) {
        //    Player p = (Player) sender;
        //    //TODO: set prefix
        //}

        Player targetPlayer = Bukkit.getPlayer(args[0]);

        if (targetPlayer == null) {
            if (OMSCore.getInstance().getConfigString(Config.REDIS_ENABLED).equalsIgnoreCase("true")) {
                //TODO: Send message via redis
            } else {
                sender.sendMessage(Color.RED+"That player is not online.");
            }
        }

        return false;
    }


    private String formatMessage(String template, String prefix, String senderName, String receiverName, String message) {
        return template
                .replace("%prefix%",prefix)
                .replace("%sendername%",senderName)
                .replace("%receivername%",receiverName)
                .replace("%message%",message);
    }
}
