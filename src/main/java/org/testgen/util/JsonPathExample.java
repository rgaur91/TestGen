package org.testgen.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JsonPathExample {
    public static void main(String[] args) throws IOException {



// using json path       https://www.baeldung.com/guide-to-jayway-jsonpath
        // Sample JSON input
        String json = "{\"person\": {\"name\": \"John Smith\", \"contacts\": [{\"email\": \"john.smith@example.com\", \"phoneNumbers\": [\"555-1234\", \"555-5678\"]}, {\"email\": \"john.smith@work.com\", \"phoneNumbers\": [\"555-2468\", \"555-3690\"]}]}}";

        // Create ObjectMapper instance to read JSON
        ObjectMapper mapper = new ObjectMapper();

        // Read JSON as a tree of JsonNode objects
        JsonNode rootNode = mapper.readTree(json);

        // Use JsonPath to retrieve a value from the JSON
        ArrayNode phoneNumbers = (ArrayNode) rootNode.at("/person/contacts/0/phoneNumbers");

        // Print the retrieved value
        System.out.println("Phone numbers: " + phoneNumbers);
    }
}
