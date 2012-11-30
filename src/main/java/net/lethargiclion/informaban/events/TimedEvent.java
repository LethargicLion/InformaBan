package net.lethargiclion.informaban.events;

import java.net.UnknownHostException;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Entity
public abstract class TimedEvent extends Event {

    /**
     * This duration value indicates that the event does not expire.
     */
    public static final int PERMANENT = 0;

    private int duration;

    @Transient
    private boolean active = true;

    protected TimedEvent() {
    }

    protected TimedEvent(String subject, String subjectIP, String issuer,
            String reason, Date when, int duration) throws UnknownHostException {
        super(subject, subjectIP, issuer, reason, when);
        setDuration(duration);
    }

    protected boolean enforce(Player subject, CommandSender enforcer,
            String reason, int duration) {
        if (!super.enforce(subject, enforcer, reason))
            return false;
        setDuration(duration);
        return true;
    }

    public boolean isActive() {
        if (this.duration == PERMANENT)
            return true;
        if (!this.active)
            return false;
        Date ends = new Date(this.getDateIssued().getTime() + duration * 1000);
        if (ends.before(new Date()))
            return false;
        return true;
    }

    /**
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    public boolean isPermanent() {
        return duration == PERMANENT;
    }

    /**
     * @param duration
     *            the duration to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

}
