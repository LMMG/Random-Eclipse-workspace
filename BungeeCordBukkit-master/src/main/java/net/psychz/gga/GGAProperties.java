package net.psychz.gga;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

public class GGAProperties extends Properties {

    private final File file;

    public GGAProperties(File file) {
        this.file = file;
    }

    public void reload() throws FileNotFoundException, IOException {

        FileInputStream fileInputStream = new FileInputStream(file);
        load(fileInputStream);

        if (containsKey("custom_properties_file")) {

            File customPropertiesFile = new File(getProperty("custom_properties_file"));

            if (!customPropertiesFile.exists() || !customPropertiesFile.isFile()) {
                GGA.getLogger().log(Level.WARNING, "Custom properties file not found, using the default one.");
                return;
            }

            if (!(customPropertiesFile.exists() && customPropertiesFile.isFile())) {
                clear();
                load(new FileInputStream(customPropertiesFile));
            }

        }

    }

}
