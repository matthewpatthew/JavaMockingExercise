package cz.upce.fei.inptp.databasedependency.dao;

import cz.upce.fei.inptp.databasedependency.business.AuthenticationService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Database wrapper.
 */
public class Database {

    private static Database instance;
    private Connection connection;

    public Database() {
        instance = this;
    }

    public static Database getInstance() {
        return instance;
    }

    public void open() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
            seedDatabase();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void seedDatabase() throws SQLException {
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        statement.executeUpdate("drop table if exists person");
        statement.executeUpdate("create table person (id integer, name string, password string)");

        statement.executeUpdate("insert into person values(1, 'leo', '" + AuthenticationService.encryptPassword("1234") + "')");
        statement.executeUpdate("insert into person values(2, 'yui', '" + AuthenticationService.encryptPassword("12345") + "')");
        statement.executeUpdate("insert into person values(3, 'johnny', '" + AuthenticationService.encryptPassword("xxx") + "')");

        statement.executeUpdate("drop table if exists role");
        statement.executeUpdate("create table role (id integer, section string, access string, modifier string)");

        statement.executeUpdate("insert into role values(1, '/', 'admin', '')");
        statement.executeUpdate("insert into role values(2, '/', 'user', '')");
        statement.executeUpdate("insert into role values(2, '/hr', 'rw', '')");
        statement.executeUpdate("insert into role values(2, '/finance', 'ro', '')");
        statement.executeUpdate("insert into role values(3, '/', 'user', '')");
        statement.executeUpdate("insert into role values(3, '/finance', 'rw', '')");
        statement.executeUpdate("insert into role values(3, '/finance/report', 'ro', '')");
    }

    public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void rollback() throws SQLException {
        connection.rollback();
    }

    public void close() throws SQLException {
        connection.close();
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return connection.createStatement(resultSetType, resultSetConcurrency);
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        connection.rollback(savepoint);
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

}
