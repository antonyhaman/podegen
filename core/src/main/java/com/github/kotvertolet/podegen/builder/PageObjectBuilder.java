package com.github.kotvertolet.podegen.builder;

import com.github.kotvertolet.podegen.data.Element;
import com.github.kotvertolet.podegen.data.PageObjectRecord;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

public class PageObjectBuilder extends AbstractBuilder {

    private static final String DRIVER = "driver";
    private final List<FieldSpec> findAllFields = new ArrayList<>();

    public PageObjectBuilder(PageObjectRecord pageObjectRecord) {
        super(pageObjectRecord);
    }

    public PageObjectBuilder addFields() {
        pageObjectRecord.elements().forEach(this::addField);
        return this;
    }

    @Override
    public PageObjectBuilder addField(Element element) {
        FieldSpec.Builder builder = FieldSpec.builder(By.class, element.elementName(), Modifier.PRIVATE);
        String locator = element.locator();
        if (locator.startsWith(FIND_ALL_FLAG)) {
            locator = locator.replace(FIND_ALL_FLAG, "");
            findAllFields.add(builder
                    .initializer(String.format("$T.%s($S)", element.locatorType().getValue()), By.class, locator)
                    .build());
        } else {
            fields.add(builder
                    .initializer(String.format("$T.%s($S)", element.locatorType().getValue()), By.class, element.locator())
                    .build());
        }
        return this;
    }

    @Override
    public PageObjectBuilder addConstructor() {
        fields.add(FieldSpec.builder(WebDriver.class, DRIVER, Modifier.PRIVATE)
                .build());
        methods.add(MethodSpec.constructorBuilder()
                .addParameter(WebDriver.class, DRIVER)
                .addModifiers(Modifier.PUBLIC)
                .addCode(String.format("this.%1$s = %1$s;", DRIVER))
                .build());
        return this;
    }

    @Override
    public PageObjectBuilder addMethod(MethodSpec methodSpec) {
        methods.add(methodSpec);
        return this;
    }

    //TODO: Hate it, needs to be rewritten
    public PageObjectBuilder addGetters() {
        String fieldName;
        for (FieldSpec field : fields) {
            fieldName = field.name;
            MethodSpec method = addGetter(field, ClassName.get(WebElement.class),
                    CodeBlock.of(String.format("return driver.findElement(%s);", fieldName)));
            addMethod(method);
        }

        for (FieldSpec field : findAllFields) {
            fieldName = field.name;
            MethodSpec method = addGetter(field, webElementsListType,
                    CodeBlock.of(String.format("return driver.findElements(%s);", fieldName)));
            addMethod(method);
        }
        return this;
    }

    private MethodSpec addGetter(FieldSpec field, TypeName returnType, CodeBlock code) {
        return MethodSpec.methodBuilder("get" + StringUtils.capitalize(field.name))
                .addModifiers(Modifier.PUBLIC)
                .returns(returnType)
                .addCode(code)
                .build();
    }

    @Override
    public TypeSpec build() {
        return TypeSpec.classBuilder(pageObjectRecord.className())
                .addModifiers(Modifier.PUBLIC)
                .addFields(fields)
                .addFields(findAllFields)
                .addMethods(methods)
                .build();
    }
}
