package com.github.kotvertolet.podegen.core.parsers;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.kotvertolet.podegen.core.data.Element;
import com.github.kotvertolet.podegen.core.data.PageObjectTemplate;
import com.github.kotvertolet.podegen.core.data.enums.LocatorType;
import com.github.kotvertolet.podegen.core.utils.PathUtils;
import io.github.classgraph.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonParser extends JsonDeserializer<List> implements Parser {
    @Override
    public PageObjectTemplate parse(Resource resource) {
        PathUtils pathUtils = new PathUtils(resource.getPath());
        List<Element> elementList;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new SimpleModule().addDeserializer(List.class, this));
            elementList = objectMapper.readValue(resource.getContentAsString(), List.class);
        } catch (IOException e) {
            throw new Error(e);
        }
        return new PageObjectTemplate(pathUtils.getClassName(), pathUtils.getPackage(), elementList);
    }

    @Override
    public List<Element> deserialize(com.fasterxml.jackson.core.JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        List<Element> pageObjElements = new ArrayList<>();
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        ArrayNode array = node.withArray("");
        Iterator<JsonNode> arrayIterator = array.elements();
        while (arrayIterator.hasNext()) {
            JsonNode arrayNode = arrayIterator.next();
            Iterator<JsonNode> innerObjectIter = arrayNode.elements();
            String fieldName = arrayNode.fieldNames().next();
            while (innerObjectIter.hasNext()) {
                JsonNode innerObject = innerObjectIter.next();
                String locatorType = innerObject.get("locatorType").asText();
                String locator = innerObject.get("locator").asText();
                pageObjElements.add(new Element(fieldName, LocatorType.get(locatorType), locator));
            }
        }
        return pageObjElements;
    }
}