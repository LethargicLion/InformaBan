package net.lethargiclion.informaban;

import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.lethargiclion.informaban.persistence.Database.RecordType;

public class Ban extends TimedEnforcement {

    public Ban() {
        super(RecordType.BAN);
        // TODO Auto-generated constructor stub
    }
    
    public Ban(String subject, String subjectIP, String issuer, String reason, Date when, int duration) throws UnknownHostException {
        super(RecordType.BAN, subject, subjectIP, issuer, reason, when, duration);
    }
    
    public void enforce(Player subject, Player enforcer, String reason, int duration) 
    {
        super.enforce(subject, enforcer, reason, duration);
        
        ResourceBundle messages = ResourceBundle.getBundle("Messages", InformaBan.getLocale());
        
        String[] message = new String[3];
        
        String serverName = "this server";
        
        message[0] = String.format(" %s%s has %sbanned you from %s!", ChatColor.GOLD, duration!=0?"":"PERMANENTLY ", serverName);
        message[1] = String.format("     Reason: %s%s%s", ChatColor.GRAY, ChatColor.ITALIC, reason);
        message[2] = String.format("       %sYour ban will %s.", ChatColor.GRAY, ChatColor.WHITE, enforcer.getName(), ChatColor.GRAY,
                duration!=0 ? (String.format("expire in %d seconds", duration)) : "NOT expire");
        
        subject.kickPlayer(StringUtils.join(message, '\n'));
    }

}
