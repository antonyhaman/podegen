package com.github.kotvertolet.annotations;

import com.github.kotvertolet.data.enums.Flavour;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface PageObject {

    Flavour flavour() default Flavour.Selenium;

    boolean pageFactory() default true;

}
