package com.github.kotvertolet.podegen.flavours;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

public class SelenideFlavour implements Flavourable {

    @Override
    public TypeName getWebElementTypeName() {
        return ClassName.get(SelenideElement.class);
    }

    @Override
    public TypeName getWebElementsTypeName() {
        return ClassName.get(ElementsCollection.class);
    }

    @Override
    public String getFindFirstMethodName() {
        return "$";
    }

    @Override
    public String getFindAllMethodName() {
        return "$$";
    }

    @Override
    public String getGetterReturnValue() {
        return "return %s(%s);";
    }
}
