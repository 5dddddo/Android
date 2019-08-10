package common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DBTemplate {
    
    static int count;

    public DBTemplate() {
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            
            Context initContext = new InitialContext();
            DataSource ds = (DataSource) initContext.lookup("java:comp/env/jdbc/mySQLDB");
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            
        } catch (Exception e) {
            System.out.println("[JdbcTemplate.getConnection] : " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    public static boolean isConnected(Connection conn) {

        boolean validConnection = true;

        try {
            if (conn == null || conn.isClosed())
                validConnection = false;
        } catch (SQLException e) {
            validConnection = false;
            e.printStackTrace();
        }
        return validConnection;
    }

    public static void close(Connection conn) {

        if (isConnected(conn)) {
            try {
                conn.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(Statement stmt) {

        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(ResultSet rset) {

        try {
            if (rset != null)
                rset.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void commit(Connection conn) {

        try {
            if (isConnected(conn)) {
                conn.commit();
                System.out.println("[JdbcTemplate.commit] : DB Successfully Committed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rollback(Connection conn) {

        try {
            if (isConnected(conn)) {
                conn.rollback();
                System.out.println("[JdbcTemplate.rollback] : DB Successfully Rollbacked!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}