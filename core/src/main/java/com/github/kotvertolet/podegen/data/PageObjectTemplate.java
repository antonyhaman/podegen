package com.github.kotvertolet.podegen.data;

import java.util.List;

public record PageObjectTemplate(String className, String packages, List<Element> elements) {
}
