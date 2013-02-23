package net.lethargiclion.informaban.events;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import net.lethargiclion.informaban.InformaBan;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Entity()
@DiscriminatorValue("KICK")
public class Kick extends Event {

    public Kick() {
    }

    /**
     * Actually kick the player (and record it).
     * @param messages The ResourceBundle containing localized strings.
     * @param subject The player to be kicked.
     * @param enforcer The player doing the kicking.
     * @param reason The reason the subject is being kicked.
     * @return true if successful.
     */
    public boolean apply(ResourceBundle messages, Player subject,
            CommandSender enforcer, String reason) {

        // Don't do anything if it's already enforced
        if (this.getDateIssued() != null)
            return false;

        // Record it
        super.apply(subject, enforcer, reason);

        String[] message = new String[3];
        // Construct kick message
        String kickMsg = new MessageFormat(
                messages.getString("banmsg.kickedby"), InformaBan.getLocale())
                .format(new Object[] { enforcer.getName() });
        message[0] = String.format("%s%s", ChatColor.GOLD, kickMsg);
        message[1] = String.format("%s: %s%s%s",
                messages.getString("banmsg.reason"), ChatColor.GRAY,
                ChatColor.ITALIC, reason);

        // Do the kick
        subject.kickPlayer(StringUtils.join(message, '\n'));

        return true;
    }

    @Override
    public String toString() {
        return String.format("%s: %s kicked %s: %s", DateFormat.getInstance()
                .format(getDateIssued()), getEnforcer(), getSubject(),
                getReason());
    }

}
