package com.github.kotvertolet.podegen.core.utils;

import com.github.kotvertolet.podegen.core.annotations.PageObject;
import com.github.kotvertolet.podegen.core.data.enums.Flavours;
import com.github.kotvertolet.podegen.core.data.enums.Strategies;
import com.github.kotvertolet.podegen.core.exceptions.PodegenException;
import com.github.kotvertolet.podegen.core.flavours.Flavourable;
import com.github.kotvertolet.podegen.core.strategies.Strategy;
import com.sun.tools.javac.code.Symbol;

import javax.lang.model.element.Element;

public class Config {

    private static Config config;
    private final Flavours flavours;
    private final Strategies strategy;
    private final String filePrefix;
    private final String supportedFilesFormats;
    private final String supportedFilesPattern;
    private final String ownerPackage;

    private Config(PageObject annotation, Symbol.ClassSymbol classSymbol) {
        flavours = annotation.flavour();
        strategy = annotation.strategy();
        filePrefix = annotation.prefix();
        ownerPackage = annotation.packages().isEmpty() ? classSymbol.owner.flatName().toString() : annotation.packages();
        supportedFilesFormats = "(\\.yaml|\\.yml|\\.json)";
        supportedFilesPattern = String.format("^.*(%s.*%s)", filePrefix, supportedFilesFormats);
    }

    public static <T extends Element> void initConfig(T element) {
        if (config == null) {
            config = new Config(element.getAnnotation(PageObject.class), (Symbol.ClassSymbol) element);
        } else throw new PodegenException("Config is already initialized");
    }

    public static Config getInstance() {
        if (config != null) {
            return config;
        } else throw new PodegenException("Configuration wasn't initialized");
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

    public String getTemplateFilePrefix() {
        return filePrefix;
    }

    public String getSupportedFilesFormats() {
        return supportedFilesFormats;
    }

    public String getSupportedFilesPattern() {
        return supportedFilesPattern;
    }

    public String getOwnerPackage() {
        return ownerPackage;
    }
}
