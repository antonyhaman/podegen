package io.github.antonyhaman.podegen.core.annotations;

import io.github.antonyhaman.podegen.core.data.enums.Flavors;
import io.github.antonyhaman.podegen.core.data.enums.Strategies;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface PageObject {

    Flavors flavor() default Flavors.Selenium;

    Strategies strategy() default Strategies.PageFactory;

    String prefix() default "";

    String packages() default "";

}
