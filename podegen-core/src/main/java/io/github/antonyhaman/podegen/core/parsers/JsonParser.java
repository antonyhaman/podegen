package io.github.antonyhaman.podegen.core.parsers;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;
import io.github.antonyhaman.podegen.core.data.Element;
import io.github.antonyhaman.podegen.core.data.PageObjectTemplate;
import io.github.antonyhaman.podegen.core.data.enums.LocatorType;
import io.github.antonyhaman.podegen.core.exceptions.PodegenException;
import io.github.antonyhaman.podegen.core.utils.PathUtils;
import io.github.classgraph.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
            throw new PodegenException(e);
        }
        return new PageObjectTemplate(pathUtils.getClassName(), pathUtils.getPackage(), elementList);
    }

    @Override
    public List<Element> deserialize(com.fasterxml.jackson.core.JsonParser jsonParser,
                                     DeserializationContext deserializationContext) throws IOException {
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
                Map.Entry<String, TextNode> props = (Map.Entry) innerObject.properties().toArray()[0];
                String locatorType = props.getKey();
                String locator = props.getValue().asText();
                pageObjElements.add(new Element(fieldName, LocatorType.get(locatorType), locator));
            }
        }
        return pageObjElements;
    }
}