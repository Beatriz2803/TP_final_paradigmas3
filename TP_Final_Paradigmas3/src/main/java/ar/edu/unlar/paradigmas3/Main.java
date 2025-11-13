package ar.edu.unlar.paradigmas3;

import ar.edu.unlar.paradigmas3.conexion.Conexion;
import ar.edu.unlar.paradigmas3.gui.Dashboard;
import java.sql.Connection;


public class Main {
    public static void main(String[] args) {
        try (Connection conexion = Conexion.getconexion()) {
            if (conexion != null) {
                System.out.println("-----------------------------------------------------------------------");
                System.out.println("Conexión exitosa ✅: " + conexion);
                System.out.println("-----------------------------------------------------------------------");
            } else {
                System.out.println("❌ FATAL: No se pudo establecer la conexión.");
                return; // Detener si la conexión falla
            }
        } catch (Exception e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        
        java.awt.EventQueue.invokeLater(() -> 
                new Dashboard().setVisible(true));
        
    }
}

