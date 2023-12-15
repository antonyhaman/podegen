package com.github.kotvertolet.podegen.core.strategies;

import com.github.kotvertolet.podegen.core.data.Element;
import com.github.kotvertolet.podegen.core.data.PageObjectTemplate;
import com.github.kotvertolet.podegen.core.flavours.Flavourable;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;


public abstract class Strategy<T extends Flavourable> {
    private final PageObjectTemplate pageObjectTemplate;
    private final T flavour;

    public Strategy(PageObjectTemplate pageObjectTemplate, T flavour) {
        this.pageObjectTemplate = pageObjectTemplate;
        this.flavour = flavour;
    }

    public PageObjectTemplate getPageObjectTemplate() {
        return pageObjectTemplate;
    }

    public T getFlavour() {
        return flavour;
    }

    protected abstract FieldSpec getElementFieldSpec(Element element);

    protected abstract MethodSpec getGetterMethodSpec(FieldSpec field, boolean isFindMany);


    public abstract TypeSpec generate();
}
