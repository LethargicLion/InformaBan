package net.lethargiclion.informaban.events;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

import javax.persistence.*;

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
    private Date dateIssued = null;
    /**
     * The name of the player who is the subject of the recorded event.
     */
    private String subject = null;
    /**
     * The IP address of the subject.
     */
    private InetAddress subjectIP = null;
    /**
     * The name of the player who enforced this event.
     */
    private String enforcer = null;
    /**
     * The reason for enforcement.
     */
    private String reason = null;

    /**
     * Creates a "blank" Enforcement with no type.
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
     * @return The IP address of the subject.
     */
    public InetAddress getSubjectIP() {
        return subjectIP;
    }

    /**
     * @param subjectIP
     *            The IP address of the subject.
     */
    public void setSubjectIP(InetAddress subjectIP) {
        this.subjectIP = subjectIP;
    }

    /**
     * @param subjectIP
     *            The string representation of the subject's IP, or the hostname
     *            of the subject.
     * @throws UnknownHostException
     *             Thrown if the supplied string is not an IP address and is not
     *             a hostname that resolves to an IP address.
     */
    public void setSubjectIP(String subjectIP) throws UnknownHostException {
        this.subjectIP = InetAddress.getByName(subjectIP);
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
     * @param The
     *            reason for enforcement.
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Enforces this event upon the subject.
     */
    protected boolean apply(Player subject, CommandSender enforcer,
            String reason) {
        if (getDateIssued() != null)
            return false;
        setDateIssued(new Date());
        this.subject = subject.getName();
        this.subjectIP = subject.getAddress().getAddress();
        this.enforcer = enforcer.getName();
        this.reason = reason;
        return true;
    }

    @Override
    public String toString() {
        return String.format("Placed by %s against %s on %s", getEnforcer(),
                getSubject(), DateFormat.getInstance().format(getDateIssued()));
    }
}
