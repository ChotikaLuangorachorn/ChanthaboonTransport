package controllers.assistants;

import org.springframework.lang.NonNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class UpdateExecutionAssistant {
    private String url;

    public UpdateExecutionAssistant(String url) {
        this.url = url;
    }

    public int execute(@NonNull String sql){
        Connection connection = null;
        try{
            connection = prepareConnection();
            if (connection != null){
                Statement statement = connection.createStatement();
                int result = statement.executeUpdate(sql);
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private Connection prepareConnection(){
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:" + url;
            Connection conn = DriverManager.getConnection(dbURL);

            return conn;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("connection Fail cannot find database");
        }
        return null;
    }
}
