/*
 * Controlador para la carga de Pantalla.

 */ 

package screensframework;

import java.util.HashMap;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 *
 * @author Rocio Carmona
 */
public class ScreensController  extends StackPane{


    private HashMap<String, Node> screens = new HashMap<>();
    
    public ScreensController() {
        super();
    }

   
    public void addScreen(String name, Node screen) {
        screens.put(name, screen);
    }

    public Node getScreen(String name) {
        return screens.get(name);
    }

    public boolean loadScreen(String name, String resource) {
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            Parent loadScreen = (Parent) myLoader.load();
            ControlledScreen myScreenControler = ((ControlledScreen) myLoader.getController());
            myScreenControler.setScreenParent(this);
            addScreen(name, loadScreen);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public boolean setScreen(final String name) {       
       
         Node screenToRemove;
         if(screens.get(name) != null){  
             if(!getChildren().isEmpty()){  
                getChildren().add(0, screens.get(name)); 
                screenToRemove = getChildren().get(1);
                getChildren().remove(1);                   
             }else{
                getChildren().add(screens.get(name));      
             }
             return true;
         }else {
             System.out.println("screen hasn't been loaded!!! \n");
             return false;
         }
    }

   
    public boolean unloadScreen(String name) {
        if (screens.remove(name) == null) {
            System.out.println("Screen didn't exist");
            return false;
        } else {
            return true;
        }
    }
    
}

