package net.lethargiclion.informaban;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.bukkit.entity.Player;

import net.lethargiclion.informaban.persistence.Database.RecordType;

/**
 * This base class represents a record of a ban, kick, jailing or other event tracked by InformaBan.
 * @author TerrorBite
 *
 */
public abstract class Enforcement {
    
    /**
     * The type of this record.
     */
    private RecordType type;
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
     * Creates a "blank" Enforcement of the given type.
     */
    protected Enforcement(RecordType type) {
        this.setType(type);
    }
    
    protected Enforcement(RecordType type, String subject, String subjectIP, String issuer, String reason, Date when) throws UnknownHostException {
        this(type);
        this.setSubject(subject);
        this.setSubjectIP(subjectIP);
        this.setEnforcer(issuer);
        this.setReason(reason);
        this.setDateIssued(when);
    }

    /**
     * @return the type of this record
     */
    public RecordType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    private void setType(RecordType type) {
        this.type = type;
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
    protected boolean enforce(Player subject, Player enforcer, String reason) {
        if(dateIssued != null) return false;
        dateIssued = new Date();
        this.subject = subject.getName();
        this.subjectIP = subject.getAddress().getAddress();
        this.enforcer = enforcer.getName();
        this.reason = reason;
        return true;
    }
    
}
