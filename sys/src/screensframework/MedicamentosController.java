
/*
 * Controlador de la ventana Medicamentos
 * @author Rocio Carmona
 */

package screensframework;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import screensframework.DBConnect.DBConnection;

public class MedicamentosController implements Initializable, ControlledScreen {
    
    ScreensController controlador;
    private ControlesBasicos controlesBasicos = new ControlesBasicos();
    @FXML private Button btAddMedicamentos;
    @FXML private Button btModificarMedicamentos;
    @FXML private Button btEliminarMedicamentos;
    @FXML private Button btNuevoMedicamentos;
    
     @FXML private TextField tfBuscarMedicamentos;
    
    @FXML private TextField tfNombreMedicamentos;
    @FXML private TextField tfVencimientoMedicamentos;
    @FXML private TextField tfLoteMedicamentos; 
    @FXML private TextField tfPrecioMedicamentos;
    @FXML private TextField tfStockMedicamentos;
   @FXML private TextField tfObservacionMedicamentos; 
    @FXML private TextField tfDescripcion;
     @FXML private TextField tfTipo;
    @FXML private Label lbCodigoMedicamentos;
    
    
    
    
    
    @FXML private TableView tablaMedicamentos;
    @FXML private TableColumn col;
    private Connection conexion;
    
    ObservableList<ObservableList> medicamentos;
    
     @FXML
    private void irMantenimientoMedicamentos(ActionEvent  event) {
       
       controlador.setScreen(ScreensFramework.mantenimientoMedicamentosID);
    }
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        this.cargarDatosTabla();
        
        
     /*   btEliminarMedicamentos.setDisable(true);
        btModificarMedicamentos.setDisable(true);
        btEliminarMedicamentos.setStyle("-fx-background-color:grey");
        btModificarMedicamentos.setStyle("-fx-background-color:grey");*/
        
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
         medicamentos = FXCollections.observableArrayList();
         
