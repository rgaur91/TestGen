package org.testgen.db;

import org.dizitart.no2.Nitrite;

import java.io.File;

public class DB {
    private static volatile DB instance = null;

    private  Nitrite database;

    private DB() {
        if (instance != null) {
            throw new AssertionError(
                    "Another instance of "
                            + DB.class.getName()
                            + " class already exists, Can't create a new instance.");
        }

        String dbDir = System.getProperty("user.home")+"/Library/testgen/";
        File file = new File(dbDir);
        if (!file.exists()) {
            if (!file.mkdir()) {
                throw new RuntimeException("Cannot create DB Dir "+dbDir+". Please create manually.");
            }
        }
        System.out.println("Will create new DB");
        try {
            database = Nitrite.builder()
                    .compressed()
                    .filePath(dbDir + "test.db")
                    .openOrCreate("test", "test");
            System.out.println("DB created "+database);
        }catch (Exception e){
            e.printStackTrace();
        }
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