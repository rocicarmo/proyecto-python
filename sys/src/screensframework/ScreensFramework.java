 

package screensframework;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Rocio Carmona
 */
 public class ScreensFramework extends Application {
    
    public static String loginID = "main";
    public static String loginFile = "Login.fxml";
    
    public static String registroID = "registro";
    public static String registroFile = "Registro.fxml";
    
    public static String contenidoID = "contenido";
    public static String contenidoFile = "Contenido.fxml";
    
    public static String mantenimientoClienteID = "clientes";
    public static String mantenimientoClienteFile = "Clientes.fxml";
    
     public static String mantenimientoMascotaID = "mascota";
    public static String mantenimientoMascotaFile = "Mascotas.fxml";
    
     public static String mantenimientoServicioID = "servicios";
    public static String mantenimientoServicioFile = "Servicios.fxml";
    
       public static String mantenimientoMedicamentosID = "medicamentos";
    public static String mantenimientoMedicamentosFile = "Medicamentos.fxml";
    
    
    public static String mantenimientoConsultaID = "consultas";
    public static String mantenimientoConsultaFile = "Consultas.fxml";
    
    
         public static String mantenimientoVacunacionID = "Vacunacion";
    public static String mantenimientoVacunacionFile = "Vacunacion.fxml";
    
     public static String mantenimientoHistorialID = "Historial";
    public static String mantenimientoHistorialFile = "HistorialMascota.fxml";
    
    
    @Override
    public void start(Stage primaryStage) {
        
        ScreensController mainContainer = new ScreensController();
        mainContainer.loadScreen(ScreensFramework.loginID, ScreensFramework.loginFile);
        mainContainer.loadScreen(ScreensFramework.registroID, ScreensFramework.registroFile);
        mainContainer.loadScreen(ScreensFramework.contenidoID, ScreensFramework.contenidoFile);
         mainContainer.loadScreen(ScreensFramework.mantenimientoClienteID, ScreensFramework.mantenimientoClienteFile);
          mainContainer.loadScreen(ScreensFramework.mantenimientoMascotaID, ScreensFramework.mantenimientoMascotaFile);
          mainContainer.loadScreen(ScreensFramework.mantenimientoServicioID, ScreensFramework.mantenimientoServicioFile);
         mainContainer.loadScreen(ScreensFramework.mantenimientoConsultaID, ScreensFramework.mantenimientoConsultaFile);
        mainContainer.loadScreen(ScreensFramework.mantenimientoMedicamentosID, ScreensFramework.mantenimientoMedicamentosFile);
        mainContainer.loadScreen(ScreensFramework.mantenimientoVacunacionID, ScreensFramework.mantenimientoVacunacionFile);
        mainContainer.loadScreen(ScreensFramework.mantenimientoHistorialID, ScreensFramework.mantenimientoHistorialFile);

        mainContainer.setScreen(ScreensFramework.loginID);
        
        Group root = new Group();
        root.getChildren().addAll(mainContainer);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }  
    public static void main(String[] args) {
        launch(args);
    }
}
