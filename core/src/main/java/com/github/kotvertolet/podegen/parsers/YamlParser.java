package com.github.kotvertolet.podegen.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.kotvertolet.podegen.data.Element;
import com.github.kotvertolet.podegen.data.PageObjectTemplate;
import com.github.kotvertolet.podegen.utils.Path;
import io.github.classgraph.Resource;

import java.io.IOException;
import java.util.List;

public class YamlParser extends JsonParser implements Parser {
    @Override
    public PageObjectTemplate parse(Resource resource) {
        Path path = new Path(resource.getPath());
        List<Element> elementList;
        try {
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            objectMapper.registerModule(new SimpleModule().addDeserializer(List.class, this));
            elementList = objectMapper.readValue(resource.getContentAsString(), List.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new PageObjectTemplate(path.getClassName(), path.getPackage(), elementList);
    }
}
