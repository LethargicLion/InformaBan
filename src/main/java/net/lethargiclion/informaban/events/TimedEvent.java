package net.lethargiclion.informaban.events;

import java.util.Date;
import java.util.UUID;

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
    private transient boolean active = true;

    protected TimedEvent() {
    }

    protected boolean apply(Player subject, CommandSender issuer,
            String reason, int duration) {
        if (!super.apply(subject, issuer, reason))
            return false;
        setDuration(duration);
        return true;
    }
    
    protected boolean apply(UUID subject, String subjectName, CommandSender issuer,
            String reason, int duration) {
        if (!super.apply(subject, subjectName, issuer, reason)) {
            return false;
        }
        return true;
    }

    /**
     * Reverses the actions (if any) taken when apply() was called on the
     * parent. Used for example to unjail a player.
     */
    public abstract void onExpire();

    /**
     * Gets the string that should be shown to the subject of this action,
     * explaining the action's details.
     */
    public abstract String getMessage();

    /**
     * Creates an active event suitable for submitting to the database.
     */
    public abstract ActiveEvent makeActiveEvent();

    public boolean isActive() {
        if (this.duration == PERMANENT)
            return true;
        if (!this.active)
            return false;
        if (getExpiryDate().before(new Date())) {
            active = false;
            return false;
        }
        return true;
    }

    /**
     * Gets the expiry date of this timed event.
     * 
     * @return The expiry date, or <c>null</c> if permanent.
     */
    protected Date getExpiryDate() {
        if (isPermanent())
            return null;
        return new Date(this.getDateIssued().getTime() + duration * 1000);
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
