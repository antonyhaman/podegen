package com.github.kotvertolet.podegen.core.strategies;

import com.github.kotvertolet.podegen.core.data.Element;
import com.github.kotvertolet.podegen.core.data.PageObjectTemplate;
import com.github.kotvertolet.podegen.core.flavours.Flavourable;
import com.github.kotvertolet.podegen.core.flavours.SeleniumFlavour;
import com.github.kotvertolet.podegen.core.utils.StringUtils;
import com.squareup.javapoet.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

public class PageFactoryStrategy<T extends Flavourable> extends Strategy<T> {

    public PageFactoryStrategy(PageObjectTemplate pageObjectTemplate, T flavour) {
        super(pageObjectTemplate, flavour);
    }

    @Override
    protected FieldSpec getElementFieldSpec(Element element) {
        String fieldName = element.elementName();
        String locator = element.locator();
        String locatorType = element.locatorType().getValue();

        TypeName fieldType = element.isFindAll() ?
                getFlavour().getWebElementsTypeName() : getFlavour().getWebElementTypeName();

        return FieldSpec.builder(fieldType, fieldName, Modifier.PROTECTED)
                .addAnnotation(AnnotationSpec.builder(FindBy.class)
                        .addMember(locatorType, "$S", locator)
                        .build())
                .build();
    }

    @Override
    protected MethodSpec getGetterMethodSpec(FieldSpec field, boolean isFindMany) {
        String fieldName = field.name;
        MethodSpec.Builder builder = MethodSpec.methodBuilder("get" + StringUtils.capitalize(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .returns(isFindMany ? getFlavour().getWebElementsTypeName() : getFlavour().getWebElementTypeName());

        builder.addCode(CodeBlock.of(String.format("return %s;", fieldName)));
        return builder.build();
    }

    protected MethodSpec getMethodForPageFactoryInitialization() {
        MethodSpec.Builder builder;

        if (getFlavour().getClass() == SeleniumFlavour.class) {
            builder = MethodSpec.constructorBuilder().addParameter(WebDriver.class, "driver")
                    .addModifiers(Modifier.PUBLIC)
                    .addCode(CodeBlock.of("$T.initElements(driver, this);", PageFactory.class));
        } else {
            TypeName typeName = ClassName.get("", getPageObjectTemplate().className());
            builder = MethodSpec.methodBuilder("getPage")
                    .returns(typeName)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addCode(CodeBlock.of("return com.codeborne.selenide.Selenide.page($T.class);", typeName));
        }
        return builder.build();
    }

    @Override
    public TypeSpec generate() {
        List<MethodSpec> methodSpecs = new ArrayList<>();
        List<FieldSpec> fieldSpecs = new ArrayList<>();
        for (Element element : getPageObjectTemplate().elements()) {
            FieldSpec elementFieldSpec = getElementFieldSpec(element);
            fieldSpecs.add(elementFieldSpec);
            MethodSpec getterMethodSpec = getGetterMethodSpec(elementFieldSpec, element.isFindAll());
            methodSpecs.add(getterMethodSpec);
        }
        return TypeSpec.classBuilder(getPageObjectTemplate().className())
                .addModifiers(Modifier.PUBLIC)
                .addFields(fieldSpecs)
                .addMethod(getMethodForPageFactoryInitialization())
                .addMethods(methodSpecs)
                .build();
    }
}