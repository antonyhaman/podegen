package io.github.antonyhaman.podegen.core.utils;

import io.github.antonyhaman.podegen.core.annotations.PageObject;
import io.github.antonyhaman.podegen.core.data.enums.Flavors;
import io.github.antonyhaman.podegen.core.data.enums.Strategies;
import io.github.antonyhaman.podegen.core.exceptions.PodegenException;
import io.github.antonyhaman.podegen.core.flavors.Flavorable;
import io.github.antonyhaman.podegen.core.strategies.Strategy;

import javax.lang.model.element.Element;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Config {

    private static Config config;
    private final Flavors flavors;
    private final Strategies strategy;
    private final String filePrefix;
    private final String supportedFilesFormats;
    private final String supportedFilesPattern;
    private final String ownerPackage;

    private Config(Element element) {
        PageObject annotation = element.getAnnotation(PageObject.class);
        flavors = annotation.flavor();
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

    private static String getOwnerPackage(Element element) {
        String[] packageArr = element.toString().split("\\.");
        String className = packageArr[packageArr.length - 1];
        return Stream.of(packageArr).takeWhile(p -> !p.equals(className)).collect(Collectors.joining("."));
    }

    public Flavors getFlavor() {
        return flavors;
    }

    public Class<? extends Flavorable> getFlavorType() {
        return flavors.getFlavor();
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

    public String getSupportedFileFormats() {
        return supportedFilesPattern;
    }

    public Pattern getSupportedFileFormatsPattern() {
        return Pattern.compile(supportedFilesPattern);
    }

    public String getOwnerPackage() {
        return ownerPackage;
    }

}
