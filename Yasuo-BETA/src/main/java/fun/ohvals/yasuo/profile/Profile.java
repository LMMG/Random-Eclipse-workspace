package fun.ohvals.yasuo.profile;

import fun.ohvals.yasuo.Yasuo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Yasuo was created by dylan on 06/2017.
 * Please do not redistribute without permission of the developer.
 */

public class Profile {

    protected Yasuo yasuo = Yasuo.getYasuo();

    private UUID uuid;
    private String currentName, currentIp;
    private boolean reports, requests, usingStaffchat, inStaffchat;
    private List<String> ips, names;
    private ProfileFile file;

    public Profile(UUID uuid) {
        this.uuid = uuid;
        file = new ProfileFile(uuid.toString());

        if (!file.getConfig().getKeys().contains("UUID")) {
            currentName = yasuo.getProxy().getPlayer(uuid).getName();
            currentIp = yasuo.getProxy().getPlayer(uuid).getAddress().getHostString();
            reports = false;
            requests = false;
            usingStaffchat = false;
            inStaffchat = false;
            ips = new ArrayList<>();
            names = new ArrayList<>();
            ips.add(currentIp);
            names.add(currentName);
            save();
        }
        save();
        currentName = file.getString("CURRENTNAME");
        currentIp = file.getString("CURRENTIP");
        reports = file.getBoolean("REPORTS");
        requests = file.getBoolean("REQUESTS");
        usingStaffchat = file.getBoolean("USINGSTAFFCHAT");
        inStaffchat = file.getBoolean("INSTAFFCHAT");
        ips = file.getStringList("IPS");
        names = file.getStringList("NAMES");
        yasuo.getProfileManager().getProfiles().add(this);
    }

    public Profile(ProxiedPlayer player) {
        this(player.getUniqueId());
    }

    public void save() {
        file.getConfig().set("UUID", uuid.toString());
        file.getConfig().set("CURRENTNAME", currentName);
        file.getConfig().set("CURRENTIP", currentIp);
        file.getConfig().set("REPORTS", reports);
        file.getConfig().set("REQUESTS", requests);
        file.getConfig().set("USINGSTAFFCHAT", usingStaffchat);
        file.getConfig().set("INSTAFFCHAT", inStaffchat);
        file.getConfig().set("IPS", ips);
        file.getConfig().set("NAMES", names);
        file.save();
    }

    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }

    public void setReports(boolean reports) {
        this.reports = reports;
    }

    public void setRequests(boolean requests) {
        this.requests = requests;
    }

    public void setUsingStaffchat(boolean usingStaffchat) {
        this.usingStaffchat = usingStaffchat;
    }

    public void setInStaffchat(boolean inStaffchat) {
        this.inStaffchat = inStaffchat;
    }

    public void addIp(String ip) {
        if (!ips.contains(ip)) {
            ips.add(ip);
        }
    }

    public void addName(String name) {
        if (!names.contains(name)) {
            names.add(name);
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getCurrentName() {
        return currentName;
    }

    public boolean isReports() {
        return reports;
    }

    public boolean isRequests() {
        return requests;
    }

    public boolean isUsingStaffchat() {
        return usingStaffchat;
    }

    public boolean isInStaffchat() {
        return inStaffchat;
    }

    public List<String> getIps() {
        return ips;
    }

    public List<String> getNames() {
        return names;
    }

    public String getCurrentIp() {
        return currentIp;
    }
}