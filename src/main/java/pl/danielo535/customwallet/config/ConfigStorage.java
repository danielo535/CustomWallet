package pl.danielo535.customwallet.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ConfigStorage {
    private static File cfgFile;
    private static final String fileName = "config.yml";
    private static FileConfiguration cfg;
    public static File getCfg() { return cfgFile; }

    public static FileConfiguration getFile() {
        return cfg;
    }

    //$ = -
    // _ = . (nowy wiersz)

    //CONFIGS SETTINGS
    public static Boolean SETTINGS_UPDATE$INFO;

    //CONFIGS DATABASE
    public static String DATABASE_HOST;
    public static Integer DATABASE_PORT;
    public static String DATABASE_DATABASE;
    public static String DATABASE_USERNAME;
    public static String DATABASE_PASSWORD;

    // CONFIGS MESSAGES
    public static String MESSAGES_ADD;
    public static String MESSAGES$ERROR_NUMBER;
    public static String MESSAGES$ERROR_MONEY;
    public static String MESSAGES_SET;
    public static String MESSAGES_REMOVE;
    public static String MESSAGES$ERROR_REMOVE$MONEY;
    public static String MESSAGES_CHECK;
    public static String MESSAGES_PAY;
    public static String MESSAGES_PAY$USER;
    public static String MESSAGES$ERROR_PAY$VALUE;
    public static String MESSAGES$ERROR_PAY$MONEY;
    public static String MESSAGES$ERROR_USER;
    public static String MESSAGES$ERROR_USER$DATABASE;
    public static String MESSAGES$ERROR_USAGE$NUMBER;
    public static String MESSAGES_RELOAD;
    public static String MESSAGES_USAGE;
    public static String MESSAGES$ERROR_NO$PERMISSION;

    public static void createDefaultFiles(Plugin plugin) {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists() && !dataFolder.mkdir()) {
            throw new RuntimeException("Wystąpił błąd podczas tworzenia folderu pluginu!");
        } else {
            cfgFile = new File(dataFolder, fileName);
            if (!cfgFile.exists()) {
                try {
                    Files.copy(plugin.getClass().getClassLoader().getResourceAsStream(fileName), Paths.get(cfgFile.toURI()), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException("Wystąpił błąd podczas tworzenia configu!", e);
                }
            }

            cfg = YamlConfiguration.loadConfiguration(cfgFile);
        }
    }

    public static void load() {
        try {
            Field[] fields = ConfigStorage.class.getFields();
            for (Field field : fields) {
                if (isConfigField(field)) {
                    String path = getPath(field);
                    if (cfg.isSet(path)) {
                        field.set(null, cfg.get(path));
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Wystąpił problem podczas ładowania configu!", e);
        }
    }

    public static void save() {
        try {
            Field[] fields = ConfigStorage.class.getFields();
            for (Field field : fields) {
                if (isConfigField(field)) {
                    String path = getPath(field);
                    cfg.set(path, field.get(null));
                }
            }
            cfg.save(cfgFile);
        } catch (Exception e) {
            throw new RuntimeException("Wystąpił problem podczas zapisywania configu!", e);
        }
    }

    public static void reload() {
        cfg = YamlConfiguration.loadConfiguration(cfgFile);
        load();
        save();
    }

    private static String getPath(Field field) {
        return field.getName().toLowerCase().replace("$", "-").replace("_", ".");
    }

    private static boolean isConfigField(Field field) {
        return Modifier.isPublic(field.getModifiers())
                && Modifier.isStatic(field.getModifiers())
                && !Modifier.isFinal(field.getModifiers())
                && !Modifier.isTransient(field.getModifiers());
    }
}
