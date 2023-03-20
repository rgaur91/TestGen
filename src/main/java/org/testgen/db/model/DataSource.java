package org.testgen.db.model;

import org.dizitart.no2.objects.Id;

import java.io.Serializable;
import java.util.Objects;

public class DataSource implements Serializable {

    @Id
    private String sourceName;
    private String endpoint;
    private String method;
    private String requestBody;
    private boolean authentication;

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataSource that = (DataSource) o;
        return Objects.equals(sourceName, that.sourceName) &&
                Objects.equals(endpoint, that.endpoint) &&
                Objects.equals(method, that.method) &&
                Objects.equals(requestBody, that.requestBody);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceName, endpoint, method, requestBody);
    }

    public void setAuthentication(boolean authentication) {
        this.authentication = authentication;
    }

    public boolean getAuthentication() {
        return authentication;
    }
}
