package net.lethargiclion.informaban.persistence;

import java.util.Date;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class Database {
    
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

    public abstract boolean isBanned(String username);
    
    /**
     * Makes a record of an event that occurred at the current time.
     * @param subject The username of the player who was kicked, banned etc.
     * @param issuer The username of the player who issued the event.
     * @param reason The reason supplied by the issuer.
     * @param duration The duration that the ban should remain in force.
     * @param playerip The string representation of the player's IP address at the time of the event.
     * @return true if the record was successfully created.
     */
    protected abstract boolean recordEntry(RecordType type, String subject, String issuer, String reason, int duration, String playerip);
    
    /**
     * Makes a record of an event that occurred at an arbritrary time.
     * @param subject The username of the player who was kicked, banned etc.
     * @param issuer The username of the player who issued the event.
     * @param reason The reason supplied by the issuer.
     * @param duration The duration that the ban should remain in force.
     * @param playerip The string representation of the player's IP address at the time of the event.
     * @param date The date and time that this event occurred.
     * @return true if the record was successfully created.
     */
    protected abstract boolean recordEntry(RecordType type, String subject, String issuer, String reason, int duration, String playerip, Date date);
    
    /**
     * Records a kick event into the database.
     * @param sender The CommandSender that executed the kick command.
     * @param subject The player being kicked.
     * @param reason The reason supplied for the kick.
     * @return true if recorded successfully.
     */
    public boolean recordKick(CommandSender sender, Player subject, String reason) {
        
        return recordEntry(RecordType.KICK, sender.getName(), subject.getName(), reason, 0,
                subject.getAddress().getAddress().getHostAddress());
    }
    
    /**
     * Records a ban event into the database.
     * @param sender The CommandSender that executed the ban command.
     * @param subject The player being banned.
     * @param reason The reason supplied for the ban.
     * @param duration The length of the ban, in seconds, or -1 for a permanent ban.
     * @return true if recorded successfully.
     */
    public boolean recordBan(CommandSender sender, Player subject, String reason, int duration) {
        
        return recordEntry(RecordType.BAN, sender.getName(), subject.getName(), reason, duration,
                subject.getAddress().getAddress().getHostAddress());
    }

     /**
     * Records a player IP ban event into the database.
     * @param sender The CommandSender that executed the ban command.
     * @param subject The player being banned.
     * @param reason The reason supplied for the ban.
     * @param duration The length of the ban, in seconds, or -1 for a permanent ban.
     * @return true if recorded successfully.
     */
    public boolean recordIpBan(CommandSender sender, Player subject, String reason, int duration) {
        
        return recordEntry(RecordType.BAN, sender.getName(), subject.getName(), reason, duration,
                subject.getAddress().getAddress().getHostAddress());
    }
    
    /**
    * Records a player IP ban event into the database.
    * @param sender The CommandSender that executed the ban command.
    * @param ip The IP or range being banned, in dotted-quad or CIDR form.
    * @param reason The reason supplied for the ban.
    * @param duration The length of the ban, in seconds, or -1 for a permanent ban.
    * @return true if recorded successfully.
    */
   public boolean recordIpBan(CommandSender sender, String ip, String reason, int duration) {
       
       return recordEntry(RecordType.BAN, sender.getName(), null, reason, duration, ip);
   }
    
   /**
    * For future use.
    * Records a jail event into the database.
    * @param sender The CommandSender that executed the jail command.
    * @param subject The player being banned.
    * @param reason The reason supplied for the ban.
    * @param duration The length of the ban, in seconds, or -1 for a permanent ban.
    * @return true if recorded successfully.
    */
    public boolean recordJail(CommandSender sender, Player subject, String reason, int duration) {
        
        return recordEntry(RecordType.JAIL, sender.getName(), subject.getName(), reason, duration,
                subject.getAddress().getAddress().getHostAddress());
    }
    
}
