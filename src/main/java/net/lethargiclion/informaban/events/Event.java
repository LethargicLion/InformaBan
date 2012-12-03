package net.lethargiclion.informaban.events;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This base class represents a record of a ban, kick, jailing or other event
 * tracked by InformaBan.
 * 
 * @author TerrorBite
 * 
 */

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "ib_events")
public abstract class Event {

    /**
     * A unique ID for this record (for the database)
     */
    @Id
    @GeneratedValue
    UUID id = null;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * The date and time that the recorded event occurred.
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date dateIssued = null;
    /**
     * The name of the player who is the subject of the recorded event.
     */
    private String subject = null;
    /**
     * The string representation of the IP address of the subject.
     */
    private String subjectIP = null;
    /**
     * The name of the player who enforced this event.
     */
    private String enforcer = null;
    /**
     * The reason for enforcement.
     */
    private String reason = null;

    /**
     * Creates a "blank" Event. Required by Ebeans.
     */
    protected Event() {
    }

    /**
     * @return The date and time that the recorded event occurred.
     */
    public Date getDateIssued() {
        return dateIssued;
    }

    /**
     * @param dateIssued
     *            The date and time that the recorded event occurred.
     */
    public void setDateIssued(Date dateIssued) {
        this.dateIssued = dateIssued;
    }

    /**
     * @return The name of the player who is the subject of the recorded event.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject
     *            The name of the player who is the subject of the recorded
     *            event.
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return The string representation of the IP address of the subject.
     */
    public String getSubjectIP() {
        return subjectIP;
    }

    /**
     * @param subjectIP
     *            The string representation of the IP address of the subject.
     */
    public void setSubjectIP(String subjectIP) {
        this.subjectIP = subjectIP;
    }

    /**
     * @return The name of the player who enforced this event.
     */
    public String getEnforcer() {
        return enforcer;
    }

    /**
     * @param issuer
     *            The name of the player who enforced this event.
     */
    public void setEnforcer(String issuer) {
        this.enforcer = issuer;
    }

    /**
     * @return The reason for enforcement.
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason
     *            The reason for enforcement.
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Applies this event to an online subject.
     */
    protected boolean apply(Player subject, CommandSender enforcer,
            String reason) {
        if (getDateIssued() != null)
            return false;
        setDateIssued(new Date());
        this.subject = subject.getName();
        this.subjectIP = subject.getAddress().getAddress().getHostAddress();
        this.enforcer = enforcer.getName();
        this.reason = reason;
        return true;
    }
    
    /**
     * Applies this event to an offline subject. No IP is recorded.
     */
    protected boolean apply(String subject, CommandSender enforcer,
            String reason) {
        if (getDateIssued() != null)
            return false;
        setDateIssued(new Date());
        this.subject = subject;
        this.enforcer = enforcer.getName();
        this.reason = reason;
        return true;
    }

    @Override
    public String toString() {
        return String.format("Placed by %s against %s on %s (IP: %s)",
                getEnforcer(), getSubject(),
                DateFormat.getInstance().format(getDateIssued()),
                getSubjectIP());
    }
}
