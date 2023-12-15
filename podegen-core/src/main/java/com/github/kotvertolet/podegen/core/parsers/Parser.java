package com.github.kotvertolet.podegen.core.parsers;

import com.github.kotvertolet.podegen.core.data.PageObjectTemplate;
import io.github.classgraph.Resource;

public interface Parser {

    PageObjectTemplate parse(Resource resource);
}
