package net.lethargiclion.informaban.persistence;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.validation.Length;
import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;

@Entity()
@Table(name="ib_events")
public class Event {
    
    @Id
    private long dateIssued;
    
    @NotNull
    private byte type;
    
    @NotNull
    private String subject;
    
    @NotNull
    private String issuer;
    
    @Length(max=128)
    @NotEmpty
    private String reason;
    
    /**
     * Default constructor required by Ebeans
     */
    public Event() {};
    
    /**
     * Convenience constructor
     * @param date Date this event occurred, in milliseconds since the Epoch
     * @param type Type of this event
     * @param subject Name of player who is the subject of this event
     * @param issuer Name of player issuing this event
     * @param reason The reason for this event
     */
    public Event(long date, byte type, String subject, String issuer, String reason) {
        this.setDate(date);
        this.type = type;
        this.subject = subject;
        this.issuer = issuer;
        this.reason = reason;
    }

    /**
     * @return the date
     */
    public long getDate() {
        return dateIssued;
    }

    /**
     * @param date the date to set
     */
    public void setDate(long date) {
        this.dateIssued = date;
    }

    /**
     * @return the type
     */
    public byte getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(byte type) {
        this.type = type;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the issuer
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * @param issuer the issuer to set
     */
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
    
    

}
