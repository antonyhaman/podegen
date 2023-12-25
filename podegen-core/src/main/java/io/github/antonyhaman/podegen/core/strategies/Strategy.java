package io.github.antonyhaman.podegen.core.strategies;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import io.github.antonyhaman.podegen.core.data.Element;
import io.github.antonyhaman.podegen.core.data.PageObjectTemplate;
import io.github.antonyhaman.podegen.core.flavours.Flavourable;


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
