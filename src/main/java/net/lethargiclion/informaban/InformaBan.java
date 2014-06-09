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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main InformaBan plugin class.
 * @author TerrorBite
 *
 */
public class InformaBan extends JavaPlugin {

    // ClassListeners
    private final InformaBanCommandExecutor commandExecutor = new InformaBanCommandExecutor(
            this);
    private final InformaBanEventListener eventListener = new InformaBanEventListener(
            this);
    // ClassListeners

    private List<Class<?>> ebeans;

    // Locale
    private static Locale locale;

    // Localised messages
    ResourceBundle messages;

    public InformaBan() {

        // Until we get the user's preferred locale from the config file,
        // use the environmental locale which should be a sensible default.
        setLocale(Locale.getDefault());
        // setupLocale(new Locale("en", "PI")); // English (Pirate) for testing

        // Compile list of bean classes
        createDBClassList();

    }

    /**
     * Generate list of database classes for getDatabaseClasses()
     */
    private void createDBClassList() {
        ebeans = new ArrayList<Class<?>>();
        ebeans.add(net.lethargiclion.informaban.events.Event.class);
        ebeans.add(net.lethargiclion.informaban.events.TimedEvent.class);
        ebeans.add(net.lethargiclion.informaban.events.Ban.class);
        ebeans.add(net.lethargiclion.informaban.events.IPBan.class);
        ebeans.add(net.lethargiclion.informaban.events.Unban.class);
        ebeans.add(net.lethargiclion.informaban.events.Kick.class);
        ebeans.add(net.lethargiclion.informaban.events.ActiveEvent.class);
        ebeans.add(net.lethargiclion.informaban.events.ActiveBan.class);
    }

    /**
     * Static method to return this plugin's current locale.
     * 
     * @return
     */
    public static Locale getLocale() {
        return locale;
    }

    /**
     * Sets the locale for this plugin, and loads the messages for it.
     * 
     * @param l
     */
    public void setLocale(Locale l) {
        locale = l;
        messages = ResourceBundle.getBundle("Messages", l);
    }

    public void onDisable() {
        Logger log = this.getLogger();

        // Log successful disable
        MessageFormat disableSuccess = new MessageFormat(
                messages.getString("plugin.disable.success"), locale);
        log.info(disableSuccess.format(new Object[] { this.getName() }));
    }

    public void onEnable() {

        Logger log = this.getLogger();

        PluginManager pm = this.getServer().getPluginManager();

        MessageFormat msgFailed = new MessageFormat(
                messages.getString("plugin.enable.commandFailure"), locale);

        // Attempt to register commands
        PluginCommand ib = getCommand("ib");
        if (ib != null) {
            ib.setExecutor(commandExecutor);
            // Override the default description and usage from plugin.yml with
            // localized versions
            ib.setDescription(messages.getString("command.ib.description"));
            ib.setUsage(messages.getString("command.ib.usage"));
        } else {
            log.severe(msgFailed.format(new Object[] { "/ib" }));
            pm.disablePlugin(this);
        }

        PluginCommand kick = getCommand("kick");
        if (kick != null) {
            kick.setExecutor(commandExecutor);
            kick.setDescription(messages.getString("command.kick.description"));
            kick.setUsage(messages.getString("command.kick.usage"));
        } else
            log.warning(msgFailed.format(new Object[] { "/kick" }));

        PluginCommand ban = getCommand("ban");
        if (ban != null) {
            ban.setExecutor(commandExecutor);
            ban.setDescription(messages.getString("command.ban.description"));
            ban.setUsage(messages.getString("command.ban.usage"));
        } else
            log.warning(msgFailed.format(new Object[] { "/ban" }));

        PluginCommand ipban = getCommand("ipban");
        if (ipban != null) {
            ipban.setExecutor(commandExecutor);
            ipban.setDescription(messages.getString("command.ipban.description"));
            ipban.setUsage(messages.getString("command.ipban.usage"));
        } else
            log.warning(msgFailed.format(new Object[] { "/ban" }));

        PluginCommand unban = getCommand("unban");
        if (unban != null) {
            unban.setExecutor(commandExecutor);
            unban.setDescription(messages.getString("command.unban.description"));
            unban.setUsage(messages.getString("command.unban.usage"));
        } else
            log.warning(msgFailed.format(new Object[] { "/unban" }));

        PluginCommand rap = getCommand("rap");
        if (rap != null) {
            rap.setExecutor(commandExecutor);
            rap.setDescription(messages.getString("command.rap.description"));
            rap.setUsage(messages.getString("command.rap.usage"));
        } else
            log.warning(msgFailed.format(new Object[] { "/rap" }));

        // you can register multiple classes to handle events if you want
        // just call pm.registerEvents() on an instance of each class
        pm.registerEvents(eventListener, this);

        // Set up database
        initDatabase();

        // do any other initialisation you need here...
        MessageFormat enableSuccess = new MessageFormat(
                messages.getString("plugin.enable.success"), locale);
        log.info(enableSuccess.format(new Object[] { this.getName() }));
    }

    /**
     * Handles initialization of the ebeans database for this plugin.
     */
    private void initDatabase() {
        try {
            // Attempt a sample database query. If this fails, we need to initialize our database.
            getDatabase().find(net.lethargiclion.informaban.events.Event.class)
                    .findRowCount();
        } catch (PersistenceException ex) {
            // Database needs init
            this.getLogger().info(messages.getString("plugin.enable.database"));

            // Call base class installDDL() method to init database.
            installDDL();
        }

    }

    /**
     * Return a list of the database classes implemented by this plugin.
     */
    @Override
    public List<Class<?>> getDatabaseClasses() {
        return ebeans;
    }
}
