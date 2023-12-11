package com.github.kotvertolet.podegen.data;

import com.github.kotvertolet.podegen.utils.Config;
import com.squareup.javapoet.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCodeGenerator {

    protected final boolean isPageFactory;
    protected final PageObjectRecord pageObjectRecord;
    protected final String findAllFlag = "$$";
    protected final TypeName byTypeName;

    public AbstractCodeGenerator(PageObjectRecord pageObjectRecord) {
        isPageFactory = Config.getInstance().isPageFactory();
        this.pageObjectRecord = pageObjectRecord;
        byTypeName = ClassName.get(By.class);
    }

    protected abstract FieldSpec getElementFieldSpec(Element element);

    protected abstract FieldSpec getDriverFieldSpec();

    protected abstract MethodSpec getGetterMethodSpec(FieldSpec field, boolean isFindMany);

    protected abstract MethodSpec getMethodSpecForConstructor();

    public TypeSpec generateCode() {
        List<MethodSpec> methodSpecs = new ArrayList<>();
        List<FieldSpec> fieldSpecs = new ArrayList<>();
        for (Element element : pageObjectRecord.elements()) {
            FieldSpec elementFieldSpec = getElementFieldSpec(element);
            fieldSpecs.add(elementFieldSpec);
            MethodSpec getterMethodSpec = getGetterMethodSpec(elementFieldSpec, element.isFindMany());
            methodSpecs.add(getterMethodSpec);
        }
        return TypeSpec.classBuilder(pageObjectRecord.className())
                .addModifiers(Modifier.PUBLIC)
                .addFields(fieldSpecs)
                .addField(getDriverFieldSpec())
                .addMethod(getMethodSpecForConstructor())
                .addMethods(methodSpecs)
                .build();
    }
}
