package com.github.kotvertolet.podegen.flavours;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SeleniumFlavour implements Flavourable {

    @Override
    public TypeName getWebElementTypeName() {
        return ClassName.get(WebElement.class);
    }

    @Override
    public TypeName getWebElementsTypeName() {
        return ParameterizedTypeName.get(ClassName.get(List.class), ClassName.get(WebElement.class));
    }

    @Override
    public String getFindFirstMethodName() {
        return "findElement";
    }

    @Override
    public String getFindAllMethodName() {
        return "findElements";
    }

    @Override
    public String getGetterReturnValue() {
        return "return driver.%s(%s);";
    }
}
