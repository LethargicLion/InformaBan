package net.lethargiclion.informaban;

import java.net.UnknownHostException;
import java.util.Date;

import org.bukkit.entity.Player;

import net.lethargiclion.informaban.persistence.Database.RecordType;

public class TimedEnforcement extends Enforcement {
    
    private int duration;
    
    private boolean active;
    
    protected TimedEnforcement(RecordType type) {
        super(type);
    }

    protected TimedEnforcement(RecordType type, String subject,
            String subjectIP, String issuer, String reason, Date when, int duration)
            throws UnknownHostException {
        super(type, subject, subjectIP, issuer, reason, when);
        setDuration(duration);
    }

    protected void enforce(Player subject, Player enforcer, String reason, int duration) 
    {
        super.enforce(subject, enforcer, reason);
        setDuration(duration);
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
