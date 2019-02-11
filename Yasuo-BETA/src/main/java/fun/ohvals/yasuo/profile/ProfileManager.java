package fun.ohvals.yasuo.profile;

import fun.ohvals.yasuo.Yasuo;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Yasuo was created by dylan on 06/2017.
 * Please do not redistribute without permission of the developer.
 */

public class ProfileManager {

    private Set<Profile> profiles;

    public ProfileManager() {
        profiles = new HashSet<>();
    }

    public Profile getByUuid(UUID uuid) {
        for (Profile profile : getProfiles()) {
            if (profile.getUuid().equals(uuid)) {
                return profile;
            }
        }
        return null;
    }

    public Profile getByCurrentName(String name) {
        for (Profile profile : getProfiles()) {
            if (profile.getCurrentName().equalsIgnoreCase(name)) {
                return profile;
            }
        }
        return null;
    }

    public Profile getByCurrentIp(String ip) {
        for (Profile profile : getProfiles()) {
            if (profile.getCurrentIp().equalsIgnoreCase(ip)) {
                return profile;
            }
        }
        return null;
    }

    public Set<Profile> getProfiles() {
        return profiles;
    }

    public void saveAllProfiles() {
        int i = 0;
        for (Profile profile : getProfiles()) {
            i++;
            profile.save();
        }
        System.out.println("[Yasuo] Saved " + i + " profile(s).");
    }

    public void loadAllProfiles() {
        int i = 0;
        File[] profiles = new File(Yasuo.getYasuo().getDataFolder() + File.separator + "profiles").listFiles();
        for (File file : profiles) {
            if (file.getName().contains(".yml") && UUID.fromString(file.getName().replace(".yml", "")) != null) {
                i++;
                new Profile(UUID.fromString(file.getName().replace(".yml", "")));
            }
        }
        System.out.println("[Yasuo] Loaded " + i + " profile(s).");
    }

}
