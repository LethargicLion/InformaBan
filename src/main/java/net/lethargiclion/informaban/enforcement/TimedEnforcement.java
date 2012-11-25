package net.lethargiclion.informaban.enforcement;

import java.net.UnknownHostException;
import java.util.Date;

import javax.persistence.MappedSuperclass;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@MappedSuperclass
public abstract class TimedEnforcement extends Enforcement {
    
    private int duration;
    
    private boolean active;
    
    protected TimedEnforcement() {
    }

    protected TimedEnforcement(String subject, String subjectIP, String issuer, String reason,
            Date when, int duration) throws UnknownHostException {
        super(subject, subjectIP, issuer, reason, when);
        setDuration(duration);
    }

    protected boolean enforce(Player subject, CommandSender enforcer, String reason, int duration) 
    {
        if(!super.enforce(subject, enforcer, reason)) return false;
        setDuration(duration);
        return true;
    }
    
    public boolean isActive() {
        if(!this.active) return false;
        Date ends = new Date(this.getDateIssued().getTime()+duration*1000);
        if(ends.before(new Date())) return false;
        return true;
    }

    /**
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    protected void setDuration(int duration) {
        this.duration = duration;
    }


}
