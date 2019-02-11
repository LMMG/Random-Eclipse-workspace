package me.kairos.ipunish.profiles;

import java.net.InetAddress;
import java.util.UUID;

public interface IProfileManager {

    /**
     * Returns the stored profile, or saves to storage if this is the
     * first time creating this users' profile.
     *
     * @param uuid    the uuid of user
     * @param address the IP address of user
     * @return the loaded or saved profile
     */
    Profile loadOrSaveProfile(UUID uuid, InetAddress address);

    /**
     * Gets the profile of a user.
     *
     * @param uuid the uuid to lookup for
     * @return the profile
     */
    Profile getProfile(UUID uuid);

    /**
     * Saves a profile to the database asynchronously.
     *
     * @param profile the profile to save
     */
    void save(Profile profile);
}
