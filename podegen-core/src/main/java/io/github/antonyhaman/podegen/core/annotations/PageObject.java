package io.github.antonyhaman.podegen.core.annotations;

import io.github.antonyhaman.podegen.core.data.enums.Flavours;
import io.github.antonyhaman.podegen.core.data.enums.Strategies;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface PageObject {

    Flavours flavour() default Flavours.Selenium;

    Strategies strategy() default Strategies.PageFactory;

    String prefix() default "";

    String packages() default "";

}
