package io.github.antonyhaman.podegen.core.strategies;

import com.squareup.javapoet.*;
import io.github.antonyhaman.podegen.core.data.Element;
import io.github.antonyhaman.podegen.core.data.PageObjectTemplate;
import io.github.antonyhaman.podegen.core.flavors.Flavorable;
import io.github.antonyhaman.podegen.core.flavors.SeleniumFlavor;
import io.github.antonyhaman.podegen.core.utils.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

public class PageFactoryStrategy<T extends Flavorable> extends Strategy<T> {

    public PageFactoryStrategy(PageObjectTemplate pageObjectTemplate, T flavor) {
        super(pageObjectTemplate, flavor);
    }

    @Override
    protected FieldSpec getElementFieldSpec(Element element) {
        String fieldName = element.elementName();
        String locator = element.locator();
        String locatorType = element.locatorType().getValue();

        TypeName fieldType = element.isFindAll() ?
                getFlavor().getWebElementsTypeName() : getFlavor().getWebElementTypeName();

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
                .returns(isFindMany ? getFlavor().getWebElementsTypeName() : getFlavor().getWebElementTypeName());

        builder.addCode(CodeBlock.of(String.format("return %s;", fieldName)));
        return builder.build();
    }

    protected MethodSpec getMethodForPageFactoryInitialization() {
        MethodSpec.Builder builder;

        if (getFlavor().getClass() == SeleniumFlavor.class) {
            builder = MethodSpec.constructorBuilder().addParameter(WebDriver.class, "driver")
                    .addModifiers(Modifier.PUBLIC)
                    .addCode(CodeBlock.of("$T.initElements(driver, this);", PageFactory.class));
        } else {
            TypeName typeName = ClassName.get("", getPageObjectTemplate().className());
            builder = MethodSpec.methodBuilder("getPage")
                    .returns(typeName)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addCode(CodeBlock.of("return page($T.class);", typeName));
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