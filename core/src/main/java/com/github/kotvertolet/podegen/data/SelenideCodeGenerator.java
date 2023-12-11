package com.github.kotvertolet.podegen.data;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import javax.lang.model.element.Modifier;

public class SelenideCodeGenerator extends AbstractCodeGenerator {

    private final TypeName selenideElementTypeName;
    private final TypeName selenideElementsCollection;
    public SelenideCodeGenerator(PageObjectRecord pageObjectRecord) {
        super(pageObjectRecord);
        selenideElementTypeName = ClassName.get(SelenideElement.class);
        selenideElementsCollection = ClassName.get(ElementsCollection.class);
    }

    protected FieldSpec getElementFieldSpec(Element element) {
        String fieldName = element.elementName();
        String locator = element.locator();
        String locatorType = element.locatorType().getValue();

        FieldSpec.Builder builder;
        if (isPageFactory) {
            TypeName fieldType = element.isFindMany() ? selenideElementsCollection : selenideElementTypeName;
            builder = FieldSpec.builder(fieldType, fieldName, Modifier.PROTECTED)
                    .addAnnotation(AnnotationSpec.builder(FindBy.class)
                            .addMember(locatorType, "$S", locator).build());
        } else {
            builder = FieldSpec.builder(byTypeName, fieldName, Modifier.PROTECTED)
                    .initializer(String.format("$T.%s($S)", locatorType), byTypeName, locator);
        }
        return builder.build();
    }

    protected FieldSpec getDriverFieldSpec() {
        return FieldSpec.builder(WebDriver.class, "driver", Modifier.PROTECTED).build();
    }

    protected MethodSpec getGetterMethodSpec(FieldSpec field, boolean isFindMany) {
        String fieldName = field.name;
        MethodSpec.Builder builder = MethodSpec.methodBuilder("get" + StringUtils.capitalize(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .returns(isFindMany ? selenideElementsCollection : selenideElementTypeName);

        if (isPageFactory) {
            builder.addCode(CodeBlock.of(String.format("return %s;", fieldName)));
        } else {
            String methodToCall = isFindMany ? "$$" : "$";
            builder.addCode(CodeBlock.of(String.format("return driver.%s(%s);", methodToCall, fieldName)));
        }
        return builder.build();
    }

    protected MethodSpec getMethodSpecForConstructor() {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder().addParameter(WebDriver.class, "driver")
                .addModifiers(Modifier.PUBLIC);
        if (isPageFactory) {
            builder.addCode(CodeBlock.of("$T.initElements(driver, this);", PageFactory.class));
        } else builder.addCode(String.format("this.%1$s = %1$s;", "driver"));
        return builder.build();
    }
}
