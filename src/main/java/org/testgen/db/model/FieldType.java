package org.testgen.db.model;

import org.apache.commons.lang3.StringUtils;

public enum FieldType {
    ARRAY, STRING, INTEGER, DOUBLE;

    public static String allowedValues() {
      return  StringUtils.join(values(), ',');
    }
}
