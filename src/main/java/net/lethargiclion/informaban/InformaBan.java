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
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class InformaBan extends JavaPlugin {

	//ClassListeners
	private final InformaBanCommandExecutor commandExecutor = new InformaBanCommandExecutor(this);
	private final InformaBanEventListener eventListener = new InformaBanEventListener(this);
	//ClassListeners
	
	// Locale
	Locale locale;
	
	// Localised messages
	ResourceBundle messages;
	
	public InformaBan() {
	    
	    // Until we get the user's preferred locale from the config file,
	    // use the environmental locale which should be a sensible default.
	    setupLocale(Locale.getDefault());
	    //setupLocale(new Locale("en", "PI")); // English (Pirate) for testing
	    
	}
	
	public void setupLocale(Locale l) {
	    locale = l;
	    messages = ResourceBundle.getBundle("Messages", l);
    }

	public void onDisable() {
		// add any code you want to be executed when your plugin is disabled
	    Logger log = this.getLogger();
	    
	    MessageFormat disableSuccess = new MessageFormat(messages.getString("plugin.disable.success"), locale);
	    log.info(disableSuccess.format(new Object[]{this.getName()}));
	}

	public void onEnable() {
	    
	    Logger log = this.getLogger();

		PluginManager pm = this.getServer().getPluginManager();
		
		MessageFormat msgFailed = new MessageFormat(messages.getString("plugin.enable.commandFailure"), locale);

		PluginCommand ib = getCommand("ib"); 
		if(ib != null) {
		    ib.setExecutor(commandExecutor);
		    // Override the default description and usage from plugin.yml with localized versions
		    ib.setDescription(messages.getString("command.ib.description"));
		    ib.setUsage(messages.getString("command.ib.usage"));
		}
		else {
		    log.severe(msgFailed.format(new Object[]{"/ib"}));
		    pm.disablePlugin(this);
		}
		
		PluginCommand kick = getCommand("kick");
		if(kick != null) {
		    kick.setExecutor(commandExecutor);
		    kick.setDescription(messages.getString("command.kick.description"));
		    kick.setUsage(messages.getString("command.kick.usage"));
		}
	    else log.warning(msgFailed.format(new Object[]{"/kick"}));
		
        PluginCommand ban = getCommand("ban");
        if(ban != null) {
            ban.setExecutor(commandExecutor);
            ban.setDescription(messages.getString("command.ban.description"));
            ban.setUsage(messages.getString("command.ban.usage"));
        }
        else log.warning(msgFailed.format(new Object[]{"/ban"}));
        
        PluginCommand rap = getCommand("rap");
        if(rap != null) {
            rap.setExecutor(commandExecutor);
            rap.setDescription(messages.getString("command.rap.description"));
            rap.setUsage(messages.getString("command.rap.usage"));
        }
        else log.warning(msgFailed.format(new Object[]{"/rap"}));


		// you can register multiple classes to handle events if you want
		// just call pm.registerEvents() on an instance of each class
		pm.registerEvents(eventListener, this);

		// do any other initialisation you need here...
		MessageFormat enableSuccess = new MessageFormat(messages.getString("plugin.enable.success"), locale);
		log.info(enableSuccess.format(new Object[]{this.getName()}));
	}
}
