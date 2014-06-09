package net.lethargiclion.informaban.events;

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

import com.avaje.ebean.validation.NotNull;

/**
 * Represents an ongoing non-ban event such as a jailing. Contains a reference to
 * the actual record of the event (which will be an Event).
 * 
 * NOTE: Ongoing bans are recorded using Bukkit's built-in ban system. ActiveEvent
 * is used used for ongoing events that have no standard system in place (jails, etc).
 * 
 * @author TerrorBite
 * 
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "ib_active")
public abstract class ActiveEvent {

    protected ActiveEvent() {
    }

    /**
     * A unique ID for this record (for the database)
     */
    @Id
    @GeneratedValue
    UUID id = null;

    /**
     * A reference to the parent record for this active event
     */
    // @ManyToOne(cascade=CascadeType.ALL)
    transient TimedEvent parent;

    UUID parentId;

    /**
     * The date at which the event expires
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    Date expiry;

    /**
     * The subject of this active event. This is usually a player name, but may
     * also be an IP address (in the case of an IP ban).
     */
    @NotNull
    String subject;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TimedEvent getParent() {
        if(parent != null) return parent;
        parent = org.bukkit.Bukkit.getPluginManager().getPlugin("InformaBan")
                .getDatabase().find(TimedEvent.class).where()
                .eq("id", getParentId()).findUnique();
        return parent;
    }

    public void setParent(TimedEvent parent) {
        this.parent = parent;
        this.parentId = parent.getId();
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Find out if this event is actually still active or not.
     * 
     * @return True if the event is still active, otherwise false.
     */
    public boolean isActive() {
        if(this.expiry == null) return true;
        return getExpiry().before(new Date());
    }

}
