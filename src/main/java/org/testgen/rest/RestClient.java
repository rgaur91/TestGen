package org.testgen.rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RestClient {

public static <R> R sendRequest(String endpoint, String method, String request, Map<String, String> headers, Class<R> responseClass) {

    try {

        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        if(headers != null && !headers.isEmpty()){
            headers.forEach(connection::setRequestProperty);
        }
        if (request != null && !request.isBlank()) {
            connection.setRequestProperty("Content-Type", "application/json");

            // Enable output to the connection for sending the request body
            connection.setDoOutput(true);

            // Get the output stream from the connection and write the JSON request body to it
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(request.getBytes());
            outputStream.flush();
            outputStream.close();
        }


        // Read the response from the connection

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getResponseCode()!=200?connection.getErrorStream():connection.getInputStream()));
        StringBuilder responseBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            responseBuilder.append(line);
        }
        reader.close();

        // Print the response
        String response = responseBuilder.toString();
        Gson gson= new Gson();
        System.out.println(response);
        if (connection.getResponseCode()==200) {
            return gson.fromJson(response, responseClass);
        } else if (responseClass.equals(JsonObject.class)){
            System.out.println("Json ResObj");
            JsonObject errorRes= new JsonObject();
            errorRes.addProperty("Status", connection.getResponseCode());
            errorRes.addProperty("Response", response);
            return (R) errorRes;
        } else {
            return null;
        }

    } catch (ProtocolException e) {
        e.printStackTrace();
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return null;
}


}
