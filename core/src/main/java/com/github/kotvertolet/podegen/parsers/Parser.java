package com.github.kotvertolet.podegen.parsers;

import com.github.kotvertolet.podegen.data.PageObjectRecord;
import io.github.classgraph.Resource;

public interface Parser {

    PageObjectRecord parse(Resource resourceToParse);
}
