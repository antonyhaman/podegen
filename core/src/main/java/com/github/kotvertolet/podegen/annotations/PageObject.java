package com.github.kotvertolet.podegen.annotations;

import com.github.kotvertolet.podegen.data.enums.Flavour;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface PageObject {

    Flavour flavour() default Flavour.Selenium;

    boolean pageFactory() default true;

}
