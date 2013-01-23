/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.db;

import brutes.db.entity.BonusEntity;
import brutes.db.entity.CharacterEntity;
import brutes.db.entity.FightEntity;
import brutes.db.entity.UserEntity;
import brutes.game.Bonus;
import brutes.game.Fight;
import brutes.game.User;
import brutes.game.Character;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sqlite.JDBC;

/**
 *
 * @author Thiktak
 */
public class DatasManager {

    static private Connection con;

    public static Connection getInstance(String type, String dbpath) throws IOException {
        Class classType;
        switch (type) {
            case "sqlite":
                try {
                    classType = Class.forName("org.sqlite.JDBC");
                    dbpath = "jdbc:sqlite:" + dbpath;
                } catch (ClassNotFoundException e) {
                    throw new IOException(e);
                }
                break;
            default:
                throw new IOException(type + " SQL support not exists");
        }
        try {
            DatasManager.con = DriverManager.getConnection(dbpath);

            DatasManager.con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, pseudo TEXT, password TEXT, token TEXT)");
            DatasManager.con.createStatement().executeUpdate("INSERT INTO users (pseudo, password) VALUES ('Thiktak', 'root1')");
            DatasManager.con.createStatement().executeUpdate("INSERT INTO users (pseudo, password) VALUES ('Kirauks', 'root2')");

            DatasManager.con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS brutes (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, name TEXT, level INTEGER, life INTEGER, strength INTEGER, speed INTEGER)");
            DatasManager.con.createStatement().executeUpdate("INSERT INTO brutes (user_id, name, level, life, strength, speed) VALUES (1, 'Yéti', 2, 62, 5, 5)");
            DatasManager.con.createStatement().executeUpdate("INSERT INTO brutes (user_id, name, level, life, strength, speed) VALUES (2, 'Rauks', 1, 50, 3, 8)");
            DatasManager.con.createStatement().executeUpdate("INSERT INTO brutes (user_id, name, level, life, strength, speed) VALUES (1, 'Bot_1', 1, 10, 2, 4)");
            DatasManager.con.createStatement().executeUpdate("INSERT INTO brutes (user_id, name, level, life, strength, speed) VALUES (2, 'Bot_2', 10, 250, 23, 18)");

            DatasManager.con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS bonus (id INTEGER PRIMARY KEY AUTOINCREMENT, brute_id INTEGER, name TEXT, life INTEGER, level INTEGER, strength INTEGER, speed INTEGER)");
            DatasManager.con.createStatement().executeUpdate("INSERT INTO bonus (brute_id, name, level, life, strength, speed) VALUES (1, 'Hache', 1, 5, 10, 15)");
            DatasManager.con.createStatement().executeUpdate("INSERT INTO bonus (brute_id, name, level, life, strength, speed) VALUES (2, 'Robe de lin', 1, 15, 1, 10)");
            DatasManager.con.createStatement().executeUpdate("INSERT INTO bonus (brute_id, name, level, life, strength, speed) VALUES (3, 'Anneau de la forge', 10, 150, 50, 0)");

            DatasManager.con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS fights (id INTEGER PRIMARY KEY AUTOINCREMENT, brute_id1 INTEGER, brute_id2 INTEGER, winner_id INTEGER)");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return DatasManager.con;
    }

    public static Connection getInstance() throws IOException {
        if (DatasManager.con == null) {
            throw new IOException("No instance of dataManager");
        }
        return DatasManager.con;
    }

    public static ResultSet exec(String query) throws IOException, SQLException {
        return DatasManager.getInstance().createStatement().executeQuery(query);
    }

    public static PreparedStatement prepare(String query) throws IOException, SQLException {
        return DatasManager.getInstance().prepareStatement(query);
    }

    public static Statement getStatement() throws IOException, SQLException {
        return DatasManager.getInstance().createStatement();
    }

    public static Fight findFightByUser(User user) throws IOException, SQLException {
        PreparedStatement psql = DatasManager.prepare("SELECT * FROM fights WHERE (brute_id1 = ? OR brute_id2 = ?) AND winner_id IS NULL");
        psql.setInt(1, user.getId());
        psql.setInt(2, user.getId());
        ResultSet rs = psql.executeQuery();
        if (rs.next()) {
            return FightEntity.create(rs);
        }
        return null;
    }

    public static String updateToken(int id) throws IOException, SQLException {
        String token = UUID.randomUUID().toString();

        User user = UserEntity.findById(id);
        user.setToken(token);
        DatasManager.save(user);
        
        return token;
    }
    
    public static <T> void save(T obj) throws IOException {
        try {
            Class classObj = Class.forName("brutes.db.entity." + obj.getClass().getSimpleName() + "Entity");
            
            classObj.getMethod("save", new Class[]{Connection.class, obj.getClass()}).invoke(null, DatasManager.getInstance(), obj);
            
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | IllegalArgumentException ex) {
            Logger.getLogger(DatasManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
