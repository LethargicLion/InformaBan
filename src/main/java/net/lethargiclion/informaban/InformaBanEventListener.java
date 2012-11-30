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

import java.util.Iterator;
import java.util.List;

import net.lethargiclion.informaban.events.Ban;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class InformaBanEventListener implements Listener {

    private InformaBan plugin;
	
	public InformaBanEventListener(InformaBan plugin) {
		this.plugin = plugin;
	}


	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
	    
	    try {
	        checkBans(event);
	    }
        catch(java.util.MissingResourceException e) {
            plugin.getLogger().severe("[InformaBan] An internal error occured. Please file a bug report.");
            plugin.getLogger().severe(String.format("[InformaBan] Error: No message defined for \"%s\".", e.getKey()));
        }

	}
	
	private void checkBans(PlayerLoginEvent event) {
	    	    // TODO: Keep a separate table of active bans for efficiency
        List<Ban> bans = plugin.getDatabase().find(Ban.class).where().eq("subject", event.getPlayer().getName()).findList();
        Iterator<Ban> i = bans.iterator();
        
        while(i.hasNext()) {
            Ban b = i.next();
            if(b.isActive()) {
                event.setKickMessage(b.getBanMessage());
                event.setResult(Result.KICK_BANNED);
                break;
            }
        }
	    
	    return;
	}
}
