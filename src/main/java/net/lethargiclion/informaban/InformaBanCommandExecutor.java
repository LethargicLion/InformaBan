package net.lethargiclion.informaban;

/*
 This file is part of InformaBan.

 InformaBan is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 InformaBan is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with InformaBan.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.lethargiclion.informaban.events.Ban;
import net.lethargiclion.informaban.events.Event;
import net.lethargiclion.informaban.events.Kick;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * InformaBan command executor class. Processes commands.
 * 
 * @author TerrorBite
 * 
 */
public class InformaBanCommandExecutor implements CommandExecutor {

    private InformaBan plugin;

    /**
     * Creates a new instance of this command executor.
     * 
     * @param plugin
     *            The parent plugin instance.
     */
    public InformaBanCommandExecutor(InformaBan plugin) {
        this.plugin = plugin;
    }

    // Javadocs provided by overridden method
    @Override
    public boolean onCommand(CommandSender sender, Command command,
            String label, String[] args) {

        try {
            if (command.getName().equalsIgnoreCase("kick"))
                return commandKick(sender, args);
            if (command.getName().equalsIgnoreCase("rap"))
                return commandRap(sender, args);
            if (command.getName().equalsIgnoreCase("ban"))
                return commandBan(sender, args);
        } catch (java.util.MissingResourceException e) {
            sender.sendMessage(new String[] {
                    "[InformaBan] An internal error occured. Please file a bug report.",
                    String.format(
                            "[InformaBan] Error: No message defined for \"%s\".",
                            e.getKey()) });
        }
        return false;
    }

    /**
     * Handles the /kick command.
     * 
     * @param sender
     *            The CommandSender executing this command.
     * @param args
     *            The command arguments.
     * @return False if a usage message should be displayed.
     */
    private boolean commandKick(CommandSender sender, String[] args) {
        if (args.length == 1)
            sender.sendMessage(plugin.messages
                    .getString("command.kick.reasonRequired"));
        if (args.length > 1) {
            Player victim = sender.getServer().getPlayer(args[0]);
            if (victim != null) {
                // Set up kick message
                String banReason = StringUtils.join(
                        Arrays.copyOfRange(args, 1, args.length), ' ');

                // Log kick to console
                plugin.getLogger().info(
                        MessageFormat.format(
                                plugin.messages
                                        .getString("command.kick.consoleLog"),
                                new Object[] { sender.getName(),
                                        victim.getName() }));

                // Do the kick and record it
                Kick k = new Kick();
                k.apply(plugin.messages, victim, sender, banReason);
                plugin.getDatabase().insert(k);
            } else
                sender.sendMessage(plugin.messages
                        .getString("error.playerNotFound"));
            return true;
        }
        return false;
    }

    /**
     * Handles the /ban command.
     * 
     * @param sender
     *            The CommandSender executing this command.
     * @param args
     *            The command arguments.
     * @return False if a usage message should be displayed.
     */
    private boolean commandBan(CommandSender sender, String[] args) {
        if (args.length == 1)
            sender.sendMessage(plugin.messages
                    .getString("command.ban.reasonRequired"));
        if (args.length > 1) {
            Player victim = sender.getServer().getPlayer(args[0]);
            if (victim != null) {
                // Set up ban message
                String banReason = StringUtils.join(
                        Arrays.copyOfRange(args, 1, args.length), ' ');

                // Log ban to console
                plugin.getLogger().info(
                        MessageFormat.format(
                                plugin.messages
                                        .getString("command.ban.consoleLog"),
                                new Object[] { sender.getName(),
                                        victim.getName() }));

                // Do the ban and record it
                Ban b = new Ban();
                b.apply(plugin.messages, victim, sender, banReason,
                        Ban.PERMANENT);
                plugin.getDatabase().insert(b);
            } else
                sender.sendMessage(plugin.messages
                        .getString("error.playerNotFound"));
            return true;
        }
        return false;
    }

    /**
     * Handles the /rap command.
     * 
     * @param sender
     *            The CommandSender executing this command.
     * @param args
     *            The command arguments.
     * @return False if a usage message should be displayed.
     */
    private boolean commandRap(CommandSender sender, String[] args) {
        if (args.length == 1) {
            String name = args[0];
            // Database query: Case insensitive match on player name.
            List<Event> events = plugin.getDatabase().find(Event.class).where()
                    .ieq("subject", name).findList();
            if (events.isEmpty()) {
                sender.sendMessage(MessageFormat.format(
                        plugin.messages.getString("command.rap.clean"), name));
            }
            Iterator<Event> i = events.iterator();
            while (i.hasNext()) {
                sender.sendMessage(i.next().toString());
            }
            return true;
        }
        return false;
    }
}
