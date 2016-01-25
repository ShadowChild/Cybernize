package io.github.shadowchild.common.reg;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zach Piddock on 16/01/2016.
 */
public class NamedRegistry<T> {

    public Map<String, T> registry = new HashMap<>();

    public int register(String name, T object) {

        if(registry.containsKey(name)) {

            return KEY_REGISTERED;
        } else if(registry.containsValue(object)) {

            return VALUE_REGISTERED;
        } else {

            registry.put(name, object);
            return SUCCESS;
        }
    }

    public static final int SUCCESS = 0, KEY_REGISTERED = 1, VALUE_REGISTERED = 2;
}