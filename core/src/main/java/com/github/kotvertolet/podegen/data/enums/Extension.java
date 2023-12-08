package com.github.kotvertolet.podegen.data.enums;

import java.util.HashMap;
import java.util.Map;

public enum Extension {

    JSON("json"),
    YAML("yaml"),
    YML("yml");

    String value;

    private static final Map<String, Extension> lookup = new HashMap<String, Extension>();

    static {
        for (Extension ext : Extension.values()) {
            lookup.put(ext.getValue(), ext);
        }
    }

    Extension(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Extension get(String extension) {
        return lookup.get(extension);
    }
}