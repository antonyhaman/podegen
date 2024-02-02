package io.github.antonyhaman.podegen.core.builder;

import com.squareup.javapoet.TypeSpec;
import io.github.antonyhaman.podegen.core.data.PageObjectTemplate;
import io.github.antonyhaman.podegen.core.flavors.Flavorable;
import io.github.antonyhaman.podegen.core.strategies.Strategy;

import java.lang.reflect.InvocationTargetException;

public class CodeGeneratorBuilder {

    private PageObjectTemplate pageObjectTemplate;
    private Class<? extends Flavorable> flavor;
    private Class<? extends Strategy> strategy;

    private CodeGeneratorBuilder() {
    }

    public static CodeGeneratorBuilder builder() {
        return new CodeGeneratorBuilder();
    }

    public CodeGeneratorBuilder addFlavor(Class<? extends Flavorable> flavor) {
        this.flavor = flavor;
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
            Flavorable flavorObj = flavor.getDeclaredConstructor().newInstance();
            return strategy
                    .getDeclaredConstructor(PageObjectTemplate.class, Flavorable.class)
                    .newInstance(pageObjectTemplate, flavorObj)
                    .generate();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
