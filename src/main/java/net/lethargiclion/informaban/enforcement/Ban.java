package net.lethargiclion.informaban.enforcement;

import java.net.UnknownHostException;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Ban extends TimedEnforcement {

    public Ban() {
        super(RecordType.BAN);
        // TODO Auto-generated constructor stub
    }
    
    public Ban(String subject, String subjectIP, String issuer, String reason, Date when, int duration) throws UnknownHostException {
        super(RecordType.BAN, subject, subjectIP, issuer, reason, when, duration);
    }
    
    /**
     * Enforce this ban.
     * 
     * Note that this performs the initial enforcement of the ban, e.g. when the ban was initially placed.
     * @return true if successfully enforced; false if the ban has already been enforced.
     */
    public boolean enforce(Player subject, Player enforcer, String reason, int duration) 
    {
        if(this.getDateIssued() != null) return false;
        
        super.enforce(subject, enforcer, reason, duration);
        
        //ResourceBundle messages = ResourceBundle.getBundle("Messages", InformaBan.getLocale());
        
        String[] message = new String[3];
        
        String serverName = "this server";
        
        message[0] = String.format(" %s%s has %sbanned you from %s!", ChatColor.GOLD, duration!=0?"":"PERMANENTLY ", serverName);
        message[1] = String.format("     Reason: %s%s%s", ChatColor.GRAY, ChatColor.ITALIC, reason);
        message[2] = String.format("       %sYour ban will %s.", ChatColor.GRAY, ChatColor.WHITE, enforcer.getName(), ChatColor.GRAY,
                duration!=0 ? (String.format("expire in %d seconds", duration)) : "NOT expire");
        
        subject.kickPlayer(StringUtils.join(message, '\n'));
        return true;
    }
    
    /**
     * Fetches the string that should be used as the kick message if the subject of this ban attempts to log in.
     * @return
     */
    public String getBanMessage() {
        String[] message = new String[3];
        String serverName = "this server";
        
        message[0] = String.format(" %sYou are %sbanned from %s!", ChatColor.GOLD, getDuration()!=0?"":"PERMANENTLY ", serverName);
        message[1] = String.format("     Reason: %s%s%s", ChatColor.GRAY, ChatColor.ITALIC, this.getReason());
        message[2] = String.format("       %sYour ban (placed by %s%s%s) will %s.", ChatColor.GRAY, ChatColor.WHITE, this.getEnforcer(), ChatColor.GRAY,
                getDuration()!=0 ? String.format("expire in %d seconds", getDuration()) : "NOT expire");
        
        return StringUtils.join(message, '\n');
        
    }

}