         try{
            conexion = DBConnection.connect();
            
            String sql = "SELECT med_codmedicamentos, "
                    + " med_nombre, "
                    + " med_vencimiento, "
                    + " med_descripcion, "
                    + " med_lote, "
                    + " med_precio, "
                    + " med_stock, "
                    + " med_observacion, "
                    + " med_tipo "
                    + " FROM medicamentos "
                    + " ORDER BY med_nombre DESC";
            //ResultSet
            ResultSet rs = conexion.createStatement().executeQuery(sql);
            // Títulos de las columnas
            String[] titulos = {
                    "Cod",
                    "Nombre",
                    "Vencimiento",
                    "Descripcion",
                    "Lote",
                    "Precio",
                    "Stock",
                    "Estado",
                     "Tipo",
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
                tablaMedicamentos.getColumns().addAll(col);
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
                medicamentos.addAll(row);
            }
            //FINALLY ADDED TO TableView
            tablaMedicamentos.setItems(medicamentos);
            rs.close();
          }catch(SQLException e){
              System.out.println("Error "+e);            
          }
    }
    
    public void cargarMedicamentosText(String valor) {
        
        try {
            
            conexion = DBConnection.connect();
            String sql = "SELECT * "
                    + " FROM medicamentos "
                    + " WHERE med_codmedicamentos = "+valor;
            
            
            ResultSet rs = conexion.createStatement().executeQuery(sql);
     /*
            cli_cedula, "
                    + " cli_nombre, "
                    + " cli_direccion, "
                    + " cli_telefono "
                    + " FROM clientes
            */       
            while (rs.next()) {
                lbCodigoMedicamentos.setText(rs.getString("med_codmedicamentos"));
                tfNombreMedicamentos.setText(rs.getString("med_nombre"));
                 tfDescripcion.setText(rs.getString("med_descripcion"));
                tfVencimientoMedicamentos.setText(rs.getString("med_vencimiento"));
                tfLoteMedicamentos.setText(rs.getString("med_lote"));
                tfPrecioMedicamentos.setText(rs.getString("med_precio"));
                tfStockMedicamentos.setText(rs.getString("med_stock"));
                tfObservacionMedicamentos.setText(rs.getString("med_observacion"));
                tfTipo.setText(rs.getString("med_tipo"));
                
           }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error "+ex);
        }
        
    }
    
    @FXML
    private void getMedicamentosSeleccionado(MouseEvent event) {
        tablaMedicamentos.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (tablaMedicamentos != null) {
                    
                    btAddMedicamentos.setDisable(true);
                    btEliminarMedicamentos.setDisable(false);
                    btModificarMedicamentos.setDisable(false);
                    btAddMedicamentos.setStyle("-fx-background-color:grey");
                    btEliminarMedicamentos.setStyle("-fx-background-color:#66CCCC");
                    btModificarMedicamentos.setStyle("-fx-background-color:#66CCCC");                
              
                    
                    String valor = tablaMedicamentos.getSelectionModel().getSelectedItems().get(0).toString();
                    
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
                        cargarMedicamentosText(cincoDigitos);
                    } else {
                        if (m4.find()) {
                            cargarMedicamentosText(cuatroDigitos);
                        } else {
                            if (m3.find()) {
                                cargarMedicamentosText(tresDigitos);
                            } else {
                                if (m2.find()) {
                                    cargarMedicamentosText(dosDigitos);
                                } else {
                                    cargarMedicamentosText(unDigitos);
                                }
                             }
                        }
                    }
                }
            }
        });
    }
    
    @FXML
    private void addMedicamentos(ActionEvent event) throws SQLException {
          //int indiceCategoria = cbCategoriaCliente.getSelectionModel().getSelectedIndex() + 1;
        //int indiceMarca = cbMarcaCliente.getSelectionModel().getSelectedIndex() + 1;
      
       
        try {
             if (tfNombreMedicamentos.getText().trim().length() == 0) {

            JOptionPane.showMessageDialog(null, "Nombre de medicamento no puede ser nulo");

        }else if (tfVencimientoMedicamentos.getText().trim().length() == 0) {

                JOptionPane.showMessageDialog(null, "Vencimiento no puede ser nulo");

            } else if (tfLoteMedicamentos.getText().trim().length() == 0) {

                JOptionPane.showMessageDialog(null, "Lote no puede ser nulo");

            } else if (tfPrecioMedicamentos.getText().trim().length() == 0) {

                JOptionPane.showMessageDialog(null, "Precio no puede ser nulo");

            } else if (tfPrecioMedicamentos.getText().matches("[^0-9]*")) {

                JOptionPane.showMessageDialog(null, "Precio debe ser numérico");

            } else if (tfStockMedicamentos.getText().matches("[^0-9]*")) {

                JOptionPane.showMessageDialog(null, "Cantidad en stock debe ser numérico");

            } else if (tfStockMedicamentos.getText().trim().length() == 0) {

                JOptionPane.showMessageDialog(null, "Cantidad en stock no puede ser nulo");

            }else if (tfStockMedicamentos.getText().matches("[^0-9]*")) {

                JOptionPane.showMessageDialog(null, "Número de stock debe ser numérico");

            } else if (tfTipo.getText().trim().length() == 0) {

                JOptionPane.showMessageDialog(null, "Tipo no puede ser nulo");

            }else {
            conexion = DBConnection.connect();
            String sql = "INSERT INTO medicamentos "
                    + " (med_nombre, med_descripcion, med_vencimiento,  med_lote, med_precio,med_stock,med_observacion, med_tipo) "
                    + " VALUES (?,?,?, ?,?,?,?,?)";
            PreparedStatement estado = conexion.prepareStatement(sql);
            estado.setString(1,(tfNombreMedicamentos.getText()));
              estado.setString(2,(tfDescripcion.getText()));
            estado.setString(3,( tfVencimientoMedicamentos.getText()));
            estado.setString(4,(tfLoteMedicamentos.getText()));
            estado.setString(5, (tfPrecioMedicamentos.getText()));
            estado.setString(6,(tfStockMedicamentos.getText()));
            estado.setString(7,( tfObservacionMedicamentos.getText()));
            estado.setString(8,( tfTipo.getText()));
          
            int n  = estado.executeUpdate();
            
            if (n > 0) {
                tablaMedicamentos.getColumns().clear();
                tablaMedicamentos.getItems().clear();
                cargarDatosTabla();
            }
             tfNombreMedicamentos.setText("");
                tfVencimientoMedicamentos.setText("");
                tfLoteMedicamentos.setText("");
                tfStockMedicamentos.setText("");
                tfPrecioMedicamentos.setText("");
                tfObservacionMedicamentos.setText("");
                tfTipo.setText("");
            estado.close();
            }
        } catch (SQLException e) {
            System.out.println("Error " + e);
        }
        
    }
    
    @FXML
    private void modificarMedicamentos(ActionEvent event) {
    
        try {
            conexion = DBConnection.connect();
            
            String sql = "UPDATE medicamentos "
                    + " SET med_nombre = ?, "
                    + " med_vencimiento = ? "
                     + " med_lote = ? "
                    + " med_precio = ? "
                    + " med_stock = ? "
                    + " med_observacion = ? "
                    + " med_tipo = ? "
                    + " WHERE med_codmedicamentos = "+lbCodigoMedicamentos.getText()+"";
            
            PreparedStatement estado = conexion.prepareStatement(sql);
            
            estado.setString(1,(tfNombreMedicamentos.getText()));
            estado.setString(2, (tfVencimientoMedicamentos.getText()));
            estado.setString(3,(tfLoteMedicamentos.getText()));
            estado.setString(4, (tfPrecioMedicamentos.getText()));
            estado.setString(5,(tfStockMedicamentos.getText()));
            estado.setString(6, (tfObservacionMedicamentos.getText()));
            estado.setString(7, (tfTipo.getText()));
            
      
         
            int n = estado.executeUpdate();
            
            if (n > 0) {
                tablaMedicamentos.getColumns().clear();
                tablaMedicamentos.getItems().clear();
                cargarDatosTabla();
            }
            
            estado.close();
        } catch (SQLException e) {
            System.out.println("Error " + e);
        }
    }
    
    @FXML
    private void eliminarMedicamentos(ActionEvent event) {
        
        int confirmarEliminar = JOptionPane.showConfirmDialog(null, "Realmente desea eliminar este MEDICAMENTO??");
        
        if (confirmarEliminar == 0) {
            try {
                conexion = DBConnection.connect();
                String sql = "DELETE FROM medicamentos WHERE med_codmedicamentos = "+lbCodigoMedicamentos.getText()+"";
                PreparedStatement estado = conexion.prepareStatement(sql);
                int n = estado.executeUpdate();
                if (n > 0) {
                    tablaMedicamentos.getColumns().clear();
                    tablaMedicamentos.getItems().clear();
                    cargarDatosTabla();
                }

                estado.close();

            } catch (SQLException e) {
                System.out.println("Error " + e);
            }
        }
    }
    
    @FXML
    private void buscarMedicamentos(ActionEvent event) {
        String fechaVencimiento=null;
        String codigo=null;
        Date fechaActual = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        String fechaSistema=formateador.format(fechaActual);
   
        tablaMedicamentos.getItems().clear();
        try {
            conexion = DBConnection.connect();
             String sql = "SELECT med_codmedicamentos, "
                    + " med_nombre, "
                    + " med_descripcion, "
                    + " med_vencimiento, "
                    + " med_lote, "
                    + " med_precio, "
                    + " med_stock, "
                    + " med_observacion,"
                    + " med_tipo "
                    + " FROM medicamentos "
                    + " WHERE upper(med_nombre) LIKE '%"+tfBuscarMedicamentos.getText().toUpperCase()+"%'"
                    + " ORDER BY med_nombre DESC";
            
            ResultSet rs = conexion.createStatement().executeQuery(sql);
           
            while(rs.next()){
                
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i = 1 ; i <= rs.getMetaData().getColumnCount(); i++){
                     codigo=(rs.getString("med_lote")); 
                     
                     
                  fechaVencimiento=(rs.getString("med_vencimiento")); 
                  
                  if (fechaVencimiento.equals(fechaSistema)){
                      
                    String sql1 = "UPDATE medicamentos "
                    + " SET med_observacion  = ?"
                    + " WHERE med_lote = '"+ codigo +"'";
            PreparedStatement estado = conexion.prepareStatement(sql1);
            estado.setString(1, "VENCIDO");
            
            int n = estado.executeUpdate();
            
                  }
                   
                    row.add(rs.getString(i));
                }
                medicamentos.addAll(row);
            }
            tablaMedicamentos.setItems(medicamentos);
            rs.close();
            
        } catch (SQLException e) {
            System.out.println("Error " + e.getMessage());
        }
        
    }
    
    @FXML
    private void nuevoMedicamentos(ActionEvent event) {
        
        btAddMedicamentos.setDisable(false);
        btEliminarMedicamentos.setDisable(true);
        btModificarMedicamentos.setDisable(true);
        btAddMedicamentos.setStyle("-fx-background-color:#66CCCC");
        btEliminarMedicamentos.setStyle("-fx-background-color:grey");
        btModificarMedicamentos.setStyle("-fx-background-color:grey");
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
    private void irMantenimientoServicio(ActionEvent  event) {
       
       controlador.setScreen(ScreensFramework.mantenimientoServicioID);
    }
    
     @FXML
    private void irMantenimientoConsulta(ActionEvent  event) {
       
       controlador.setScreen(ScreensFramework.mantenimientoConsultaID);
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
