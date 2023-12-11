package com.github.kotvertolet.podegen.data;

import com.github.kotvertolet.podegen.utils.Config;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import javax.lang.model.element.Modifier;
import java.util.List;

public class SeleniumCodeGenerator extends AbstractCodeGenerator {

    private final TypeName webElementTypeName;
    private final TypeName webElementsTypeName;

    public SeleniumCodeGenerator(PageObjectRecord pageObjectRecord) {
        super(pageObjectRecord);
        webElementTypeName = ClassName.get(WebElement.class);
        webElementsTypeName = ParameterizedTypeName.get(ClassName.get(List.class), ClassName.get(WebElement.class));
    }

    protected FieldSpec getElementFieldSpec(Element element) {
        String fieldName = element.elementName();
        String locator = element.locator();
        String locatorType = element.locatorType().getValue();

        FieldSpec.Builder builder;
        if (isPageFactory) {
            TypeName fieldType = element.isFindMany() ? webElementsTypeName : webElementTypeName;
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
                .returns(isFindMany ? webElementsTypeName : webElementTypeName);

        if (isPageFactory) {
            builder.addCode(CodeBlock.of(String.format("return %s;", fieldName)));
        } else {
            String methodToCall = isFindMany ? "findElements" : "findElement";
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
