package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Library {

    private static Library library = null;
    private static Properties properties;

    private Library() {
        try {
            FileInputStream file = new FileInputStream("Configs/config.properties");
            properties = new Properties();
            properties.load(file);
            // System.out.println(properties.getProperty("browser"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Fail to load config.properties");
        }
    }

    // Singleton accessor
    public static Library getLibrary() {
        if (library == null)
            library = new Library();
        return library;
    }

    // Reads value from config file or sysproperties if passed via
    public String getProperty(String key) {
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isEmpty()) {
            return sysProp;
        }

        return properties.getProperty(key);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public Properties configDataReadProperties(String appName) {

        Properties prop = new Properties();
        File propFile = new File(System.getProperty("user.dir")
                + "/src/test/resources/testData/" + appName
                + "/data.properties".replace('/', File.separatorChar));

        try {
            FileInputStream fis = new FileInputStream(propFile);
            prop.load(fis);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return prop;
    }

    public void configDataWriteProperties(String key, String value, String appName) {

        Properties prop = new Properties();

        // File propFile = new File(System.getProperty("user.dir")+"\\src\\test\\resources\\config\\WriteData");

        // File propFile = new File(System.getProperty("user.dir")+"/src/test/resources/config/config.properties".replace('/', File.separatorChar));

        File propFile = new File(System.getProperty("user.dir")
                + "/src/test/resources/testData/" + appName
                + "/data.properties".replace('/', File.separatorChar));

        try {

            if (!propFile.exists()) {
                propFile.getParentFile().mkdirs();
                propFile.createNewFile();
            }

            if (propFile.exists()) {
                FileInputStream fis = new FileInputStream(propFile);
                prop.load(fis);
                fis.close();
            }

            prop.setProperty(key, value);

            FileOutputStream fos = new FileOutputStream(propFile);
            prop.store(fos, "Updated configuration");
            fos.close();

            System.out.println(" Property [" + key + "] set to [" + value
                    + "] in data.properties for " + appName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
