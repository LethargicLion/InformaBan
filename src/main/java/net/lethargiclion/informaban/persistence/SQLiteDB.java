package net.lethargiclion.informaban.persistence;

import java.net.InetAddress;
import java.util.List;

import net.lethargiclion.informaban.Enforcement;

import org.bukkit.entity.Player;

public class SQLiteDB implements Database {

    @Override
    public boolean record(Enforcement e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<Enforcement> getCurrent(String subject) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Enforcement> getAll(String subject) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Enforcement> getIssuedBy(String enforcer) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isBanned(Player subject) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isIpBanned(InetAddress addr) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isJailed(Player subject) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean deactivate(Enforcement e) {
        // TODO Auto-generated method stub
        return false;
    }

}
