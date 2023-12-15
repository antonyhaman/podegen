package com.github.kotvertolet.podegen.core.data;


import com.github.kotvertolet.podegen.core.data.enums.LocatorType;

import java.util.Objects;

public final class Element {
    private final String elementName;
    private final LocatorType locatorType;
    private final String locator;
    private final boolean findMany;

    public Element(String elementName, LocatorType locatorType, String locator) {
        this.findMany = elementName.startsWith("$$");
        this.elementName = elementName.replaceAll("^\\${1,2}", "");
        this.locatorType = locatorType;
        this.locator = locator;
    }

    public String elementName() {
        return elementName;
    }

    public LocatorType locatorType() {
        return locatorType;
    }

    public String locator() {
        return locator;
    }

    public boolean isFindMany() {
        return findMany;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Element element = (Element) o;
        return findMany == element.findMany && Objects.equals(elementName, element.elementName) && locatorType == element.locatorType && Objects.equals(locator, element.locator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elementName, locatorType, locator, findMany);
    }

    @Override
    public String toString() {
        return "Element{" +
                "elementName='" + elementName + '\'' +
                ", locatorType=" + locatorType +
                ", locator='" + locator + '\'' +
                ", findMany=" + findMany +
                '}';
    }
}
