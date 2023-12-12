package com.github.kotvertolet.podegen.data.enums;

import java.util.HashMap;
import java.util.Map;

public enum Extension {

    JSON("json"),
    YAML("yaml"),
    YML("yml");

    private static final Map<String, Extension> lookup = new HashMap<String, Extension>();

    static {
        for (Extension ext : Extension.values()) {
            lookup.put(ext.getValue(), ext);
        }
    }

    String value;

    Extension(String value) {
        this.value = value;
    }

    public static Extension get(String extension) {
        return lookup.get(extension);
    }

    public String getValue() {
        return value;
    }
}