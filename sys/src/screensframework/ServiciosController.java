
/*
 * Controlador de la ventana Servicios
 * @author Rocio Carmona
 */

package screensframework;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import screensframework.DBConnect.DBConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.Label;

public class ServiciosController implements Initializable, ControlledScreen {
    
    ScreensController controlador;
    private ControlesBasicos controlesBasicos = new ControlesBasicos();
    @FXML private Button btAddServicio;
    @FXML private Button btModificarServicio;
    @FXML private Button btEliminarServicio;
    @FXML private Button btNuevoServicio;
    
    @FXML private TextField tfDescripcionServicio;
    @FXML private TextField tfPrecioServicio;
    @FXML private TextField tfBuscarServicio;
    @FXML private Label lbCodigoServicio;
    
    @FXML private TableView tablaServicio;
    @FXML private TableColumn col;
    private Connection conexion;
    
    ObservableList<ObservableList> servicio;
    
     @FXML
    private void irMantenimientoServicio(ActionEvent  event) {
       
       controlador.setScreen(ScreensFramework.mantenimientoServicioID);
    }
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        this.cargarDatosTabla();
        
        btEliminarServicio.setDisable(true);
        btModificarServicio.setDisable(true);
        btEliminarServicio.setStyle("-fx-background-color:grey");
        btModificarServicio.setStyle("-fx-background-color:grey");
        
        ObservableList<Object> categoriaID = FXCollections.observableArrayList();
        ObservableList<Object> categoriaNomnre = FXCollections.observableArrayList();
        ObservableList<Object> subCategoria = FXCollections.observableArrayList();
        ObservableList<Object> marcas = FXCollections.observableArrayList();
        
