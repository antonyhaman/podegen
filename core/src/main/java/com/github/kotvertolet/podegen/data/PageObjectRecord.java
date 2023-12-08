package com.github.kotvertolet.podegen.data;

import java.util.List;

public record PageObjectRecord(String className, String packages, List<Element> elements) { }
