package controllers.services;

import org.springframework.lang.NonNull;
import utils.ReservationDateFormatter;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SQLiteService {
    protected String url;
    protected SimpleDateFormat formatter;

    public SQLiteService(String url) {
        this.url = url;
        this.formatter = ReservationDateFormatter.getInstance().getDbFormatter();
    }

    public <T> T executeQuery(String sql, OnResultSetCallback<T> callback, T defaultResult){
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

    protected int executeUpdate(@NonNull String sql){
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

    protected interface OnResultSetCallback<T>{
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
