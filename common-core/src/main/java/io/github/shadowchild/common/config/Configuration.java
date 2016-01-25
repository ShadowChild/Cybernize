package io.github.shadowchild.common.config;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Zach Piddock on 03/11/2015.
 */
@Deprecated
public class Configuration {

    private ConfigType type;
    private String fileName;

    public enum ConfigType {

        JSON(JsonConfig.class),
        INI(IniConfig.class);

        private Class<? extends Config> clazz;

        ConfigType(Class<? extends Config> clazz) {

            this.clazz = clazz;
        }

        public Class<? extends Config> getClazz() {

            return clazz;
        }
    }

    public Configuration(ConfigType type, String fileName) {

        this.type = type;
        this.fileName = fileName;
    }

    public Config load() {

        Config conf = null;

        Class<? extends Config> clazz = type.getClazz();

        try {

            Constructor<? extends Config> constructor = clazz.getConstructor(String.class);

            conf = constructor.newInstance(fileName);
        } catch(NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {

            e.printStackTrace();
        }

        return conf;
    }
}