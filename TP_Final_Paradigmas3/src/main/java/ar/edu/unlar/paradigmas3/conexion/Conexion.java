package ar.edu.unlar.paradigmas3.conexion;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Conexion {
    public static Connection getconexion(){
        Connection conexion = null;
        Dotenv dotenv = Dotenv.load();

        var basedatos = dotenv.get("DB_NAME");
        var usuario = dotenv.get("DB_USER");
        var contraseña = dotenv.get("DB_PASSWORD");
        var host = dotenv.get("DB_HOST");
        var puerto = dotenv.get("DB_PORT");
        var url = "jdbc:postgresql://"+ host + ":" + puerto + "/" + basedatos;

        try{
            Class.forName("org.postgresql.Driver");
            conexion= DriverManager.getConnection(url,usuario,contraseña);
        }catch (Exception e){
            System.out.println("error al conectarse ala base de datos: "+ e.getMessage());
        }
        return conexion;

    }
    // -------------------------------------------
    // MÉTODOS AUXILIARES PARA CERRAR RECURSOS
    // -------------------------------------------

    // Cierra la conexión de forma segura. */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    // Cierra el PreparedStatement de forma segura. /
    public static void close(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (Exception e) {
                System.err.println("Error al cerrar el PreparedStatement: " + e.getMessage());
            }
        }
    }

//Cierra el ResultSet de forma segura./
 public static void close(ResultSet rs) {
    if (rs != null) {
        try {
            rs.close();
        } catch (Exception e) {
            System.err.println("Error al cerrar el ResultSet: " + e.getMessage());
             }
        }
        }


}

