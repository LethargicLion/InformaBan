package net.lethargiclion.informaban.events;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.bukkit.command.CommandSender;

@Entity()
@DiscriminatorValue("UNBAN")
public class Unban extends Event {
    
    public Unban() {
    }
    
    public boolean apply(ActiveBan ban,
            CommandSender enforcer, String reason) {

        // Don't do anything if it's already applied
        if (this.getDateIssued() != null)
            return false;

        // Record details
        super.apply(ban.subject, enforcer, reason);

        return true;
    }

}
