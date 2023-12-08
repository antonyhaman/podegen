package com.github.kotvertolet.parsers;

import com.github.kotvertolet.data.PageObjectRecord;
import io.github.classgraph.Resource;

public interface Parser {

    PageObjectRecord parse(Resource resourceToParse);
}
