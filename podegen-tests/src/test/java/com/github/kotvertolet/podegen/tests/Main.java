package com.github.kotvertolet.podegen.tests;


import com.github.kotvertolet.podegen.core.annotations.PageObject;
import com.github.kotvertolet.podegen.core.data.enums.Flavours;
import com.github.kotvertolet.podegen.core.data.enums.Strategies;

@PageObject(flavour = Flavours.Selenium, strategy = Strategies.PageObject, prefix = "POGE_")
public class Main {

    public static void main(String[] args) {

    }
}