package net.lethargiclion.informaban.events;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity()
@DiscriminatorValue("BAN")
/**
 * Represents a currently-active ban.
 * @author TerrorBite
 *
 */
public class ActiveBan extends ActiveEvent {
    
    public ActiveBan() {
    }

    // Nothing to do here! It's all in the superclass.
}
