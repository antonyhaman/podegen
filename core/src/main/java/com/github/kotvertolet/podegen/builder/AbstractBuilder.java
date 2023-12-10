package com.github.kotvertolet.podegen.builder;

import com.github.kotvertolet.podegen.data.Element;
import com.github.kotvertolet.podegen.data.PageObjectRecord;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBuilder {

    protected PageObjectRecord pageObjectRecord;
    protected List<FieldSpec> fields;
    protected List<MethodSpec> methods;

    protected AbstractBuilder(PageObjectRecord pageObjectRecord) {
        this.pageObjectRecord = pageObjectRecord;
        this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();
    }

    public abstract AbstractBuilder addField(Element element);

    public abstract AbstractBuilder addConstructor();

    public abstract AbstractBuilder addMethod(MethodSpec code);

    public TypeSpec build() {
        return TypeSpec.classBuilder(pageObjectRecord.className())
                .addModifiers(Modifier.PUBLIC)
                .addFields(fields)
                .addMethods(methods)
                .build();
    }
}
