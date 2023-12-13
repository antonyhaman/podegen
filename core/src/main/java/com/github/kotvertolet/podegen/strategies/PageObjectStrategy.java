package com.github.kotvertolet.podegen.strategies;

import com.github.kotvertolet.podegen.data.Element;
import com.github.kotvertolet.podegen.data.PageObjectTemplate;
import com.github.kotvertolet.podegen.flavours.Flavourable;
import com.github.kotvertolet.podegen.flavours.SeleniumFlavour;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

public class PageObjectStrategy<T extends Flavourable> extends Strategy<T> {


    public PageObjectStrategy(PageObjectTemplate pageObjectTemplate, T flavour) {
        super(pageObjectTemplate, flavour);
    }

    protected FieldSpec getElementFieldSpec(Element element) {
        String fieldName = element.elementName();
        String locator = element.locator();
        String locatorType = element.locatorType().getValue();


        FieldSpec.Builder builder = FieldSpec.builder(getFlavour().getByTypeName(), fieldName, Modifier.PROTECTED)
                .initializer(String.format("$T.%s($S)", locatorType), getFlavour().getByTypeName(), locator);

        return builder.build();
    }

    protected FieldSpec getDriverFieldSpec() {
        return FieldSpec.builder(getFlavour().getDriverTypeName(), "driver", Modifier.PROTECTED).build();
    }

    protected MethodSpec getGetterMethodSpec(FieldSpec field, boolean isFindMany) {
        String fieldName = field.name;
        MethodSpec.Builder builder = MethodSpec.methodBuilder("get" + StringUtils.capitalize(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .returns(isFindMany ? getFlavour().getWebElementsTypeName() : getFlavour().getWebElementTypeName());

        String methodToCall = isFindMany ? getFlavour().getFindAllMethodName() : getFlavour().getFindFirstMethodName();
        builder.addCode(CodeBlock.of(getFlavour().getGetterReturnValue(), methodToCall, fieldName));
        return builder.build();
    }

    protected MethodSpec getMethodSpecForConstructor() {
        return MethodSpec.constructorBuilder()
                .addParameter(getFlavour().getDriverTypeName(), "driver")
                .addModifiers(Modifier.PUBLIC)
                .addCode(String.format("this.%1$s = %1$s;", "driver"))
                .build();
    }

    @Override
    public TypeSpec generate() {
        List<MethodSpec> methodSpecs = new ArrayList<>();
        List<FieldSpec> fieldSpecs = new ArrayList<>();
        for (Element element : getPageObjectTemplate().elements()) {
            FieldSpec elementFieldSpec = getElementFieldSpec(element);
            fieldSpecs.add(elementFieldSpec);
            MethodSpec getterMethodSpec = getGetterMethodSpec(elementFieldSpec, element.isFindMany());
            methodSpecs.add(getterMethodSpec);
        }

        TypeSpec.Builder build = TypeSpec.classBuilder(getPageObjectTemplate().className())
                .addModifiers(Modifier.PUBLIC)
                .addFields(fieldSpecs);
        if (getFlavour().getClass() == SeleniumFlavour.class) {
            build
                    .addField(getDriverFieldSpec())
                    .addMethod(getMethodSpecForConstructor());
        }
        return build
                .addMethods(methodSpecs)
                .build();
    }
}
