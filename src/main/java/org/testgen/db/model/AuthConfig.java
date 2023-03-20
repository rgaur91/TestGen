package org.testgen.db.model;

import org.dizitart.no2.objects.Id;

public class AuthConfig {

    @Id
    private final int id=1;
    private String host;
    private String endPoint;
    private String requestBody;

    public int getId() {
        return id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }
}
