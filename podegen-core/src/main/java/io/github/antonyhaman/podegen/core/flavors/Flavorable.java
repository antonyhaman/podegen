package io.github.antonyhaman.podegen.core.flavors;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public interface Flavorable {

    default TypeName getDriverTypeName() {
        return ClassName.get(WebDriver.class);
    }

    default TypeName getByTypeName() {
        return ClassName.get(By.class);
    }

    TypeName getWebElementTypeName();

    TypeName getWebElementsTypeName();

    String getFindFirstMethodName();

    String getFindAllMethodName();

    String getGetterReturnValue();
}