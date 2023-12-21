package com.github.kotvertolet.podegen.core.utils;

import com.github.kotvertolet.podegen.core.annotations.PageObject;
import com.github.kotvertolet.podegen.core.data.enums.Flavours;
import com.github.kotvertolet.podegen.core.data.enums.Strategies;
import com.github.kotvertolet.podegen.core.exceptions.PodegenException;
import com.github.kotvertolet.podegen.core.flavours.Flavourable;
import com.github.kotvertolet.podegen.core.strategies.Strategy;

import javax.lang.model.element.Element;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Config {

    private static Config config;
    private final Flavours flavours;
    private final Strategies strategy;
    private final String filePrefix;
    private final String supportedFilesFormats;
    private final String supportedFilesPattern;
    private final String ownerPackage;

    private Config(Element element) {
        PageObject annotation = element.getAnnotation(PageObject.class);
        flavours = annotation.flavour();
        strategy = annotation.strategy();
        filePrefix = annotation.prefix();
        ownerPackage = annotation.packages().isEmpty() ? getOwnerPackage(element) : annotation.packages();
        supportedFilesFormats = "(\\.yaml|\\.yml|\\.json)";
        supportedFilesPattern = String.format("^.*(%s.*%s)", filePrefix, supportedFilesFormats);
    }

    public static void initConfig(Element element) {
        if (config == null) {
            config = new Config(element);
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

    private static String getOwnerPackage(Element element) {
        String[] packageArr = element.toString().split("\\.");
        String className = packageArr[packageArr.length - 1];
        return Stream.of(packageArr).takeWhile(p -> !p.equals(className)).collect(Collectors.joining("."));
    }

}