        try {
            conexion = DBConnection.connect();
            
           
        } catch (SQLException e) {
            System.out.println("Error " + e);
        }
    }
    
    @Override
    public void setScreenParent(ScreensController pantallaPadre) {
        controlador = pantallaPadre;
    }
    
    public  void cargarDatosTabla() {
         servicio = FXCollections.observableArrayList();
         
         try{
            conexion = DBConnection.connect();
            
            String sql = "SELECT ser_codservicio, "
                    + " ser_descripcion, "
                    + " ser_precio "
                    + " FROM servicios "
                    + " ORDER BY ser_descripcion DESC";
            //ResultSet
            ResultSet rs = conexion.createStatement().executeQuery(sql);
            // Títulos de las columnas
            String[] titulos = {
                    "Cod",
                    "Descripción",
                    "Precio",
            };
            
            
            /**********************************
             * Columna cargada dinamicamente *
             **********************************/
            
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++ ) {
                final int j = i;
                col = new TableColumn(titulos[i]);
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>(){                   
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> parametro) {                                                                                             
                        return new SimpleStringProperty((String)parametro.getValue().get(j));                       
                    }                   
                });
                tablaServicio.getColumns().addAll(col);
                // Asignamos un tamaño a ls columnnas
                col.setMinWidth(100);
                //System.out.println("Column ["+i+"] ");
                // Centrar los datos de la tabla
                col.setCellFactory(new Callback<TableColumn<String,String>,TableCell<String,String>>(){
                    @Override
                    public TableCell<String, String> call(TableColumn<String, String> p) {
                        TableCell cell = new TableCell(){
                            @Override
                            protected void updateItem(Object t, boolean bln) {
                                if(t != null){
                                    super.updateItem(t, bln);
                                    //System.out.println(t);
                                    setText(t.toString());
                                    setAlignment(Pos.CENTER); //Setting the Alignment
                                }
                            }
                        };
                        return cell;
                    }
                });
            }
            /********************************
             * Cargamos de la base de datos *
             ********************************/
            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i = 1 ; i <= rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                //System.out.println("Row [1] added "+row );
                servicio.addAll(row);
            }
            //FINALLY ADDED TO TableView
            tablaServicio.setItems(servicio);
            rs.close();
          }catch(SQLException e){
              System.out.println("Error "+e);            
          }
    }
    
    public void cargarServiciosText(String valor) {
        
        try {
            
            conexion = DBConnection.connect();
            String sql = "SELECT * "
                    + " FROM servicios "
                    + " WHERE ser_codservicio = "+valor;
            ResultSet rs = conexion.createStatement().executeQuery(sql);
     /*
            cli_cedula, "
                    + " cli_nombre, "
                    + " cli_direccion, "
                    + " cli_telefono "
                    + " FROM clientes
            */       
            while (rs.next()) {
                lbCodigoServicio.setText(rs.getString("ser_codservicio"));
                tfDescripcionServicio.setText(rs.getString("ser_descripcion"));
                tfPrecioServicio.setText(rs.getString("ser_precio"));  
                
                
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error "+ex);
        }
        
    }
    
    @FXML
    private void getServicioSeleccionado(MouseEvent event) {
       tablaServicio.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (tablaServicio != null) {
                    
                    btAddServicio.setDisable(true);
                    btEliminarServicio.setDisable(false);
                    btModificarServicio.setDisable(false);
                    btAddServicio.setStyle("-fx-background-color:grey");
                    btEliminarServicio.setStyle("-fx-background-color:#66CCCC");
                    btModificarServicio.setStyle("-fx-background-color:#66CCCC");
                    
                    String valor = tablaServicio.getSelectionModel().getSelectedItems().get(0).toString();
                    
                    String cincoDigitos = valor.substring(1, 6);
                    String cuatroDigitos = valor.substring(1, 5);
                    String tresDigitos = valor.substring(1, 4);
                    String dosDigitos = valor.substring(1, 3);
                    String unDigitos = valor.substring(1, 2);
                    
                    Pattern p = Pattern.compile("^[0-9]*$");
                    
                    Matcher m5 = p.matcher(cincoDigitos);
                    Matcher m4 = p.matcher(cuatroDigitos);
                    Matcher m3 = p.matcher(tresDigitos);
                    Matcher m2 = p.matcher(dosDigitos);
                    
                    if (m5.find()) {
                        cargarServiciosText(cincoDigitos);
                    } else {
                        if (m4.find()) {
                            cargarServiciosText(cuatroDigitos);
                        } else {
                            if (m3.find()) {
                                cargarServiciosText(tresDigitos);
                            } else {
                                if (m2.find()) {
                                    cargarServiciosText(dosDigitos);
                                } else {
                                    cargarServiciosText(unDigitos);
                                }
                             }
                        }
                    }
                }
            }
        });
    }
    
    @FXML
    private void addServicio(ActionEvent event) {
    try {
        
            if(tfDescripcionServicio.getText().trim().length()==0 ){
                
                JOptionPane.showMessageDialog(null, "Descripción no puede ser nulo");
                
            }else if(tfPrecioServicio.getText().trim().length()==0 ){
                
                JOptionPane.showMessageDialog(null, "Precio no puede ser nulo");
                
            }else if(tfPrecioServicio.getText().matches("[^0-9]*")){
                
                JOptionPane.showMessageDialog(null, "Número de telefono debe ser numérico");
                
            }else{
            conexion = DBConnection.connect();
           
            String sql = "INSERT INTO servicios "
                    + " (ser_descripcion, ser_precio) "
                    + " VALUES (?, ?)";
            PreparedStatement estado = conexion.prepareStatement(sql);
            
            estado.setString(1,(tfDescripcionServicio.getText().toUpperCase()));
            estado.setString(2, tfPrecioServicio.getText().toUpperCase());
           
            int n  = estado.executeUpdate();
           
            if (n > 0) {
                tablaServicio.getColumns().clear();
                tablaServicio.getItems().clear();
                cargarDatosTabla();
            }
            tfDescripcionServicio.setText("");
            tfPrecioServicio.setText("");
            estado.close();
                        
            }
        } catch (SQLException e) {
            System.out.println("Error " + e);
        }
        
    }
    
    @FXML
    private void modificarServicio(ActionEvent event) {
   
        try {
            if(tfDescripcionServicio.getText().trim().length()==0 ){
                
                JOptionPane.showMessageDialog(null, "Descripción no puede ser nulo");
                
            }else if(tfPrecioServicio.getText().trim().length()==0 ){
                
                JOptionPane.showMessageDialog(null, "Precio no puede ser nulo");
                
            }else if(tfPrecioServicio.getText().matches("[^0-9]*")){
                
                JOptionPane.showMessageDialog(null, "Precio debe ser numérico");
                
            }else{
            conexion = DBConnection.connect();
            
            String sql = "UPDATE servicios "
                    + " SET ser_descripcion = ?, "
                    + " ser_precio = ? "
                    + " WHERE ser_codservicio = "+lbCodigoServicio.getText()+"";
            
            PreparedStatement estado = conexion.prepareStatement(sql);
            
            estado.setString(1,(tfDescripcionServicio.getText().toUpperCase()));
            estado.setString(2, (tfPrecioServicio.getText().toUpperCase()));
         
            int n = estado.executeUpdate();
            
            if (n > 0) {
                tablaServicio.getColumns().clear();
                tablaServicio.getItems().clear();
                cargarDatosTabla();
            }
             tfDescripcionServicio.setText("");
            tfPrecioServicio.setText("");
            estado.close();
            }
        } catch (SQLException e) {
            System.out.println("Error " + e);
        }
    }
    
    @FXML
    private void eliminarServicio(ActionEvent event) {
        
        int confirmarEliminar = JOptionPane.showConfirmDialog(null, "Realmente desea eliminar este SERVICIO??");
        
        if (confirmarEliminar == 0) {
            try {
                conexion = DBConnection.connect();
                String sql = "DELETE FROM servicios WHERE ser_codservicio = "+lbCodigoServicio.getText()+"";
                PreparedStatement estado = conexion.prepareStatement(sql);
                int n = estado.executeUpdate();
                if (n > 0) {
                    tablaServicio.getColumns().clear();
                    tablaServicio.getItems().clear();
                    cargarDatosTabla();
                }

                estado.close();

            } catch (SQLException e) {
                System.out.println("Error " + e);
            }
        }
    }
    
    @FXML
    private void buscarServicio(ActionEvent event) {
        
        tablaServicio.getItems().clear();
        try {
            conexion = DBConnection.connect();
             String sql = "SELECT ser_codservicio, "
                    + " ser_descripcion, "
                    + " ser_precio "
                    + " FROM servicios "
                    + " WHERE upper(ser_descripcion) LIKE '%"+tfBuscarServicio.getText().toUpperCase()+"%'"
                    + " ORDER BY ser_descripcion DESC";
            
            ResultSet rs = conexion.createStatement().executeQuery(sql);
            
            while(rs.next()){
                
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i = 1 ; i <= rs.getMetaData().getColumnCount(); i++){
                   
                    row.add(rs.getString(i));
                }
                servicio.addAll(row);
            }
            tablaServicio.setItems(servicio);
            rs.close();
            
        } catch (SQLException e) {
            System.out.println("Error " + e.getMessage());
        }
        
    }
    
    @FXML
    private void nuevoServicio(ActionEvent event) {
        
        btAddServicio.setDisable(false);
        btEliminarServicio.setDisable(true);
        btModificarServicio.setDisable(true);
        btAddServicio.setStyle("-fx-background-color:#66CCCC");
        btEliminarServicio.setStyle("-fx-background-color:grey");
        btModificarServicio.setStyle("-fx-background-color:grey");
         tfDescripcionServicio.setText("");
            tfPrecioServicio.setText("");
            lbCodigoServicio.setText("");
    }
    
  @FXML
    private void irMantenimientoCliente(ActionEvent  event) {
       
       controlador.setScreen(ScreensFramework.mantenimientoClienteID);
    }
    
     @FXML
    private void irMantenimientoMascotas(ActionEvent  event) {
       
       controlador.setScreen(ScreensFramework.mantenimientoMascotaID);
    }
    
  
     @FXML
    private void irMantenimientoConsulta(ActionEvent  event) {
       
       controlador.setScreen(ScreensFramework.mantenimientoConsultaID);
    }
    
     @FXML
    private void irMantenimientoMedicamentos(ActionEvent  event) {
       
       controlador.setScreen(ScreensFramework.mantenimientoMedicamentosID);
    }
    
     @FXML
    private void irMantenimientoVacunacion(ActionEvent  event) {
       
       controlador.setScreen(ScreensFramework.mantenimientoVacunacionID);
    }
    
     @FXML
    private void irInicioContenido(ActionEvent event) {
        controlador.setScreen(ScreensFramework.contenidoID);
    }
    
       
    @FXML
    private void salir(ActionEvent event) {
        this.controlesBasicos.salirSistema();
    }
    
    @FXML
    private void cerrarSesion(ActionEvent event) {
        controlador.setScreen(ScreensFramework.loginID);
    }
}
