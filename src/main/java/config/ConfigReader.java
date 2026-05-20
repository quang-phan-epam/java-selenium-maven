package config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigReader is used to read and load config.properties.
 */
public class ConfigReader {

    private static final Logger log  = LogManager.getLogger(ConfigReader.class);
    private static final Properties props = new Properties();

    static {
        String env = System.getProperty("env","qa").toLowerCase().trim();
        String configFile = "config-" + env + ".properties";

        log.info("Load config for environment: [{}] → file: [{}] ", env, configFile);
        try (InputStream in = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream(configFile))
        {
            if (in == null) throw new RuntimeException("config file ["+configFile+"] not found. +" +
                    "Valid value are qa, staging, prod");
            props.load(in);
            log.info("Config loaded successfully → env=[{}] file=[{}]", env, configFile);

        } catch (IOException e) {
            log.fatal("Failed to load config file [{}]",configFile, e);
            throw new RuntimeException("Failed to load: "+ configFile, e);
        }
    }

    /** Returns the value for a key — system property overrides the file (useful for CI). */
    public static String get(String key) {
        String sys = System.getProperty(key);
        if(sys != null && !sys.isBlank())
        {
            log.debug("Config {} gets from system property", key);
            return sys;
        }
        String val = props.getProperty(key);
        if (val == null) {
            log.error("Config key not found: [{}]", key);
            throw new RuntimeException("Missing config key: [" + key + "]");
        }
        log.debug("Config [{}] = [{}]", key, key.toLowerCase().contains("password") ? "****" : val.trim());
        return val.trim();
    }

    public static String  getEnv(){return get("env");}
    public static int     getInt(String key)     { return Integer.parseInt(get(key)); }
    public static boolean getBoolean(String key) { return Boolean.parseBoolean(get(key)); }
}
