package me.fix3dll.hypixelah;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static java.lang.Integer.parseInt;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.UUID;
import javax.swing.JOptionPane;

public class ConfigManager {

    public static void create() {
        try (OutputStream output = Files.newOutputStream(Paths.get("./HypixelAH.properties"))) {
            Properties prop = new Properties();
            prop.setProperty("apiKey", "");
            prop.setProperty("backgroundPath", "");
            prop.setProperty("backgroundRGB", "");
            prop.store(output, "\"/api\" ile oyun içinden \"apiKey\" alin");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String read(String key) {
        Properties prop = null;
        try (InputStream input = new FileInputStream("./HypixelAH.properties")) {
            prop = new Properties();
            prop.load(input);
        } catch (FileNotFoundException ex) {
            create();
            JOptionPane.showMessageDialog(null,
                    "\"apiKey\" bulunamadı! HypixelAH.properties dosyasından ayarlayın.",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop.getProperty(key);
    }

    public static UUID readAPI() {
        UUID key = null;
        try {
            key = UUID.fromString(read("apiKey"));
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null,
                    "Geçersiz UUID: \"" + key + "\"",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        return key;
    }

    public static String readBgPath() {
        if (!new File(read("backgroundPath")).exists()) {
            JOptionPane.showMessageDialog(null,
                    "\"backgroundPath\" yanlış!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        return read("backgroundPath");
    }

    public static int[] readBgRGB() {
        String[] bgRGB = ConfigManager.read("backgroundRGB").split(",", 4);
        int x = parseInt(bgRGB[0].trim());
        int y = parseInt(bgRGB[1].trim());
        int z = parseInt(bgRGB[2].trim());
        if (0 <= x && 255 >= x && 0 <= y && 255 >= y && 0 <= z && 255 >= z) {
            return new int[]{x, y, z};
        } else {
            JOptionPane.showMessageDialog(null,
                    "\"backgroundRGB\" yanlış!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
            return null;
        }
    }
}
