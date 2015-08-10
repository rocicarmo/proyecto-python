/*
 * Controlador de la ventana Mascotas
 * @author Rocio Carmona
 */
package screensframework;

//import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.dateTime;
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

public class HistorialMascotaController implements Initializable, ControlledScreen {

    ScreensController controlador;
    private ControlesBasicos controlesBasicos = new ControlesBasicos();
    @FXML
    private Label lbClienteNombre;
    @FXML
    private Label lbClieNombre;
    @FXML
    private Label lbCodMascota;

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

    @FXML
    private TextField tfDescripcionServicio;
    @FXML
    private TextField tfPrecioServicio;
    @FXML
    private TextField tfBuscarServicio;
    @FXML
    private Label lbCodigoServicio;

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
        //    this.cargarDatosTabla("0");
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

    public void cargarConsultaTabla(String valor) {

        try {
            conexion = DBConnection.connect();
            String sql = "SELECT * "
                    + " FROM servicios "
                    + " WHERE ser_codservicio = " + valor;
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
            if (tfDescripcionServicio.getText() != "") {
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

                estado.setString(1, (lbCodClie.getText()));
                estado.setString(2, combomas.getValue());
                estado.setString(3, "hola");
                estado.setInt(4, Integer.parseInt(lbCodigoServicio.getText()));
                estado.setInt(5, Integer.parseInt(tfPrecioServicio.getText()));
                estado.setTimestamp(6, currentTimestamp);

                int n = estado.executeUpdate();

                if (n > 0) {
                    //tablaServicios.getColumns().clear();
                    //tablaServicios.getItems().clear();
                    cargarDatosTabla(combomas.getValue());
                }
                tfDescripcionServicio.setText("");
                tfPrecioServicio.setText("");
                lbCodigoServicio.setText("");
                estado.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error " + ex);
        }

    }

    public void cargarDatosTabla(String id) {
        olConsultas = FXCollections.observableArrayList();
        int contador = 0;
        try {
            conexion = DBConnection.connect();
            /* String sql = "SELECT con_codigo, con_fecha, con_codservicio, con_diagnostico "
             + "FROM consultas "
             + "WHERE con_mascota = '" + id +"'";*/

            String sql = "SELECT c.con_codigo, c.con_fecha,  s.ser_descripcion , c.con_diagnostico  "
                    + " FROM servicios s "
                    + " inner join consultas c on s.ser_codservicio=c.con_codservicio"
                    + " WHERE con_mascota = '" + id + "'";

            ResultSet rs = conexion.createStatement().executeQuery(sql);

         //  boolean existeUsuario =  rs.next();
            // Títulos de las columnas
            String[] titulos = {
                "Cod",
                "Fecha",
                "Servicio",
                "Diagnostico"};

           // tablaConsultas.setItems(olConsultas);
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
                col.setMinWidth(200);
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
                    contador++;
                }
                //System.out.println("Row [1] added "+row );
                olConsultas.addAll(row);
            }
            //FINALLY ADDED TO TableView
            tablaConsultas.setItems(olConsultas);

            rs.close();

            if (contador == 0) {
                JOptionPane.showMessageDialog(null, id + " NO POSEE HISTORIAL");

            }
        } catch (SQLException e) {
            System.out.println("Error " + e);
        }
    }

    public void cargarMascotasText(String valor) {

        try {

            conexion = DBConnection.connect();
            String sql = "SELECT * "
                    + " FROM mascotas "
                    + " WHERE mas_codmascota = " + valor;
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
                lbCodCliente.setText(rs.getString("cli_codcliente"));;
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
    private void comboMascota(ActionEvent event) throws SQLException {
        tablaConsultas.getColumns().clear();
        String valorSeleccionado = null;
        valorSeleccionado = combomas.getValue();
        if (valorSeleccionado != null) {
            cargarDatosTabla(valorSeleccionado);

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
                    + " WHERE cli_cedula ='" + tfBuscarCliente.getText() + "'"
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

                // buscarMascotas(nombre);
                //   lbCodCliente.setText(rs.getString("cli_codcliente"));
                //  lbClienteNombre.setText(rs.getString("cli_nombre"));
                // lbCodClie.setText(rs.getString("cli_codcliente"));
                lbClieNombre.setText(rs.getString("cli_nombre"));

               // tfBuscarCliente.setText(rs.getString("cli_cedula"));
                //  Action1.setText(rs.getString("cli_nombre"));
                //  final ComboBox comboBox = new ComboBox();
                System.out.println("Apagado");

                //  clientes.addAll(row);
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
           String sql = "SELECT mas_nombre FROM mascotas m "
             + "nom_mascota"
             + "INNER JOIN clientes c ON c.cli_codcliente = m.mas_codpropietario "
             + "WHERE c.cli_nombre='" + cliente + "'"
             + "ORDER BY m.mas_nombre";
          /*  String sql = "select ser_codservicio, ser_descripcion, ser_precio from servicios"
                    + "UNION"
                    + " select med_codmedicamentos as ser_codservicio, med_nombre as ser_descripcion, med_precio as ser_precio from medicamentos";
*/
            ResultSet rs = conexion.createStatement().executeQuery(sql);

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                    combomas.getItems().add(rs.getString("mas_nombre"));
                }
            }
            //   cargarServiciosTabla();
        } catch (SQLException ex) {
            System.out.println("Error " + ex);
        }
    }


    private void siExiste(ResultSet consulta) throws SQLException {

        int contador = 0;
        //  boolean existeUsuario =  rs.next();
        while (consulta.next()) {

            contador++;
        }

        if (contador == 0) {
            JOptionPane.showMessageDialog(null, "No posee historial");

        }
    }

    @FXML
    private void irMantenimientoCliente(ActionEvent event) {

        controlador.setScreen(ScreensFramework.mantenimientoClienteID);
    }

    @FXML
    private void irMantenimientoHistorial(ActionEvent event) {

        controlador.setScreen(ScreensFramework.mantenimientoHistorialID);
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
