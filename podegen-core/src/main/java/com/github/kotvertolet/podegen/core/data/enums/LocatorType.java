package com.github.kotvertolet.podegen.core.data.enums;

import java.util.HashMap;
import java.util.Map;

public enum LocatorType {

    ID("id"),
    NAME("name"),
    CLASS_NAME("className"),
    CSS("css"),
    TAG_NAME("tagName"),
    LINK_TEXT("linkText"),
    PARTIAL_LINK_TEXT("partialLinkText"),
    XPATH("xpath");

    private static final Map<String, LocatorType> lookup = new HashMap<String, LocatorType>();

    static {
        for (LocatorType ext : LocatorType.values()) {
            lookup.put(ext.getValue(), ext);
        }
    }

    String value;

    LocatorType(String value) {
        this.value = value;
    }

    public static LocatorType get(String locatorType) {
        return lookup.get(locatorType);
    }

    public String getValue() {
        return value;
    }
}
