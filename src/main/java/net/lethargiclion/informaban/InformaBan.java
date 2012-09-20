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

import java.util.logging.Logger;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class InformaBan extends JavaPlugin {
    
public static Logger log;

	//ClassListeners
	private final InformaBanCommandExecutor commandExecutor = new InformaBanCommandExecutor(this);
	private final InformaBanEventListener eventListener = new InformaBanEventListener(this);
	//ClassListeners
	
	public InformaBan() {
	    log = org.bukkit.Bukkit.getLogger();
	}

	public void onDisable() {
		// add any code you want to be executed when your plugin is disabled
	}

	public void onEnable() { 

		PluginManager pm = this.getServer().getPluginManager();

		PluginCommand ib = getCommand("ib"); 
		if(ib != null)
		    ib.setExecutor(commandExecutor);
		else {
		    log.severe("Failed to hook /ib command - this shouldn't happen! InformaBan cannot continue..");
		    pm.disablePlugin(this);
		}
		
		PluginCommand kick = getCommand("kick");
		if(kick != null)  kick.setExecutor(commandExecutor);
	    else log.warning("Failed to register /kick command - is another plugin overriding it?");
		
        PluginCommand ban = getCommand("ban");
        if(ban != null)  ban.setExecutor(commandExecutor);
        else log.warning("Failed to register /ban command - is another plugin overriding it?");
        
        PluginCommand rap = getCommand("rap");
        if(rap != null)  rap.setExecutor(commandExecutor);
        else log.warning("Failed to register /rap command - is another plugin overriding it?");


		// you can register multiple classes to handle events if you want
		// just call pm.registerEvents() on an instance of each class
		pm.registerEvents(eventListener, this);

		// do any other initialisation you need here...
	}
}
