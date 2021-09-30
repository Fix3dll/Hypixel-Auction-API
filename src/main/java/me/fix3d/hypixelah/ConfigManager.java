package me.fix3d.hypixelah;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.UUID;
import javax.swing.JOptionPane;

public class ConfigManager {   
    
    public static void create() {
        try (OutputStream output = new FileOutputStream("./HypixelAH.properties")) {
            Properties prop = new Properties();
            prop.setProperty("apiKey", "");
            prop.setProperty("backgroundPath", "");
            prop.setProperty("backgroundRGB", "");
            prop.store(output, "\"/api\" ile oyun içinden \"apiKey\" alin");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static String read(String key) {
        Properties prop = null;
        try (InputStream input = new FileInputStream("./HypixelAH.properties")) {
            prop = new Properties();
            prop.load(input);
        } catch (FileNotFoundException e) {
            create();
            JOptionPane.showMessageDialog(null,
                    "\"apiKey\" bulunamadı! HypixelAH.properties dosyasından ayarlayın.",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        } return prop.getProperty(key);
    }
    
    public static UUID readAPI() {
        UUID key = null;
        try {
            key = UUID.fromString(read("apiKey"));
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null,
                    "Geçersiz UUID: \""+key+"\"",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } return key;
    }
}
