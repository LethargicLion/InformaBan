package net.lethargiclion.informaban.persistence;

import java.net.InetAddress;
import java.util.List;

import org.bukkit.entity.Player;

import net.lethargiclion.informaban.Enforcement;

public interface Database {
    
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
        JAIL;
    }

    /**
     * Make a record of an Enforcement.
     * @param e The Enforcement to record.
     * @return true if successful.
     */
    public boolean record(Enforcement e);
    
    /**
     * Get all current (i.e. in-effect) Enforcements for the named player.
     * @param subject The player to look up.
     * @return
     */
    public List<Enforcement> getCurrent(String subject);
    
    /**
     * Get all Enforcements, current or past, for the named player.
     * @param subject The player to look up.
     * @return
     */
    public List<Enforcement> getAll(String subject);
    
    /**
     * Gets all Enforcements that were set by the named player.
     * @param enforcer
     * @return
     */
    public List<Enforcement> getIssuedBy(String enforcer);
    
    /**
     * Checks if the given player is currently banned, either by name or by IP.
     * @param subject The online Player to check.
     * @return
     */
    public boolean isBanned(Player subject);
    
    /**
     * Checks if the given address is banned.
     * @param addr The address to check.
     * @return
     */
    public boolean isIpBanned(InetAddress addr);
    
    /**
     * For future expansion. Checks if the given player is currently jailed.
     * @param subject The online Player to check.
     * @return
     */
    public boolean isJailed(Player subject);
    
    /**
     * Deactivate an Enforcement, usually used when a player is unbanned.
     * @param e
     * @return
     */
    public boolean deactivate(Enforcement e);
}
