/*
 * Controlador de la ventana Mascotas
 * @author Rocio Carmona
 */
package screensframework;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.dateTime;
import static java.awt.SystemColor.text;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import screensframework.DBConnect.DBConnection;

public class ConsultasController implements Initializable, ControlledScreen {
    
    

    ScreensController controlador;
    private ControlesBasicos controlesBasicos = new ControlesBasicos();
    @FXML
    private Button btAddMascota;
    @FXML
    private Button btModificarMascota;
    @FXML
    private Button btEliminarMascota;
    @FXML
    private Button btNuevaMascota;
    @FXML
    private Label lbClienteNombre;
    @FXML
    private Label lbClieNombre;
    @FXML
    private Label lbCodMascota;
    @FXML
    private Label valor1;
    @FXML
    private TextField tfBuscarCliente;
    @FXML
    private TextField tfBuscarMascota;
    @FXML
    private TextField tfNombreMascota;
    @FXML
    private TextField tfSexoMascota;
    @FXML
    private TextField tfRazaMascota;
    @FXML
    private TextField tfFechaNacMascota;
    @FXML
    private TextField tfEdadMascota;
    @FXML
    private TextField tfPrueba;
    @FXML
    private TextField Action1;

    @FXML
    private Label lbCodCliente;
    @FXML
    private TableView tablaClientes;
    @FXML
    private TableView tablaServicios;
    @FXML
    private TableView tablaConsultas;
    @FXML
    private TableColumn col;
    @FXML
    private ComboBox combobox;
    private Connection conexion;
    @FXML //  fx:id="fruitCombo"
    private ComboBox<String> combomas; // Value injected by FXMLLoader

    @FXML
    private Label lbCodClie;
    
    @FXML private TextField tfDescripcionServicio;
    @FXML private TextField tfPrecioServicio;
    @FXML private TextField tfBuscarServicio;
    @FXML private Label lbCodigoServicio;

    ObservableList<ObservableList> clientes;
    ObservableList<ObservableList> servicios;
    ObservableList<ObservableList> olConsultas;

    @FXML
    private Pane root;

    private Popup popup;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //  this.cargarClientesTabla();
        //  this.cargarMascotasTabla();
        //this.cargarDatosTabla("0");
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

