package com.github.kotvertolet.data;

import com.github.kotvertolet.data.enums.LocatorType;

public record Element(String elementName, LocatorType locatorType, String locator) {

}
