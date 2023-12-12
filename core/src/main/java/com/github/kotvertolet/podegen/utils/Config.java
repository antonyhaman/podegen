package com.github.kotvertolet.podegen.utils;

import com.github.kotvertolet.podegen.annotations.PageObject;
import com.github.kotvertolet.podegen.data.enums.Flavours;
import com.github.kotvertolet.podegen.data.enums.Strategies;
import com.github.kotvertolet.podegen.flavours.Flavourable;
import com.github.kotvertolet.podegen.strategies.Strategy;

public class Config {

    private static Config config;

    private final Flavours flavours;
    private final Strategies strategy;

    private Config(PageObject annotation) {
        flavours = annotation.flavour();
        strategy = annotation.strategy();
    }

    public static void initConfig(PageObject annotation) {
        if (config == null) {
            config = new Config(annotation);
        } else throw new RuntimeException("Config is already initialized");
    }

    public static Config getInstance() {
        if (config != null) {
            return config;
        } else throw new RuntimeException("Configuration wasn't initialized");
    }

    public Flavours getFlavour() {
        return flavours;
    }

    public Class<? extends Flavourable> getFlavourType() {
        return flavours.getFlavour();
    }

    public Strategies getStrategy() {
        return strategy;
    }

    public Class<? extends Strategy> getStrategyType() {
        return strategy.getStrategy();
    }
}
