package com.example.util;

import java.sql.*;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/authdb_servlet";
    private static final String USER = "root";
    private static final String PASS = "subhasmita";

    static{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static boolean createUser(String username, String passwordHash) throws SQLException{
        try(Connection connection = getConnection()){
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,username);
            stmt.setString(2, passwordHash);
            int rows = stmt.executeUpdate();
            return rows > 0;
        }
    }
    public static String getPasswordHash(String username) throws SQLException{
        try(Connection connection = getConnection()) {
            String sql = "SELECT password FROM users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,username);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? resultSet.getString("password") : null;
        }
    }

}
