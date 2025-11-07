package ar.edu.unlar.paradigmas3.conexion;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;

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
}

