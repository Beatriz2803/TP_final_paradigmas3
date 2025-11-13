package ar.edu.unlar.paradigmas3.utilidades;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Validaciones {

    /**
     * Valida que un campo de texto no esté vacío y cumpla con el mínimo de 3 caracteres.
     * @param campo JTextField a validar.
     * @param nombreCampo Nombre visible del campo para el mensaje de error.
     * @return true si es válido, false si falla.
     */
    public static boolean validarTextoMinimo(JTextField campo, String nombreCampo) {
        String texto = campo.getText().trim();

        if (texto.length() < 3) {
            JOptionPane.showMessageDialog(
                    null,
                    "El campo '" + nombreCampo + "' debe tener al menos 3 caracteres.",
                    "Error de Validación",
                    JOptionPane.ERROR_MESSAGE
            );
            campo.requestFocus(); // Enfoca el campo para que el usuario corrija
            return false;
        }
        return true;
    }

    /**
     * Valida que un campo sea un número, no esté vacío y sea estrictamente mayor a 0.
     * @param campo JTextField a validar.
     * @param nombreCampo Nombre visible del campo para el mensaje de error.
     * @return true si es válido, false si falla.
     */
    public static boolean validarNumeroMayorCero(JTextField campo, String nombreCampo) {
        String texto = campo.getText().trim();

        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El campo '" + nombreCampo + "' no puede estar vacío.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            campo.requestFocus();
            return false;
        }

        try {
            double valor = Double.parseDouble(texto);
            if (valor <= 0) {
                JOptionPane.showMessageDialog(
                        null,
                        "El campo '" + nombreCampo + "' debe ser un valor numérico mayor a 0.",
                        "Error de Validación",
                        JOptionPane.ERROR_MESSAGE
                );
                campo.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "El campo '" + nombreCampo + "' solo acepta números válidos.",
                    "Error de Validación",
                    JOptionPane.ERROR_MESSAGE
            );
            campo.requestFocus();
            return false;
        }
        return true;
    }
}