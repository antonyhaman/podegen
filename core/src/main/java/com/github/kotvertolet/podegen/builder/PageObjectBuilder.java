package com.github.kotvertolet.podegen.builder;

import com.github.kotvertolet.podegen.data.Element;
import com.github.kotvertolet.podegen.data.PageObjectRecord;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.lang.model.element.Modifier;

public class PageObjectBuilder extends AbstractBuilder {

    private static final String DRIVER = "driver";

    public PageObjectBuilder(PageObjectRecord pageObjectRecord) {
        super(pageObjectRecord);
    }

    public PageObjectBuilder addFields() {
        pageObjectRecord.elements().forEach(this::addField);
        return this;
    }

    @Override
    public PageObjectBuilder addField(Element element) {
        fields.add(FieldSpec.builder(By.class, element.elementName(), Modifier.PRIVATE)
                .initializer(String.format("$T.%s($S)", element.locatorType().getValue()), By.class, element.locator())
                .build());
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

    public PageObjectBuilder addGetters() {
        for (FieldSpec field : fields) {
            final String fieldName = field.name;
            MethodSpec method = MethodSpec.methodBuilder("get" + StringUtils.capitalize(fieldName))
                    .addModifiers(Modifier.PUBLIC)
                    .returns(WebElement.class)
                    .addCode(CodeBlock.of(String.format("return driver.findElement(%s);", fieldName)))
                    .build();
            addMethod(method);
        }
        return this;
    }
}
