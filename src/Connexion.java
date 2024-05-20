import	java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {
    public static Connection getConnection(String url,String username,String password){
        String nomDriver="com.mysql.jdbc.Driver";
        try {
            Class.forName(nomDriver);
            System.out.println("Driver chargé..");
        } catch (ClassNotFoundException e) {
            //throw new RuntimeException(e);
            System.out.println("Erreur chargement Driver"+e.getMessage());
        }

        //Connection a la BD
        java.sql.Connection con=null;

        try {
            con= DriverManager.getConnection(url,username,password);
            System.out.println("Connected");
            return con;
        } catch (SQLException e) {
            //throw new RuntimeException(e);
            System.out.println("erreur connexion"+e.getMessage());
        }
        return null;
    }
}
