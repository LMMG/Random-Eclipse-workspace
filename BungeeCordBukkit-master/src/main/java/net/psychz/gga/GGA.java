/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.psychz.gga;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luca
 */
public class GGA {

    private static GGAProperties properties;

    private static Logger logger;

    private static String version;

    public static boolean init(Logger localLogger, File propertiesFile, String pluginVersion) {

        version = pluginVersion;
        logger = localLogger;
        properties = new GGAProperties(propertiesFile);

        if (!propertiesFile.exists()) {

            try {
                propertiesFile.createNewFile();
            } catch (IOException ex) {
                getLogger().log(Level.SEVERE, "There was an error while trying to create the properties file.");
                ex.printStackTrace();
                return false;
            }

            getProperties().setProperty("gga_connection_enabled", "true");
            getProperties().setProperty("gga_external_connection_enabled", "false");

            try {
                getProperties().store(new FileWriter(propertiesFile), null);
            } catch (IOException ex) {
                getLogger().log(Level.SEVERE, "There was an error while trying to write the default properties file.");
                ex.printStackTrace();
                return false;
            }

        }

        return true;
    }

    public static GGAProperties getProperties() {
        return properties;
    }

    public static Logger getLogger() {
        return logger;
    }

}
