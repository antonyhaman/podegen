package com.github.kotvertolet.podegen.utils;

import com.github.kotvertolet.podegen.annotations.PageObject;
import com.github.kotvertolet.podegen.data.enums.Flavour;

public class Config {

    private static Config config;

    private final Flavour flavour;
    private final boolean pageFactory;

    private Config(PageObject annotation) {
        flavour = annotation.flavour();
        pageFactory = annotation.pageFactory();
    }

    public static void initConfig(PageObject annotation) {
        if (config == null) {
            config = new Config(annotation);
        }
        else throw new RuntimeException("Config is already initialized");
    }

    public static Config getInstance() {
        if (config != null) {
            return config;
        }
        else throw new RuntimeException("Configuration wasn't initialized");
    }

    public Flavour getFlavour() {
        return flavour;
    }

    public boolean isPageFactory() {
        return pageFactory;
    }
}
