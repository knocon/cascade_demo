package com.demo.resy;

import java.sql.*;

public class Database {

    public String url;
    public String user;
    public String password;
    public Connection connection;

    public Database(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }


    public void initializeConnection() {
        System.out.println("DB Connection starting...");
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            //TODO: java Log
            System.out.println("DB Connection successful!");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public static void insert_User(User user) throws SQLException {
        System.out.println("Insert User starting...");
        Connection conn = createConnection();
        PreparedStatement ps = conn.prepareStatement("insert into user (Username, Email, Password) values(?,?,?)");
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getEmail());
        ps.setString(3, user.getPassword());
        ps.executeUpdate();
        ps.close();
        conn.close();
        System.out.println("Insert User successful!");

    }

    /* Hilfsmethode, um eine Connection zu erzeugen und zurückzugeben */
    public static Connection createConnection() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cascade_db?", "root", "");
        System.out.println("Connection created.");
        return con;
    }

    public static boolean checkDuplicateonEmail(User user) throws SQLException {
        Connection conn = createConnection();
        PreparedStatement statement = conn.prepareStatement("SELECT * from user WHERE Email = '" + user.getEmail() + "'");
        ResultSet rS = statement.executeQuery("SELECT * from user WHERE Email = '" + user.getEmail() + "'");
        if (!rS.next()) {
            System.out.print("Check: Duplicates not found!");
            rS.close();
            statement.close();
            conn.close();
            return false;
        } else {
            System.out.println("Check: Duplicates found!");
            statement.close();
            rS.close();
            conn.close();
            return true;
        }
    }


}
