package com.github.kotvertolet.podegen.parsers;

import com.github.kotvertolet.podegen.data.PageObjectTemplate;
import io.github.classgraph.Resource;

public interface Parser {

    PageObjectTemplate parse(Resource resource);
}
