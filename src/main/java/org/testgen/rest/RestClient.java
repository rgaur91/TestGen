package org.testgen.rest;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class RestClient {

public static <R> R sendRequest(String endpoint, String method, String request, Class<R> responseClass) {

    try {

        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
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
        return gson.fromJson(response, responseClass);
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
