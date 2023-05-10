package org.testgen.db.model;

import org.apache.commons.lang3.StringUtils;

public enum FieldType {
    ARRAY, STRING, NUMBER, OBJECT, BOOLEAN, NULL;

    public static String allowedValues() {
      return  StringUtils.join(values(), ',');
    }
}
