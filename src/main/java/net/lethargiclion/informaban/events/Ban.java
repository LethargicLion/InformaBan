package net.lethargiclion.informaban.events;

import java.text.DateFormat;
import java.util.ResourceBundle;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Entity()
@DiscriminatorValue("BAN")
public class Ban extends TimedEvent {

    public Ban() {
    }

    /**
     * Enforce this ban.
     * 
     * Note that this performs the initial enforcement of the ban, e.g. when the
     * ban was initially placed.
     * 
     * @param subject
     *            The player being banned.
     * @param enforcer
     *            The player (or console) performing the ban.
     * @param reason
     *            The reason given for the ban.
     * @param duration
     *            The length of the ban in seconds, or 0 for a permanent ban.
     * @return true if successfully enforced; false if the ban has already been
     *         enforced.
     */
    public boolean apply(ResourceBundle messages, Player subject,
            CommandSender enforcer, String reason, int duration) {
        if (this.getDateIssued() != null)
            return false;

        super.apply(subject, enforcer, reason, duration);

        // ResourceBundle messages = ResourceBundle.getBundle("Messages",
        // InformaBan.getLocale());

        String[] message = new String[3];

        String serverName = "this server";

        message[0] = String.format("%s%s has %sbanned you from %s!",
                ChatColor.GOLD, enforcer.getName(),
                isPermanent() ? "PERMANENTLY " : "", serverName);
        message[1] = String.format("Reason: %s%s%s", ChatColor.GRAY,
                ChatColor.ITALIC, reason);
        message[2] = String.format(
                "%sYour ban will %s.",
                ChatColor.GRAY,
                isPermanent() ? "NOT expire" : (String.format(
                        "expire in %d seconds", duration)));

        subject.kickPlayer(StringUtils.join(message, '\n'));
        return true;
    }

    /**
     * Fetches the string that should be used as the kick message if the subject
     * of this ban attempts to log in.
     * 
     * @return
     */
    public String getMessage() {
        String[] message = new String[3];
        String serverName = "this server";

        message[0] = String
                .format("%sYou are %sbanned from %s!", ChatColor.GOLD,
                        isPermanent() ? "PERMANENTLY " : "", serverName);
        message[1] = String.format("Reason: %s%s%s", ChatColor.GRAY,
                ChatColor.ITALIC, getReason());
        message[2] = String.format(
                "%sYour ban (placed by %s%s%s) will %s.",
                ChatColor.GRAY,
                ChatColor.WHITE,
                this.getEnforcer(),
                ChatColor.GRAY,
                getDuration() != 0 ? String.format("expire in %d seconds",
                        getDuration()) : "NOT expire");

        return StringUtils.join(message, '\n');

    }

    @Override
    public String toString() {
        if(isPermanent()) return String.format("%s: %s permanently banned %s: %s",
                DateFormat.getInstance().format(getDateIssued()),
                getEnforcer(), getSubject(),
                getReason());
            
        else return String.format("%s: %s banned %s for %d seconds: %s",
                DateFormat.getInstance().format(getDateIssued()),
                getEnforcer(), getSubject(),
                getDuration(), getReason());
    }

    @Override
    /**
     * Process actions to be taken when the user's ban has expired.
     * Nothing to do here for a ban.
     */
    public void onExpire() {

    }

    @Override
    public ActiveEvent makeActiveEvent() {
        ActiveEvent ab = new ActiveBan();
        ab.setParent(this);
        ab.setSubject(getSubject());
        ab.setExpiry(getExpiryDate());
        return ab;
    }

}
