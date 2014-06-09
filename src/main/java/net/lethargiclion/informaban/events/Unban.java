package net.lethargiclion.informaban.events;

import java.text.DateFormat;
import java.util.UUID;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.google.common.net.InetAddresses;

@Entity()
@DiscriminatorValue("UNBAN")
public class Unban extends Event {

    public Unban() {}

    public boolean apply(UUID subject, String subjectName,
            CommandSender enforcer, String reason) {

        // Don't do anything if it's already applied
        if (this.getDateIssued() != null)
            return false;

        // Record details
        super.apply(subject, subjectName, enforcer, reason);
        
        if (InetAddresses.isInetAddress(subjectName)) {
            Bukkit.unbanIP(subjectName);
        } else {
            // Record as unbanned - since banned player must be offline
            // TODO: Eliminate getOfflinePlayer() call
            Bukkit.getOfflinePlayer(subject).setBanned(false);
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
