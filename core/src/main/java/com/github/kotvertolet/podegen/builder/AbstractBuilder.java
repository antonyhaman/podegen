package com.github.kotvertolet.podegen.builder;

import com.github.kotvertolet.podegen.data.Element;
import com.github.kotvertolet.podegen.data.PageObjectRecord;
import com.squareup.javapoet.*;
import org.openqa.selenium.WebElement;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBuilder {

    protected PageObjectRecord pageObjectRecord;
    protected List<FieldSpec> fields;

    protected List<MethodSpec> methods;

    protected final static String FIND_ALL_FLAG = "*";
    protected final TypeName webElementsListType;

    protected AbstractBuilder(PageObjectRecord pageObjectRecord) {
        this.pageObjectRecord = pageObjectRecord;
        fields = new ArrayList<>();
        methods = new ArrayList<>();
        webElementsListType = ParameterizedTypeName.get(ClassName.get(List.class), ClassName.get(WebElement.class));
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
