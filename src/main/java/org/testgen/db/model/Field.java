package org.testgen.db.model;

import org.dizitart.no2.objects.Id;

import java.util.Objects;

public class Field {
    @Id
    private String name;
    private String source;
    private FieldType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return Objects.equals(name, field.name) &&
                Objects.equals(source, field.source) &&
                type == field.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, source, type);
    }

    @Override
    public String toString() {
        return "Field{" +
                "name='" + name + '\'' +
                ", source='" + source + '\'' +
                ", type=" + type +
                ", hash=" + hashCode() +
                ", objHash=" + super.hashCode() +
                '}';
    }
}
