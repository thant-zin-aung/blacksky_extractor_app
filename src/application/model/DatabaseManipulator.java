package application.model;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseManipulator {
    private static final String SQL_USERNAME = "BlackskyExtractor";
    private static final String SQL_PASSWORD = "blacksky123!@#";
    private static final String SQL_SCHEMA_NAME = "extractor";

    private static Connection connection;

    public static Connection getConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+SQL_SCHEMA_NAME
                    ,SQL_USERNAME,SQL_PASSWORD);
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return connection;
    }
}
