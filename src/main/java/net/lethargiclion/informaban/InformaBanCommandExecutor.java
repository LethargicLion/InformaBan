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

import net.lethargiclion.informaban.events.ActiveBan;
import net.lethargiclion.informaban.events.Ban;
import net.lethargiclion.informaban.events.Event;
import net.lethargiclion.informaban.events.IPBan;
import net.lethargiclion.informaban.events.Kick;
import net.lethargiclion.informaban.events.Unban;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.net.InetAddresses;

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
            if (command.getName().equalsIgnoreCase("ipban"))
                return commandIPBan(sender, args);
            if (command.getName().equalsIgnoreCase("unban"))
                return commandUnban(sender, args);
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
                                new Object[] {sender.getName(),
                                        victim.getName()}));

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
                                new Object[] {sender.getName(),
                                        victim.getName()}));

                // Do the ban and record it
                Ban b = new Ban();
                b.apply(plugin.messages, victim, sender, banReason,
                        Ban.PERMANENT);
                plugin.getDatabase().insert(b); // Record the banning event
                plugin.getDatabase().insert(b.makeActiveEvent()); // Set the actual ban
            } else
                sender.sendMessage(plugin.messages
                        .getString("error.playerNotFound"));
            return true;
        }
        return false;
    }

    /**
     * Handles the /ipban command.
     * 
     * @param sender
     *            The CommandSender executing this command.
     * @param args
     *            The command arguments.
     * @return False if a usage message should be displayed.
     */
    private boolean commandIPBan(CommandSender sender, String[] args) {

        if (args.length == 1)
            sender.sendMessage(plugin.messages
                    .getString("command.ban.reasonRequired"));

        String subject = null;

        if (args.length > 1) {
            if (InetAddresses.isInetAddress(args[0])) {
                subject = args[0];
            } else {
                subject = sender.getServer().getPlayer(args[0]).getAddress().getAddress().getHostAddress();
            }

            // Check for existing IP ban
            ActiveBan ab = plugin.getDatabase().find(ActiveBan.class).where()
                    .eq("subject", subject)
                    .findUnique();

            if (ab != null) {
                sender.sendMessage(plugin.messages
                        .getString("error.IPAlreadyBanned"));
                return true;
            }

            if (subject != null) {
                // Set up ban message
                String banReason = StringUtils.join(
                        Arrays.copyOfRange(args, 1, args.length), ' ');

                // Log ban to console
                plugin.getLogger().info(
                        MessageFormat.format(
                                plugin.messages
                                        .getString("command.ban.consoleLog"),
                                new Object[] {sender.getName(),
                                        subject}));

                // Do the ban and record it
                IPBan b = new IPBan();
                b.apply(plugin.messages, subject, sender, banReason,
                        Ban.PERMANENT);
                plugin.getDatabase().insert(b); // Record the banning event
                plugin.getDatabase().insert(b.makeActiveEvent()); // Set the actual ban
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
            else {
                sender.sendMessage(MessageFormat.format(
                        plugin.messages.getString("command.rap.ban"), name));
            }
            Iterator<Event> i = events.iterator();
            while (i.hasNext()) {
                sender.sendMessage(i.next().toString());
            }
            
            // Check for online player, and get bans matching IP address
            Player p = plugin.getServer().getPlayer(name);

            if (p != null) {
                String ipaddress = p.getAddress().getAddress().getHostAddress();

                if (ipaddress != null) {
                    List<Event> ipevents = plugin.getDatabase().find(Event.class).where()
                            .disjunction()
                            .eq("subjectIP", ipaddress)
                            .findList();

                    if (!ipevents.isEmpty()) {
                        sender.sendMessage(MessageFormat.format(
                                plugin.messages.getString("command.rap.ip"), ipaddress));
                    }
                    Iterator<Event> j = ipevents.iterator();

                    while (j.hasNext()) {
                        sender.sendMessage(j.next().toString());
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Handles the /unban command.
     * 
     * @param sender
     *            The CommandSender executing this command.
     * @param args
     *            The command arguments.
     * @return False if a usage message should be displayed.
     */
    private boolean commandUnban(CommandSender sender, String[] args) {
        if (args.length == 1)
            sender.sendMessage(plugin.messages
                    .getString("command.unban.reasonRequired"));
        if (args.length > 1) {
            String exconvict = args[0];
            ActiveBan ab;
            if ((ab=plugin.getDatabase().find(ActiveBan.class).where().ieq("subject", exconvict).findUnique()) != null) {
                // Set up ban message
                String unbanReason = StringUtils.join(
                        Arrays.copyOfRange(args, 1, args.length), ' ');

                // Log ban to console
                plugin.getLogger().info(
                        MessageFormat.format(
                                plugin.messages
                                        .getString("command.unban.consoleLog"),
                                new Object[] {sender.getName(),
                                        ab.getSubject()}));

                // Do the unban and record it
                Unban b = new Unban();
                b.apply(ab, sender, unbanReason);
                plugin.getDatabase().insert(b); // Record the unbanning event
                plugin.getDatabase().delete(ab); // Remove the actual ban
            } else
                sender.sendMessage(plugin.messages
                        .getString("error.playerNotBanned"));
            return true;
        }
        return false;
    }
}
