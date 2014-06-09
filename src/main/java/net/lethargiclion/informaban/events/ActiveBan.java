package net.lethargiclion.informaban.events;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity()
@DiscriminatorValue("BAN")
/**
 * Represents a currently-active ban.
 * 
 * Deprecated: The built-in ban system is now used to check for ongoing bans.
 * @author TerrorBite
 *
 */
@Deprecated
public class ActiveBan extends ActiveEvent {
    
    public ActiveBan() {
    }

    // Nothing to do here! It's all in the superclass.
}