    public void cargarServiciosTabla() {
        servicios = FXCollections.observableArrayList();

        try {
            conexion = DBConnection.connect();
            /* String sql = "SELECT m.*,c.cli_nombre as Propietario FROM mascotas m "
             + "INNER JOIN clientes c ON c.cli_codcliente = m.mas_codpropietario "
             + "ORDER BY m.mas_nombre";*/
            /*   */
            String sql = "SELECT ser_codservicio, ser_descripcion, ser_precio FROM servicios "
                    + "UNION "
                    + " SELECT med_codmedicamentos as ser_codservicio, med_nombre as ser_descripcion, "
                    + " med_precio as ser_precio "
                    + " FROM medicamentos";
          //  String sql = "SELECT * FROM servicios order by ser_descripcion";
            //ResultSet
            ResultSet rs = conexion.createStatement().executeQuery(sql);
            // Títulos de las columnas
            String[] titulos = {
                "Cod",
                "Descripcion",
                "precio"
            };
            /**
             * ********************************
             * TABLE COLUMN ADDED DYNAMICALLY * ********************************
             */

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                col = new TableColumn(titulos[i]);
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> parametro) {
                        return new SimpleStringProperty((String) parametro.getValue().get(j));
                    }
                });
                tablaServicios.getColumns().addAll(col);
                
                // Asignamos un tamaño a ls columnnas
                col.setMinWidth(100);
                //System.out.println("Column ["+i+"] ");
                // Centrar los datos de la tabla
                col.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
                    @Override
                    public TableCell<String, String> call(TableColumn<String, String> p) {
                        TableCell cell = new TableCell() {
                            @Override
                            protected void updateItem(Object t, boolean bln) {
                                if (t != null) {
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
            /**
             * ******************************
             * Cargamos de la base de datos * ******************************
             */
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                //System.out.println("Row [1] added "+row );
                servicios.addAll(row);
            }
            //FINALLY ADDED TO TableView
            tablaServicios.setItems(servicios);
            servicios=null;
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error " + e);
        }
    }
    public void cargarConsultaTabla(String valor) {
        
        try {            
            conexion = DBConnection.connect();
            String sql = "SELECT * "
                    + " FROM servicios "
                    + " WHERE ser_codservicio = "+valor;
            ResultSet rs = conexion.createStatement().executeQuery(sql);
     /*
  con_codigo serial NOT NULL,
  con_cliente character varying(100) NOT NULL,
  con_mascota character varying(100) NOT NULL,
  con_fecha character varying(100) NOT NULL,
  con_diagnostico character varying(100) NOT NULL,
  con_codservicio integer,
  con_precio integer
            */       
            while (rs.next()) {
                lbCodigoServicio.setText(rs.getString("ser_codservicio"));
                tfDescripcionServicio.setText(rs.getString("ser_descripcion"));
                tfPrecioServicio.setText(rs.getString("ser_precio"));  
            }
            rs.close();
            if(tfDescripcionServicio.getText()!=""){
                String sql1 = "INSERT INTO consultas (con_cliente,con_mascota,con_diagnostico,con_codservicio,con_precio,con_fecha) "
                    + " VALUES (?,?,?,?,?,?)";
                PreparedStatement estado = conexion.prepareStatement(sql1);
                
                // 1) create a java calendar instance
                Calendar calendar = Calendar.getInstance();

                // 2) get a java.util.Date from the calendar instance.
                //    this date will represent the current instant, or "now".
                java.util.Date now = calendar.getTime();

                // 3) a java current time (now) instance
                java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
                
                estado.setString(1,(lbCodClie.getText()));
                estado.setString(2, combomas.getValue());
                estado.setString(3, "hola");
                estado.setInt(4, Integer.parseInt(lbCodigoServicio.getText()));
                estado.setInt(5, Integer.parseInt(tfPrecioServicio.getText()));
                estado.setTimestamp(6, currentTimestamp);
           
                int n  = estado.executeUpdate();
           
                if (n > 0) {
                    //tablaServicios.getColumns().clear();
                    //tablaServicios.getItems().clear();
                    cargarDatosTabla(valor);
                }
                tfDescripcionServicio.setText("");
                tfPrecioServicio.setText("");
                lbCodigoServicio.setText("");
                estado.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error "+ex);
        }
        
    }
    public void cargarDatosTabla(String id) {
        olConsultas = FXCollections.observableArrayList();
                tablaConsultas.getColumns().clear();
                tablaConsultas.getItems().clear();
        try {
            conexion = DBConnection.connect();
            String sql = "select * from consultas"
              + " WHERE con_codservicio = "+Integer.parseInt(id);
            
            //ResultSet
            ResultSet rs = conexion.createStatement().executeQuery(sql);
            String[] titulos = {"Cod","con_cliente","con_mascota","con_diagnostico","con_codservicio","con_precio","con_fecha"};
           // /**
             //* ********************************
             //* TABLE COLUMN ADDED DYNAMICALLY * ********************************
             //*

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                col = new TableColumn(titulos[i]);
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> parametro) {
                       
                        return new SimpleStringProperty((String) parametro.getValue().get(j));
                    }
                });
                tablaConsultas.getColumns().addAll(col);
                
                // Asignamos un tamaño a ls columnnas
                col.setMinWidth(100);
                //System.out.println("Column ["+i+"] ");
                // Centrar los datos de la tabla
                col.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
                    @Override
                    public TableCell<String, String> call(TableColumn<String, String> p) {
                        TableCell cell = new TableCell() {
                            @Override
                            protected void updateItem(Object t, boolean bln) {
                                if (t != null) {
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
           // 
            // * ******************************
            // * Cargamos de la base de datos * ******************************
            // 
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                //System.out.println("Row [1] added "+row );
                olConsultas.addAll(row);
            }
            //FINALLY ADDED TO TableView
            tablaConsultas.setItems(olConsultas);
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error " + e);
        }
    }

    public void cargarMascotasText(String valor) {

        try {

            conexion = DBConnection.connect();
            String sql = "SELECT * "
                    + " FROM mascotas "
                    + " WHERE mas_codmascota = " + Integer.parseInt(valor);
            ResultSet rs = conexion.createStatement().executeQuery(sql);

            while (rs.next()) {
                lbCodMascota.setText(rs.getString("mas_codmascota"));
                tfNombreMascota.setText(rs.getString("mas_nombre"));
                tfSexoMascota.setText(rs.getString("mas_sexo"));
                tfRazaMascota.setText(rs.getString("mas_raza"));
                tfFechaNacMascota.setText(rs.getString("mas_fechanac"));
                tfEdadMascota.setText(rs.getString("mas_edad"));
                //  tfNombreM.setText(rs.getString("mas_nombre"));
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error " + ex);
        }
        combomas.getSelectionModel();

        ComboBox emailComboBox = new ComboBox();
        emailComboBox.getItems().addAll("A", "B", "C", "D", "E");
        emailComboBox.setValue("A");
        System.out.println(emailComboBox.getValue());

    }

    public void cargarClientesText(String valor) {

        try {
            DefaultComboBoxModel modeloCombo = new DefaultComboBoxModel();
            conexion = DBConnection.connect();
            String sql = "SELECT * "
                    + " FROM clientes "
                    + " WHERE cli_codcliente = " + valor;
            ResultSet rs = conexion.createStatement().executeQuery(sql);
            /*
             cli_cedula, "
             + " cli_nombre, "
             + " cli_direccion, "
             + " cli_telefono "
             + " FROM clientes
             */
            while (rs.next()) {
                modeloCombo.addElement(rs.getString("cli_codcliente"));
                lbCodClie.setText(rs.getString("cli_codcliente"));;
                lbClienteNombre.setText(rs.getString("cli_nombre"));

                // valor1.setText(rs.getString("cli_nombre"));
                //combobox.getItems ().addAll (rs);
            }
            rs.close();

        } catch (SQLException ex) {
            System.out.println("Error " + ex);
        }

    }

    @FXML
    private void buscarCliente(ActionEvent event) {
        // tablaClientes.getItems().clear();
        String nombre = null;
        try {
            conexion = DBConnection.connect();
            String sql = "SELECT cli_codcliente, "
                    + " cli_nombre "
                    + " FROM clientes "
                    + " WHERE upper(cli_nombre) LIKE '%" + tfBuscarCliente.getText().toUpperCase() + "%'"
                    + " ORDER BY cli_nombre DESC";
            ResultSet rs = conexion.createStatement().executeQuery(sql);
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                    nombre = rs.getString("cli_codcliente");
                    System.out.print(nombre);
                    //  combomas.getItems().add(rs.getString("cli_nombre")); 
                }
                String sql1 = "SELECT mas_nombre FROM mascotas m "
                        + "INNER JOIN clientes c ON c.cli_codcliente = m.mas_codpropietario "
                        + "WHERE m.mas_codpropietario='" + nombre + "'";

                ResultSet rs1 = conexion.createStatement().executeQuery(sql1);
                combomas.getItems().clear();
                while (rs1.next()) {
                    {
                        // row.add(rs1.getString(i));
                        combomas.getItems().add(rs1.getString("mas_nombre"));
                    }
                }
                 Object selectedItem = combomas.selectionModelProperty();
                 
                // buscarMascotas(nombre);
                //   lbCodCliente.setText(rs.getString("cli_codcliente"));
                //  lbClienteNombre.setText(rs.getString("cli_nombre"));

              lbCodClie.setText(rs.getString("cli_codcliente"));
                lbClieNombre.setText(rs.getString("cli_nombre"));

               // tfBuscarCliente.setText(rs.getString("cli_cedula"));
              //  Action1.setText(rs.getString("cli_nombre"));

                //  final ComboBox comboBox = new ComboBox();
                System.out.println("Apagado");

               // clientes.addAll(row);
            }
            //  tablaClientes.setItems(clientes);

            rs.close();

        } catch (SQLException e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    @FXML
    private void buscarMascotas(String cliente) {

        try {
            DefaultComboBoxModel modeloCombo = new DefaultComboBoxModel();
            conexion = DBConnection.connect();
            String nombre = null;
            /* String sql = "SELECT mas_nombre FROM mascotas m "
             + "nom_mascota"
             + "INNER JOIN clientes c ON c.cli_codcliente = m.mas_codpropietario "
             + "WHERE c.cli_nombre='" + cliente + "'"
             + "ORDER BY m.mas_nombre";*/
            String sql = "select ser_codservicio, ser_descripcion, ser_precio from servicios"
                    + "UNION"
                    + " select med_codmedicamentos as ser_codservicio, med_nombre as ser_descripcion, med_precio as ser_precio from medicamentos";

            ResultSet rs = conexion.createStatement().executeQuery(sql);

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                    combomas.getItems().add(rs.getString("mas_nombre"));
                }
            }
            cargarServiciosTabla();
        } catch (SQLException ex) {
            System.out.println("Error " + ex);
        }
    }

    @FXML
    private void buscarServicios(ActionEvent event) {
        //  tablaClientes.getItems().clear();
        try {
            conexion = DBConnection.connect();
            String sql = "SELECT cli_codcliente, "
                    + " cli_nombre "
                    + " FROM clientes "
                    + " WHERE upper(cli_nombre) LIKE '%" + tfBuscarCliente.getText().toUpperCase() + "%'"
                    + " ORDER BY cli_nombre DESC";
            ResultSet rs = conexion.createStatement().executeQuery(sql);
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                clientes.addAll(row);
            }
            // tablaClientes.setItems(clientes);
            rs.close();

        } catch (SQLException e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    

    @FXML
    private void buscarMascota(ActionEvent event) {

        Stage stage = (Stage) root.getScene().getWindow();

        if (popup == null) {
            popup = new Popup();

            VBox vbox = new VBox();
            //box.getChildren().add(new Label("In popup..."));
            int ancho = 400;
            int alto = 200;
            vbox.setPrefSize(ancho, alto);
            vbox.setAlignment(Pos.BOTTOM_CENTER);
            vbox.setStyle("-fx-background-color: gray;");
            
            /**
             * Configuracion de los botones
             */
           
            Button buttonOk = new Button("Aceptar");
            Button buttonCancel = new Button("Cancelar");

            buttonOk.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    System.out.println(t);
                    //name.setText(name2.getText());
               // cargarConsultaTabla();

                }
              
            });

            buttonCancel.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    popup.hide();
                   //   cargarConsultaTabla();
                    
                }
              
            });

            buttonOk.setMaxWidth(Integer.MAX_VALUE);
            buttonCancel.setMaxWidth(Integer.MAX_VALUE);
            
           

            /**
             * Configuracion del contenedor de los botones
             */
            HBox hbox = HBoxBuilder.create()
                .spacing(20.0) //In case you are using HBoxBuilder
                .padding(new Insets(5, 5, 5, 5))
                .children(buttonOk, buttonCancel)
                .build();
            
            hbox.setAlignment(Pos.CENTER);
            
            cargarServiciosTabla();
            
            
            Label label = new Label("Seleccione el servicio");
            label.setFont(new Font("Cambria", 32));
            label.setTextFill(Color.WHITE);
            vbox.getChildren().add(label); 
            
            
            vbox.getChildren().add(tablaServicios);
            vbox.getChildren().add(hbox);

            popup.getContent().add(vbox);
            
            //popup.setAutoFix(false);
            //popup.setHideOnEscape(true);
           
            //popup.setAutoFix(true);
            //popup.setX(100);
            //popup.setY(100);
        }

        if (popup.isShowing()) {
            popup.hide();
        } else {
            //hello.setText("hello.." + name.getText());
            popup.show(stage);
        }
    }

    @FXML
    private void getServicioSeleccionado(MouseEvent event) {
        tablaServicios.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                popup.hide();
                if (tablaServicios != null) {
    
                    String valor = tablaServicios.getSelectionModel().getSelectedItems().get(0).toString();
                    
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
                       cargarConsultaTabla(cincoDigitos);
                    } else {
                        if (m4.find()) {
                            cargarConsultaTabla(cuatroDigitos);
                        } else {
                            if (m3.find()) {
                                cargarConsultaTabla(tresDigitos);
                            } else {
                                if (m2.find()) {
                                    cargarConsultaTabla(dosDigitos);
                                } else {
                                    cargarConsultaTabla(unDigitos);
                                }
                             }
                        }
                    }
                }
            }
        });
    }

    @FXML
    private void irMantenimientoCliente(ActionEvent event) {

        controlador.setScreen(ScreensFramework.mantenimientoClienteID);
    }

    @FXML
    private void irMantenimientoMascotas(ActionEvent event) {

        controlador.setScreen(ScreensFramework.mantenimientoMascotaID);
    }

    @FXML
    private void irMantenimientoServicio(ActionEvent event) {

        controlador.setScreen(ScreensFramework.mantenimientoServicioID);
    }

    @FXML
    private void irMantenimientoConsulta(ActionEvent event) {

        controlador.setScreen(ScreensFramework.mantenimientoConsultaID);
    }

    @FXML
    private void irMantenimientoMedicamentos(ActionEvent event) {

        controlador.setScreen(ScreensFramework.mantenimientoMedicamentosID);
    }

    @FXML
    private void irMantenimientoVacunacion(ActionEvent event) {

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
