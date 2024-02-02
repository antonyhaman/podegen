package io.github.antonyhaman.podegen.core.strategies;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import io.github.antonyhaman.podegen.core.data.Element;
import io.github.antonyhaman.podegen.core.data.PageObjectTemplate;
import io.github.antonyhaman.podegen.core.flavors.Flavorable;
import io.github.antonyhaman.podegen.core.flavors.SeleniumFlavor;
import io.github.antonyhaman.podegen.core.utils.StringUtils;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

public class PageObjectStrategy<T extends Flavorable> extends Strategy<T> {

    public PageObjectStrategy(PageObjectTemplate pageObjectTemplate, T flavor) {
        super(pageObjectTemplate, flavor);
    }

    protected FieldSpec getElementFieldSpec(Element element) {
        String fieldName = element.elementName();
        String locator = element.locator();
        String locatorType = element.locatorType().getValue();
        // Added due to inconsistency of the css selector name in @FindBy and By.class
        if (locatorType.equals("css")) {
            locatorType = "cssSelector";
        }

        FieldSpec.Builder builder = FieldSpec.builder(getFlavor().getByTypeName(), fieldName, Modifier.PROTECTED)
                .initializer(String.format("$T.%s($S)", locatorType), getFlavor().getByTypeName(), locator);

        return builder.build();
    }

    protected FieldSpec getDriverFieldSpec() {
        return FieldSpec.builder(getFlavor().getDriverTypeName(), "driver", Modifier.PROTECTED).build();
    }

    protected MethodSpec getGetterMethodSpec(FieldSpec field, boolean isFindMany) {
        String fieldName = field.name;
        MethodSpec.Builder builder = MethodSpec.methodBuilder("get" + StringUtils.capitalize(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .returns(isFindMany ? getFlavor().getWebElementsTypeName() : getFlavor().getWebElementTypeName());

        String methodToCall = isFindMany ? getFlavor().getFindAllMethodName() : getFlavor().getFindFirstMethodName();
        builder.addCode(CodeBlock.of(getFlavor().getGetterReturnValue(), methodToCall, fieldName));
        return builder.build();
    }

    protected MethodSpec getMethodSpecForConstructor() {
        return MethodSpec.constructorBuilder()
                .addParameter(getFlavor().getDriverTypeName(), "driver")
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
            MethodSpec getterMethodSpec = getGetterMethodSpec(elementFieldSpec, element.isFindAll());
            methodSpecs.add(getterMethodSpec);
        }

        TypeSpec.Builder build = TypeSpec.classBuilder(getPageObjectTemplate().className())
                .addModifiers(Modifier.PUBLIC)
                .addFields(fieldSpecs);
        if (getFlavor().getClass() == SeleniumFlavor.class) {
            build
                    .addField(getDriverFieldSpec())
                    .addMethod(getMethodSpecForConstructor());
        }
        return build
                .addMethods(methodSpecs)
                .build();
    }
}
