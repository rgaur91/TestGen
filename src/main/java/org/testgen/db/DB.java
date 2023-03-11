package org.testgen.db;

import org.dizitart.no2.Nitrite;

public class DB {
    private static volatile DB instance = null;

    private final Nitrite database;

    private DB() {
        if (instance != null) {
            throw new AssertionError(
                    "Another instance of "
                            + DB.class.getName()
                            + " class already exists, Can't create a new instance.");
        }
        database = Nitrite.builder()
                .compressed()
                .filePath("/tmp/test.db")
                .openOrCreate("test", "test");
    }

    public static DB getInstance() {
        if (instance == null) {
            synchronized (DB.class) {
                if (instance == null) {
                    instance = new DB();
                }
            }
        }
        return instance;
    }

    public Nitrite getDatabase() {
        return database;
    }
}