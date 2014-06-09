package net.lethargiclion.informaban.events;

import java.text.DateFormat;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.google.common.net.InetAddresses;

@Entity()
@DiscriminatorValue("UNBAN")
public class Unban extends Event {

    public Unban() {}

    public boolean apply(ActiveBan ban,
            CommandSender enforcer, String reason) {

        // Don't do anything if it's already applied
        if (this.getDateIssued() != null)
            return false;

        // Record details
        super.apply(ban.subject, enforcer, reason);
        
        if (InetAddresses.isInetAddress(ban.subject)) {
            Bukkit.unbanIP(ban.subject);
        } else {
            // Record as unbanned - since banned player must be offline        
            Bukkit.getOfflinePlayer(ban.subject).setBanned(false);
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s: %s unbanned %s: %s",
                DateFormat.getInstance().format(getDateIssued()),
                getEnforcer(), getSubject(), getReason());
    }

}
