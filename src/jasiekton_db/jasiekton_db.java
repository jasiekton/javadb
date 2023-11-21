package jasiekton_db;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public final class jasiekton_db {

    public static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    public static final String JDBC_URL = "jdbc:derby:./db/jasiekton_db";
    public static final String QUERY = "select * from app.windows";
    private static java.sql.Connection conn;

    private jasiekton_db() {
    }

    public static boolean Connect() throws ClassNotFoundException, SQLException {
        conn = DriverManager.getConnection(JDBC_URL);
        if (conn == null) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean Disconnect() throws SQLException {
        if (conn == null) {
            return false;
        } else {
            conn.close();
            return true;
        }
    }

    public static String getData() throws SQLException {
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(QUERY);
        ResultSetMetaData rsmd = rs.getMetaData();
        String wiersz = new String();
        int colCount = rsmd.getColumnCount();
        for (int i = 1; i <= colCount; i++) {
            wiersz = wiersz.concat(rsmd.getColumnName(i) + " \t| ");
        }
        wiersz = wiersz.concat("\r\n");
        while (rs.next()) {
            System.out.println("");
            for (int i = 1; i <= colCount; i++) {
                if(i==2||i==6)
                    wiersz = wiersz.concat(rs.getString(i) + " \t\t| ");
                else
                wiersz = wiersz.concat(rs.getString(i) + " \t| ");
            }
            wiersz = wiersz.concat("\r\n");
        }
        if (stat != null) {
            stat.close();
        }
        return wiersz;
    }

    public static void NewRecord(int window_id, String manufacturer, String type, String material, String color, int thickness_mm, boolean is_tilting) throws SQLException {
        Statement stat = conn.createStatement();
        String command = "INSERT INTO APP.WINDOWS (WINDOW_ID, MANUFACTURER, TYPE, MATERIAL, COLOR, THICKNESS_MM, IS_TILTING)"
                + " VALUES (" + window_id + ", '" + manufacturer + "', '" + type + "', '" + material + "', '" + color + "', " + thickness_mm + ", " + is_tilting + ")";
        stat.executeUpdate(command);
    }

    public static void Delete(int window_id) throws SQLException {
        Statement stat = conn.createStatement();

        String command = "DELETE FROM WINDOWS "
                + " WHERE WINDOW_ID = " + window_id + "";
        stat.executeUpdate(command);
    }

    public static void Update(int window_id, String manufacturer, String type, String material, String color, int thickness_mm, boolean is_tilting) throws SQLException {
        Statement stat = conn.createStatement();

        String command = "UPDATE WINDOWS"
                + " SET MANUFACTURER = '" + manufacturer + "', TYPE = '" + type + "', MATERIAL = '" + material + "', COLOR = '" + color + "', THICKNESS_MM= " + thickness_mm + ", IS_TILTING= " + is_tilting + ""
                + " WHERE WINDOW_ID =  " + window_id + "";
        stat.executeUpdate(command);
    }
    public static boolean isWindowIdExist(int window_id) throws SQLException {
    Statement stat = conn.createStatement();
    ResultSet rs = stat.executeQuery("SELECT * FROM APP.WINDOWS WHERE WINDOW_ID = " + window_id);
    boolean exists = rs.next();
    if (stat != null) {
        stat.close();
    }
    return exists;
}
}
