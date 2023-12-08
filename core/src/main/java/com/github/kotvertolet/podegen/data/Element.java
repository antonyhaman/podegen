package com.github.kotvertolet.podegen.data;


import com.github.kotvertolet.podegen.data.enums.LocatorType;

public record Element(String elementName, LocatorType locatorType, String locator) {

}
