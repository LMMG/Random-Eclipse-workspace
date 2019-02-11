package net.psychz.gga;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Test {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream(new File("D:/test.conf")));
        System.out.println(prop.getProperty("teste"));
        prop.clear();
        System.out.println(prop.getProperty("teste"));
        prop.load(new FileInputStream(new File("D:/test.conf")));
        System.out.println(prop.getProperty("teste"));
    }

}
