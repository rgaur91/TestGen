package org.testgen.db.model;

import org.dizitart.no2.objects.Id;

import java.util.Objects;

public class SourcedField {

    @Id
    private String id;
    private String name;
    private String source;
    private String path;
    private FieldType type;

    public SourcedField() {
    }

    public SourcedField(String name, String source, String path, FieldType type) {
        this.name = name;
        this.source = source;
        this.path = path;
        this.type = type;
        if (source != null && name != null) {
            id=source+name;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (source != null && name != null) {
            id=source+name;
        }
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        if (source != null && name != null) {
            id=source+name;
        }
        this.source = source;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SourcedField that = (SourcedField) o;
        return name.equals(that.name) &&
                source.equals(that.source) &&
                path.equals(that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, source, path);
    }

    @Override
    public String toString() {
        return "{"  + name + ',' + source +
                '}';
    }
}
