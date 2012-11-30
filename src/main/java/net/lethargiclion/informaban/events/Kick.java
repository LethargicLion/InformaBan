package net.lethargiclion.informaban.events;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
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
    
    public Kick() {}
    
    public Kick(String subject, String subjectIP, String issuer, String reason, Date when) throws UnknownHostException {
        super(subject, subjectIP, issuer, reason, when);
    }
    
    public boolean enforce(ResourceBundle messages, Player subject, CommandSender enforcer, String reason) {

        // Don't do anything if it's already enforced
        if(this.getDateIssued() != null) return false;
        
        // Record it
        super.enforce(subject, enforcer, reason);
        
        String[] message = new String[3];
        // Construct kick message
        String kickMsg = new MessageFormat(messages.getString("banmsg.kickedby"), InformaBan.getLocale()).format(new Object[]{enforcer.getName()});
        message[0] = String.format("%s%s", ChatColor.GOLD, kickMsg);
        message[1] = String.format("%s: %s%s%s", messages.getString("banmsg.reason"), ChatColor.GRAY, ChatColor.ITALIC, reason);
        
        // Do the kick
        subject.kickPlayer(StringUtils.join(message, '\n'));
        
        return true;
    }
    
    @Override
    public String toString() {
        return String.format("%s kicked %s on %s: %s", getEnforcer(), getSubject(), DateFormat.getInstance().format(getDateIssued()), getReason());
    }

}
