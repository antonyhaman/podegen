package com.github.kotvertolet.podegen.builder;

import com.github.kotvertolet.podegen.data.PageObjectTemplate;
import com.github.kotvertolet.podegen.flavours.Flavourable;
import com.github.kotvertolet.podegen.strategies.Strategy;
import com.squareup.javapoet.TypeSpec;

import java.lang.reflect.InvocationTargetException;

public class CodeGeneratorBuilder {

    private PageObjectTemplate pageObjectTemplate;
    private Class<? extends Flavourable> flavour;
    private Class<? extends Strategy> strategy;

    private CodeGeneratorBuilder() {
    }

    public static CodeGeneratorBuilder builder() {
        return new CodeGeneratorBuilder();
    }

    public CodeGeneratorBuilder addFlavour(Class<? extends Flavourable> flavour) {
        this.flavour = flavour;
        return this;
    }

    public CodeGeneratorBuilder addStrategy(Class<? extends Strategy> strategy) {
        this.strategy = strategy;
        return this;
    }

    public CodeGeneratorBuilder addPageObjectTemplate(PageObjectTemplate pageObjectTemplate) {
        this.pageObjectTemplate = pageObjectTemplate;
        return this;
    }

    public TypeSpec generateCode() {
        try {
            Flavourable flavourObj = flavour.getDeclaredConstructor().newInstance();
            return strategy
                    .getDeclaredConstructor(PageObjectTemplate.class, Flavourable.class)
                    .newInstance(pageObjectTemplate, flavourObj)
                    .generate();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
