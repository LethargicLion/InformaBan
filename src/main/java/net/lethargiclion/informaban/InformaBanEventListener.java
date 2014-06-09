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

import net.lethargiclion.informaban.events.ActiveBan;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

/**
 * InformaBan event listener class. Processes events.
 * 
 * @author TerrorBite
 * 
 */
public class InformaBanEventListener implements Listener {

    private InformaBan plugin;

    /**
     * Creates a new instance of this event listener.
     * 
     * @param plugin
     *            The parent plugin
     */
    public InformaBanEventListener(InformaBan plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the player login event.
     * 
     * @param event
     *            Event to process.
     */
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        if(event.getResult() == Result.KICK_BANNED)
            try {
                checkBans(event);
            } catch (java.util.MissingResourceException e) {
                plugin.getLogger()
                        .severe("[InformaBan] An internal error occured. Please file a bug report.");
                plugin.getLogger()
                        .severe(String
                                .format("[InformaBan] Error: No message defined for \"%s\".",
                                        e.getKey()));
            }

    }

    /**
     * Checks whether this player is banned, and modifies the event accordingly.
     * 
     * @param event
     */
    private void checkBans(PlayerLoginEvent event) {

        // Check for any ban record (player name or IP)
        ActiveBan b = plugin.getDatabase().find(ActiveBan.class).where()
                .disjunction().eq("subject", event.getPlayer().getName())
                .eq("subject", event.getAddress().getHostAddress())
                .findUnique();


        if (b != null) {
            if (b.isActive()) {
                event.setKickMessage(b.getParent().getMessage());
            } else {
                // If expired, remove it from the database
                plugin.getDatabase().delete(b);
                // and allow the player
                event.getPlayer().setBanned(false);
                event.setResult(Result.ALLOWED);
            }
        }
        else { // No record found
            plugin.getLogger().warning(String.format("Player %s is banned, but is not in the database!",
                    event.getPlayer().getName()));
        }

        return;
    }
}
