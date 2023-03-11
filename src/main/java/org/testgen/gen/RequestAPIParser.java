package org.testgen.gen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.Operation;
import io.swagger.oas.models.PathItem;
import io.swagger.oas.models.Paths;
import io.swagger.oas.models.media.Content;
import io.swagger.oas.models.media.MediaType;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.parameters.RequestBody;
import io.swagger.parser.OpenAPIParser;
import io.swagger.parser.models.ParseOptions;
import io.swagger.parser.models.SwaggerParseResult;
import io.swagger.parser.v3.OpenAPIV3Parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
/*import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;*/

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestAPIParser {

    private void parse() throws IOException {

//        SwaggerParseResult swaggerParseResult = new OpenAPIParser().readContents("~/Downloads/a.yaml", null, null);
//        OpenAPI openAPI = swaggerParseResult.getOpenAPI();
//        System.out.println(openAPI.getOpenapi());

        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        parseOptions.setResolveFully(true);
        SwaggerParseResult swaggerParseResult = new OpenAPIV3Parser().readLocation("/Users/rahulgaur/Downloads/DataAcquisitionHistory.yaml", null, parseOptions);
        final OpenAPI openAPI = swaggerParseResult.getOpenAPI();
        Paths paths = openAPI.getPaths();
        for (String path : paths.keySet()) {
            PathItem pathItem = paths.get(path);
            for (PathItem.HttpMethod httpMethod : pathItem.readOperationsMap().keySet()) {
                Operation operation = pathItem.readOperationsMap().get(httpMethod);
                RequestBody requestBody = operation.getRequestBody();
                if (requestBody != null) {
                    String json = new ObjectMapper().writeValueAsString(requestBody.getContent());
                    // Save the request payload as a JSON file
//                FileWriter fileWriter = new FileWriter(path + "_" + httpMethod.name() + ".json");
//                fileWriter.write(json);
//                fileWriter.close();
//                System.out.println(pathItem.readOperationsMap().get(httpMethod).getRequestBody().getContent());
//                System.out.println(json);
                    Content requestBodyContent = requestBody.getContent();
                    if (requestBodyContent == null) {
                        System.out.println("Request body not found for operation " + operation.getOperationId());
                        return;
                    }
                    MediaType mediaType = requestBodyContent.get("application/json");
                    if (mediaType == null) {
                        System.out.println("Media type 'application/json' not found in request body for operation " + operation.getOperationId());
                        return;
                    }

                    Schema<?> schema = mediaType.getSchema();
                    if (schema == null) {
                        System.out.println("Schema not found in request body for operation " + operation.getOperationId());
                        return;
                    }
                    ObjectMapper objectMapper = new ObjectMapper();
                    String outputFilePath = "output.json";
//                JsonNode jsonNode = schema.getExample() != null ? objectMapper.readTree(schema.getExample().toString()) : objectMapper.readTree(schema.getDefault().toString());
                    Object o = generateSampleOutput(schema);
                    System.out.println(o);
//                objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputFilePath), jsonNode);
                } else {
                    System.out.println("Request body is null for: "+operation.getSummary());
                }
            }
        }
        System.out.println(openAPI.getOpenapi());
    }

    private static Object generateSampleOutput(Schema schema) {
        if (schema.getProperties() != null) {
            Map<String, Object> properties = new HashMap<>();
            for (Object o : schema.getProperties().entrySet()) {
                Map.Entry<String, Schema> entry = (Map.Entry<String, Schema>) o;
                properties.put(entry.getKey(), generateSampleOutput(entry.getValue()));
            }

            return properties;
        } else if (schema.getEnum() != null) {
            return schema.getEnum().get(0); // return first enum value
        } else if (schema.getType() != null) {
            switch (schema.getType()) {
                case "string":
                    return "example";
                case "integer":
                case "number":
                    return 0;
                case "boolean":
                    return true;
                case "array":
                    if (schema.getMinItems() != null) {
                        List<Object> items = new ArrayList<>();
                        items.add(generateSampleOutput(schema.uniqueItems(true)));
                        return items;
                    } else {
                        return new ArrayList<>();
                    }
                case "object":
                default:
                    return new HashMap<>();
            }
        } else {
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        new RequestAPIParser().parse();
    }
}
