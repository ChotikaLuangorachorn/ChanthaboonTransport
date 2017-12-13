package controllers.assistants;

import java.sql.*;
import java.text.ParseException;

public class QueryExecutionAssistant<T> {

    private String url;

    public QueryExecutionAssistant(String url) {
        this.url = url;
    }

    public T execute(String sql, OnResultSetCallback<T> callback, T defaultResult){
        System.out.println("callback = " + callback);
        Connection connection = null;
        try{
            connection = prepareConnection();
            if (connection != null){
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                return callback.perform(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return defaultResult;
    }

    public interface OnResultSetCallback<T>{
        T perform(ResultSet resultSet) throws ParseException, SQLException;
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
