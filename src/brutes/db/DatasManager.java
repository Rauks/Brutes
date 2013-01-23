/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.db;

import brutes.game.Bonus;
import brutes.game.Fight;
import brutes.game.User;
import brutes.game.Character;
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
            DatasManager.con.createStatement().executeUpdate("INSERT INTO users (pseudo, password) VALUES ('Thiktak', 'root1')");
            DatasManager.con.createStatement().executeUpdate("INSERT INTO users (pseudo, password) VALUES ('Kirauks', 'root2')");

            DatasManager.con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS brutes (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, name TEXT, level INTEGER, life INTEGER, strength INTEGER, speed INTEGER)");
            DatasManager.con.createStatement().executeUpdate("INSERT INTO brutes (user_id, name, level, life, strength, speed) VALUES (1, 'Yéti', 2, 62, 5, 5)");
            DatasManager.con.createStatement().executeUpdate("INSERT INTO brutes (user_id, name, level, life, strength, speed) VALUES (1, 'Rauks', 1, 50, 3, 8)");

            DatasManager.con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS bonus (id INTEGER PRIMARY KEY AUTOINCREMENT, brute_id INTEGER, name TEXT, life INTEGER, level INTEGER, strength INTEGER, speed INTEGER)");
            DatasManager.con.createStatement().executeUpdate("INSERT INTO bonus (brute_id, name, level, life, strength, speed) VALUES (1, 'Hache', 1, 5, 10, 15)");
            DatasManager.con.createStatement().executeUpdate("INSERT INTO bonus (brute_id, name, level, life, strength, speed) VALUES (2, 'Robe de lin', 1, 15, 1, 10)");

            DatasManager.con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS fights (id INTEGER PRIMARY KEY AUTOINCREMENT, brute_id1 INTEGER, brute_id2 INTEGER, winner_id INTEGER)");
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

    public static User findUserByToken(String token) throws Exception {
        PreparedStatement psql = DatasManager.prepare("SELECT * FROM users WHERE token = ?");
        psql.setString(1, token);
        ResultSet rs = psql.executeQuery();
        if (rs.next()) {
            return new User(rs);
        }
        return null;
    }
    
    public static User findUserById(int id) throws Exception {
        PreparedStatement psql = DatasManager.prepare("SELECT * FROM users WHERE id = ?");
        psql.setInt(1, id);
        ResultSet rs = psql.executeQuery();
        if (rs.next()) {
            return new User(rs);
        }
        return null;
    }
    
    public static Character findCharacterById(int id) throws Exception {
        PreparedStatement psql = DatasManager.prepare("SELECT * FROM brutes WHERE id = ?");
        psql.setInt(1, id);
        ResultSet rs = psql.executeQuery();
        if (rs.next()) {
            return new brutes.game.Character(rs);
        }
        return null;
    }
    
    public static Character findCharacterByUser(User user) throws Exception {
        PreparedStatement psql = DatasManager.prepare("SELECT * FROM brutes WHERE user_id = ?");
        psql.setInt(1, user.getId());
        ResultSet rs = psql.executeQuery();
        if (rs.next()) {
            return new brutes.game.Character(rs);
        }
        return null;
    }
    
    public static Fight findFightByUser(User user) throws Exception {
        PreparedStatement psql = DatasManager.prepare("SELECT * FROM fights WHERE (brute_id1 = ? OR brute_id2 = ?) AND winner_id IS NULL");
        psql.setInt(1, user.getId());
        psql.setInt(2, user.getId());
        ResultSet rs = psql.executeQuery();
        if (rs.next()) {
            return new Fight(rs);
        }
        return null;
    }
    
    public static Bonus findBonusById(int id) throws Exception {
        PreparedStatement psql = DatasManager.prepare("SELECT * FROM bonus WHERE id = ?");
        psql.setInt(1, id);
        ResultSet rs = psql.executeQuery();
        if (rs.next()) {
            return new Bonus(rs);
        }
        return null;
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
