package io.github.antonyhaman.podegen.core.data;

import java.util.List;

public record PageObjectTemplate(String className, String packages, List<Element> elements) {
}
