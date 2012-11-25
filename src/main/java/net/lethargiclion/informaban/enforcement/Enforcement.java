package net.lethargiclion.informaban.enforcement;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.UUID;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This base class represents a record of a ban, kick, jailing or other event tracked by InformaBan.
 * @author TerrorBite
 *
 */

@MappedSuperclass
public abstract class Enforcement {
    
    public enum RecordType {
        /**
         * This is a record of a user being kicked.
         */
        KICK,
        /**
         * This is a record of a user being banned.
         */
        BAN,
        /**
         * This is a record of a user being ip-banned.
         */
        IPBAN,
        /**
         * This is a record of a user being unbanned/pardoned.
         */
        UNBAN,
        /**
         * For future expansion. This is a record of a user being jailed.
         */
        JAIL,
        /**
         * This is a comment added manually regarding this user.
         */
        COMMENT;
    }
    
    /**
     * A unique ID for this record (for the database)
     */
    @Id
    @GeneratedValue
    UUID id = null;
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
    protected Enforcement() {
    }
    
    protected Enforcement(String subject, String subjectIP, String issuer, String reason, Date when) throws UnknownHostException {
        this.setSubject(subject);
        this.setSubjectIP(subjectIP);
        this.setEnforcer(issuer);
        this.setReason(reason);
        this.setDateIssued(when);
    }

    /**
     * @return The date and time that the recorded event occurred.
     */
    public Date getDateIssued() {
        return dateIssued;
    }

    /**
     * @param dateIssued The date and time that the recorded event occurred.
     */
    protected void setDateIssued(Date dateIssued) {
        this.dateIssued = dateIssued;
    }

    /**
     * @return The name of the player who is the subject of the recorded event.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject The name of the player who is the subject of the recorded event.
     */
    protected void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return The IP address of the subject.
     */
    public InetAddress getSubjectIP() {
        return subjectIP;
    }

    /**
     * @param subjectIP The IP address of the subject.
     */
    protected void setSubjectIP(InetAddress subjectIP) {
        this.subjectIP = subjectIP;
    }
    
    /**
     * @param subjectIP The string representation of the subject's IP, or the hostname of the subject.
     * @throws UnknownHostException Thrown if the supplied string is not an IP address and is not a hostname that resolves to an IP address.
     */
    protected void setSubjectIP(String subjectIP) throws UnknownHostException {
        this.subjectIP = InetAddress.getByName(subjectIP);
    }

    /**
     * @return The name of the player who enforced this event.
     */
    public String getEnforcer() {
        return enforcer;
    }

    /**
     * @param issuer The name of the player who enforced this event.
     */
    protected void setEnforcer(String issuer) {
        this.enforcer = issuer;
    }

    /**
     * @return The reason for enforcement.
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param The reason for enforcement.
     */
    protected void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Enforces this event upon the subject.
     */
    protected boolean enforce(Player subject, CommandSender enforcer, String reason) {
        if(dateIssued != null) return false;
        dateIssued = new Date();
        this.subject = subject.getName();
        this.subjectIP = subject.getAddress().getAddress();
        this.enforcer = enforcer.getName();
        this.reason = reason;
        return true;
    }
    
}
