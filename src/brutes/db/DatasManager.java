/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.db;

import java.sql.*;
import java.util.Random;
import java.util.UUID;
import org.sqlite.JDBC;

/**
 *
 * @author Thiktak
 */
public class DatasManager {

    static private Connection con;

    public static Connection getInstance(String type, String dbpath) throws Exception {
        Class classType;
        switch (type) {
            case "sqlite":
                classType = Class.forName("org.sqlite.JDBC");
                dbpath = "jdbc:sqlite:" + dbpath;
                break;
            default:
                throw new Exception(type + " SQL support not exists");
        }
        try {
            DatasManager.con = DriverManager.getConnection(dbpath);
            DatasManager.con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, pseudo TEXT, password TEXT, token TEXT)");
            //datasManager.con.createStatement().executeUpdate("INSERT INTO users (pseudo, password) VALUES ('Thiktak', 'root1')");
            //datasManager.con.createStatement().executeUpdate("INSERT INTO users (pseudo, password) VALUES ('Kirauks', 'root2')");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return DatasManager.con;
    }

    public static Connection getInstance() throws Exception {
        if (DatasManager.con == null) {
            throw new Exception("No instance of dataManager");
        }
        return DatasManager.con;
    }

    public static ResultSet exec(String query) throws Exception {
        return DatasManager.getInstance().createStatement().executeQuery(query);
    }

    public static PreparedStatement prepare(String query) throws Exception {
        return DatasManager.getInstance().prepareStatement(query);
    }

    public static Statement getStatement() throws Exception {
        return DatasManager.getInstance().createStatement();
    }
    
    
    public static String updateToken(int id) throws Exception {
        Random rn = new Random();
        String token = UUID.randomUUID().toString();

        PreparedStatement psql = DatasManager.prepare("UPDATE users SET token = ? WHERE id = ?");
        psql.setString(1, token);
        psql.setInt(2, id);
        psql.executeUpdate();
        return token;
    }
}
