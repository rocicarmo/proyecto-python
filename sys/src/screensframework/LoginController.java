

/*
 * Controlador de la ventana Login de Usuario
 * @author Rocio Carmona
 */
package screensframework;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import org.apache.commons.codec.digest.DigestUtils;
import screensframework.DBConnect.DBConnection;
import screensframework.ContenidoController;

/**
 * FXML Controller de la ventana Login de Usuario
 *
 * @author Rocio Carmona
 */
public class LoginController implements Initializable, ControlledScreen 
{
    @FXML
    private Label Usuario;
    ScreensController controlador;
    private Validaciones validation = new Validaciones();
    private Connection conexion;
    
    private ContenidoController contenido;
    private ContenidoController conte=new ContenidoController();
    public TextField tfUsuario;
    public PasswordField tfPass;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @Override
    public void setScreenParent(ScreensController pantallaPadre) {
        controlador = pantallaPadre;
        
    }
    
    @FXML
    private void iniciarSesion(ActionEvent event){
        /********************************** 
         *         Area de validaciones 
         ***********************************/
        if (!validation.validarVacios(tfUsuario.getText(), "USUARIO")) {
            return;
        }
        
        
        if (!validation.validarMaximo(tfUsuario.getText(), "USUARIO", 20, 2)) {
            return;
        }
        
        /********************************** 
         *     Fin de las validaciones 
         ***********************************/
        
        //______________________________________________________
        /* SE HACE EL LLAMADO AL MODELO PARA ENTRAR AL SISTEMA */
        try {
            conexion = DBConnection.connect();
            String sql = "SELECT * FROM "
                    + " usuarios WHERE "
                    + " usu_usuario = '"+tfUsuario.getText()+"' AND "
                    + " usu_pass = '"+DigestUtils.shaHex(tfPass.getText())+"'";
            ResultSet rs = conexion.createStatement().executeQuery(sql);
            
            boolean existeUsuario = rs.next();
            
            if (existeUsuario) {
                tfUsuario.setText("");
                tfPass.setText("");
                 String usuario=tfUsuario.getText();
               // conte.usuarioNombre("hola");
                controlador.setScreen(ScreensFramework.contenidoID);
               
              
                
            } else {
                JOptionPane.showMessageDialog(null, "Este usuario no está registrado");
            }
            
        } catch (SQLException e) {
            System.out.println("Error " + e.getMessage());
        }
    }
    
    @FXML
    private void irFormRegistro(ActionEvent event) {
        controlador.setScreen(ScreensFramework.registroID);
    }
    
    @FXML
    private void salir(ActionEvent event) {
        Platform.exit();
    }
}
