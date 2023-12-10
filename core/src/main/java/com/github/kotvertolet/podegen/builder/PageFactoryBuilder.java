package com.github.kotvertolet.podegen.builder;

import com.github.kotvertolet.podegen.data.Element;
import com.github.kotvertolet.podegen.data.PageObjectRecord;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import javax.lang.model.element.Modifier;

public class PageFactoryBuilder extends AbstractBuilder {

    public PageFactoryBuilder(PageObjectRecord pageObjectRecord) {
        super(pageObjectRecord);
    }

    @Override
    public PageFactoryBuilder addField(Element element) {
        FieldSpec.Builder builder;
        String locator = element.locator();
        // Check if we have to use List<WebElement> instead
        if (locator.startsWith(FIND_ALL_FLAG)) {
            locator = locator.replace(FIND_ALL_FLAG, "");
            builder = FieldSpec.builder(webElementsListType, element.elementName(), Modifier.PRIVATE);
        } else {
            builder = FieldSpec.builder(WebElement.class, element.elementName(), Modifier.PRIVATE);
        }
        fields.add(builder.addAnnotation(AnnotationSpec.builder(FindBy.class)
                        .addMember(element.locatorType().getValue(), "$S", locator)
                        .build())
                .build());
        return this;
    }

    public PageFactoryBuilder addFields() {
        pageObjectRecord.elements().forEach(this::addField);
        return this;
    }

    @Override
    public PageFactoryBuilder addConstructor() {
        methods.add(MethodSpec.constructorBuilder().addParameter(WebDriver.class, "driver")
                .addCode(CodeBlock.of("$T.initElements(driver, this);", PageFactory.class))
                .build());
        return this;
    }

    @Override
    public PageFactoryBuilder addMethod(MethodSpec methodSpec) {
        methods.add(methodSpec);
        return this;
    }

    /**
     * Adds getter methods for each field added so far thus it should be called after all fields were added
     *
     * @return builder
     */
    public PageFactoryBuilder addGetters() {
        for (FieldSpec field : fields) {
            MethodSpec method = addGetter(field)
                    .returns(field.type)
                    .build();
            addMethod(method);
        }
        return this;
    }

    //TODO: Implement adding annotation provided through configuration
    @Override
    public TypeSpec build() {
        return TypeSpec.classBuilder(pageObjectRecord.className())
                .addModifiers(Modifier.PUBLIC)
                //.addAnnotation(Getter.class)
                .addFields(fields)
                .addMethods(methods)
                .build();
    }

    private MethodSpec.Builder addGetter(FieldSpec field) {
        String fieldName = field.name;
        return MethodSpec.methodBuilder("get" + StringUtils.capitalize(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .addCode(CodeBlock.of(String.format("return %s;", fieldName)));
    }
}
