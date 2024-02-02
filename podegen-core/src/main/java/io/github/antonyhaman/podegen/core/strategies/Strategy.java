package io.github.antonyhaman.podegen.core.strategies;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import io.github.antonyhaman.podegen.core.data.Element;
import io.github.antonyhaman.podegen.core.data.PageObjectTemplate;
import io.github.antonyhaman.podegen.core.flavors.Flavorable;


public abstract class Strategy<T extends Flavorable> {
    private final PageObjectTemplate pageObjectTemplate;
    private final T flavor;

    public Strategy(PageObjectTemplate pageObjectTemplate, T flavor) {
        this.pageObjectTemplate = pageObjectTemplate;
        this.flavor = flavor;
    }

    public PageObjectTemplate getPageObjectTemplate() {
        return pageObjectTemplate;
    }

    public T getFlavor() {
        return flavor;
    }

    protected abstract FieldSpec getElementFieldSpec(Element element);

    protected abstract MethodSpec getGetterMethodSpec(FieldSpec field, boolean isFindMany);

    public abstract TypeSpec generate();
}
