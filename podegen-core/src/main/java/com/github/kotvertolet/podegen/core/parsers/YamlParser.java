package com.github.kotvertolet.podegen.core.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.kotvertolet.podegen.core.data.Element;
import com.github.kotvertolet.podegen.core.data.PageObjectTemplate;
import com.github.kotvertolet.podegen.core.utils.PathUtils;
import io.github.classgraph.Resource;

import java.io.IOException;
import java.util.List;

public class YamlParser extends JsonParser implements Parser {
    @Override
    public PageObjectTemplate parse(Resource resource) {
        PathUtils pathUtils = new PathUtils(resource.getPath());
        List<Element> elementList;
        try {
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            objectMapper.registerModule(new SimpleModule().addDeserializer(List.class, this));
            elementList = objectMapper.readValue(resource.getContentAsString(), List.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new PageObjectTemplate(pathUtils.getClassName(), pathUtils.getPackage(), elementList);
    }
}
