package io.github.antonyhaman.podegen.core.flavors;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SeleniumFlavor implements Flavorable {

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
        return "return driver.$L($L);";
    }
}
