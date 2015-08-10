
package screensframework;

import javafx.application.Platform;
import javax.swing.JOptionPane;

/**
 *
 * @author Rocio Carmona
 */
public class ControlesBasicos {
    
    public void salirSistema() {
        int pregunta = JOptionPane.showConfirmDialog(null, "Realmente desea salir del programa?");
        
        if (pregunta == 0) {
            Platform.exit();
        } 
    }
}
