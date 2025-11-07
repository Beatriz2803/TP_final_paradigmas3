package ar.edu.unlar.paradigmas3;

import ar.edu.unlar.paradigmas3.conexion.Conexion;
import ar.edu.unlar.paradigmas3.modelo.*;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
            try (Connection conexion = Conexion.getconexion()){
                if(conexion != null) {
                    System.out.println("conexion exitosa: "+conexion);
                }
            }catch (Exception e ){
                System.err.println("error al conectarse a la base datos : "+e.getMessage());
            }
        }
}
