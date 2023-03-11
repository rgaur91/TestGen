package org.testgen.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class RestResponseTreeToNoSQL {
    public static void loadSourceData() throws IOException {
        // Load response from REST endpoint
        String restEndpoint = "https://jsonplaceholder.typicode.com/users";
        String response = loadResponseFromEndpoint(restEndpoint);

        // Map response to tree data structure
        Map<String, Map<String, List<?>>> dbMap=new HashMap<>();

        HashMap<String, List<?>> user1Data = new HashMap<>();
        dbMap.put("User1", user1Data);
        Gson jsonArray = new Gson();
        JsonObject res = jsonArray.fromJson(response, JsonObject.class);
        for (String fields : res.keySet()) {
//            user1Data.put(fields, )
        }


        // Store tree data structure in NoSQL database
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("mydb");
        MongoCollection<Document> collection = database.getCollection("restResponseTree");

        List<Document> documents = new ArrayList<>();
//        createDocumentsFromTree(root, documents);

        collection.insertMany(documents);
        mongoClient.close();
    }

    private static String loadResponseFromEndpoint(String endpoint) throws IOException {
        URL url = new URL(endpoint);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

   /* private static void createDocumentsFromTree(TreeNode node, List<Document> documents) {
        Document document = new Document();
        document.put("name", node.getName());

        List<Document> childDocuments = new ArrayList<>();
        for (TreeNode child : node.getChildren()) {
            createDocumentsFromTree(child, childDocuments);
        }
        document.put("children", childDocuments);

        documents.add(document);
    }*/

    /*private static class TreeNode {
        private final String name;
        private final List<TreeNode> children;

        public TreeNode(String name) {
            this.name = name;
            this.children = new ArrayList<>();
        }

        public String getName() {
            return name;
        }

        public List<TreeNode> getChildren() {
            return children;
        }

        public void addChild(TreeNode child) {
            children.add(child);
        }
    }*/
}
