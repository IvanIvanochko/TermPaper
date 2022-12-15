import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
public class JDBC {
    public Connection dblink;
    public Connection getConnection(){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://DESKTOP-4AFFQ40:1433;databaseName=TaxiDepotApp;encrypt=true;trustServerCertificate=true;integratedSecurity=true;";
            dblink = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return dblink;
    }
}
