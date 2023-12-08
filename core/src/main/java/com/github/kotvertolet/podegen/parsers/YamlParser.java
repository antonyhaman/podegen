package com.github.kotvertolet.podegen.parsers;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.github.kotvertolet.podegen.data.PageObjectRecord;
import com.github.kotvertolet.podegen.data.Element;
import com.github.kotvertolet.podegen.data.enums.LocatorType;
import com.github.kotvertolet.podegen.utils.Path;
import io.github.classgraph.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class YamlParser implements Parser {

    @Override
    public PageObjectRecord parse(Resource resource) {
        PageObjectRecord pageObjectRecordFile = null;
        List<Element> pageObjectFields = new ArrayList<>();
        try (YamlReader reader = new YamlReader(resource.getContentAsString())) {
            LinkedHashMap yml = (LinkedHashMap) reader.read();
            Iterator keyIter = yml.keySet().iterator();
            while (keyIter.hasNext()) {
                var key = keyIter.next();
                var map = (LinkedHashMap) yml.get(key);

                pageObjectFields.add(new Element((String) key,
                        LocatorType.get((String) map.get("locatorType")),
                        (String) map.get("locator")));

                Path path = new Path(resource.getPath());
                pageObjectRecordFile = new PageObjectRecord(path.getClassName(), path.getPackage(), pageObjectFields);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return pageObjectRecordFile;
    }
}
