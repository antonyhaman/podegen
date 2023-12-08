package com.github.kotvertolet.builder;

import com.github.kotvertolet.data.Element;
import com.github.kotvertolet.data.PageObjectRecord;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

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

    public abstract PageFactoryBuilder addField(Element element);

    public abstract PageFactoryBuilder addConstructor();

    public abstract PageFactoryBuilder addMethod(CodeBlock code);

    public abstract TypeSpec build();
}
