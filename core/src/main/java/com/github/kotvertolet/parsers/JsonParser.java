package com.github.kotvertolet.parsers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.kotvertolet.data.Element;
import com.github.kotvertolet.data.PageObjectRecord;
import com.github.kotvertolet.data.enums.LocatorType;
import com.github.kotvertolet.utils.Path;
import io.github.classgraph.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonParser extends JsonDeserializer<List> implements Parser {
    @Override
    public PageObjectRecord parse(Resource resource) {
        Path path = new Path(resource.getPath());
        List<Element> elementList;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new SimpleModule().addDeserializer(List.class, this));
            elementList = objectMapper.readValue(resource.getContentAsString(), List.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new PageObjectRecord(path.getClassName(), path.getPackage(), elementList);
    }

    @Override
    public List<Element> deserialize(com.fasterxml.jackson.core.JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
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