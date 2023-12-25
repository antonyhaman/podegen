package io.github.antonyhaman.podegen.core.parsers;

import io.github.antonyhaman.podegen.core.data.PageObjectTemplate;
import io.github.classgraph.Resource;

public interface Parser {

    PageObjectTemplate parse(Resource resource);
}
