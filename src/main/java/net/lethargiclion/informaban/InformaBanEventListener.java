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

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class InformaBanEventListener implements Listener {

	@SuppressWarnings("unused")
    private InformaBan plugin;
	
	public InformaBanEventListener(InformaBan plugin) {
		this.plugin = plugin;
	}


	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
	    //Example ban code (for now just testing message formatting)
	    
	    return;
	    /*
	    String[] message = new String[3];
	    
	    String serverName = "this server";
	    String banReason = "Example reason";
	    String bannedBy = "Admin";
	    boolean tempban = r.nextBoolean();
	    String banExpiry = "3 hours, 14 minutes";
	    
		message[0] = String.format(" %sYou are %sbanned from %s!", ChatColor.GOLD, tempban?"":"PERMANENTLY ", serverName);
		message[1] = String.format("     Reason: %s%s%s", ChatColor.GRAY, ChatColor.ITALIC, banReason);
		message[2] = String.format("       %sYour ban (placed by %s%s%s) %s.", ChatColor.GRAY, ChatColor.WHITE, bannedBy, ChatColor.GRAY,
		        tempban ? ("expires in "+banExpiry) : "will not expire");
		
		event.setKickMessage(StringUtils.join(message, '\n'));
		event.setResult(Result.KICK_BANNED);
		*/
	}
}
