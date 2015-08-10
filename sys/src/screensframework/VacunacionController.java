
/*
 * Controlador de la ventana Vacunación
 * @author Rocio Carmona
 */
package screensframework;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static java.sql.Types.NULL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Callback;
import static javax.print.attribute.Size2DSyntax.MM;
import javax.swing.JOptionPane;
import screensframework.DBConnect.DBConnection;
import screensframework.Funcion;

public class VacunacionController implements Initializable, ControlledScreen {

    ScreensController controlador;
    private ControlesBasicos controlesBasicos = new ControlesBasicos();
    @FXML
    private Button btAddVacunacion;
    @FXML
    private TableView tablaServicios;
    @FXML
    private TableView tablaMedicamentos;
    @FXML
    private Button btModificarVacunacion;
    @FXML
    private Button btEliminarVacunacion;
    @FXML
    private Button btNuevoVacunacion;
    @FXML
    private TextField tfBuscarCliente;
    @FXML
    private TextField tfNombreVacunacion;
    @FXML
    private TextField tfFechaVacunacion;
    @FXML
    private TextField tfFechaProximaVacunacion;
    @FXML
    private TextField tfBuscarVacunacion;
    @FXML
    private TextField tfBuscarMedicamentos;
    @FXML
    private TextField tfBuscarMascota;
    @FXML
    private TextField tfBuscarTipo;

    @FXML
    private Label lbCodigoVacunacion;

    @FXML
    private Pane root;
    
     @FXML
    private GridPane root1;
     
     @FXML
private GridPane gridPane;

private DatePicker birthdayDatePicker;

    private Popup popup;

    @FXML
    private ComboBox combobox;
    @FXML //  fx:id="fruitCombo"
    private ComboBox<String> combomas; // Value injected by FXMLLoader
    @FXML
    private Label lbCodiCliente;
    @FXML
    private Label lbNomiCliente;

    @FXML
    private TableView tablaVacunacion;
    @FXML
    private TableView tablaServicio;
    @FXML
    private TableColumn col;
    private Connection conexion;
    int codMascota = 0;
    ObservableList<ObservableList> Vacunacion;
    ObservableList<ObservableList> servicios;
    ObservableList<ObservableList> medicamentos;
    String nombre = null;
    String masc = null;
    int ibVacunacion = 0;
    Funcion funcion = new Funcion();

    @FXML
    private void irMantenimientoVacunacion(ActionEvent event) {

        controlador.setScreen(ScreensFramework.mantenimientoVacunacionID);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.cargarDatosTabla();
        this.cargarDatosTablaMedicamentos();

        btEliminarVacunacion.setDisable(true);
        btModificarVacunacion.setDisable(true);
        btEliminarVacunacion.setStyle("-fx-background-color:grey");
        btModificarVacunacion.setStyle("-fx-background-color:grey");
        ObservableList<ObservableList> medicamentos;
        ObservableList<Object> categoriaID = FXCollections.observableArrayList();
        ObservableList<Object> categoriaNomnre = FXCollections.observableArrayList();
        ObservableList<Object> subCategoria = FXCollections.observableArrayList();
        ObservableList<Object> marcas = FXCollections.observableArrayList();
        
 // Initialize the DatePicker for birthday
  birthdayDatePicker = new DatePicker(Locale.ENGLISH);
  birthdayDatePicker.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
  birthdayDatePicker.getCalendarView().todayButtonTextProperty().set("Today");
  birthdayDatePicker.getCalendarView().setShowWeeks(false);
  birthdayDatePicker.getStylesheets().add("ch/makery/address/view/DatePicker.css");

  // Add DatePicker to grid
  //gridPane.add(birthdayDatePicker, 1, 5); 

        try {
            conexion = DBConnection.connect();

        } catch (SQLException e) {
            System.out.println("Error " + e);
        }
    }

    public void buscarVacunaciones(ActionEvent event) throws SQLException {
        try {
            conexion = DBConnection.connect();
            String sql = "select c.cli_telefono, c.cli_nombre, v.vac_fechavacprox, v.vac_mascota "
                    + " from clientes c inner join mascotas m on c.cli_codcliente = m.mas_codpropietario "
                    + " inner join vacunacion v on m.mas_codmascotas=v.vac_codmascota ";
            ResultSet rs = conexion.createStatement().executeQuery(sql);
            while (rs.next()) {
                long diferencia;
                ObservableList<String> row = FXCollections.observableArrayList();

                long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000;
                String fechaP = rs.getString("vac_fechavacprox");
                String fechaF[] = fechaP.split("/");
                String fechaFormato = fechaF[2] + "/" + fechaF[1] + "/" + fechaF[0];
                SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
                String strFecha = fechaP;
                Date fecha = null;

                try {

                    fecha = formatoDelTexto.parse(strFecha);

                } catch (ParseException ex) {

                    ex.printStackTrace();

                }
                long fechaLong = fecha.getTime();
                Date fechaActual = new Date();
                SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
                String fechaSistema = formateador.format(fechaActual);
                Date fechaA = null;

                try {

                    fechaA = formatoDelTexto.parse(fechaSistema);

                } catch (ParseException ex) {

                    ex.printStackTrace();

                }

                diferencia = (fecha.getTime() - fechaA.getTime()) / 86400000;
                if (diferencia == 1) {
                    funcion.envioSms(rs.getString("cli_nombre") + " Le recordamos que mañana es la fecha de vacunacion de " + rs.getString("vac_mascota"), "vacunacion", rs.getString("cli_telefono"));
                }

            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error " + e);
        }
    }

    public void buscarCliente(ActionEvent event) {
        // tablaClientes.getItems().clear();

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
                String sql1 = "SELECT mas_codmascotas, mas_nombre FROM mascotas m "
                        + "INNER JOIN clientes c ON c.cli_codcliente = m.mas_codpropietario "
                        + "WHERE m.mas_codpropietario='" + nombre + "'";

                ResultSet rs1 = conexion.createStatement().executeQuery(sql1);

                combomas.getItems().clear();
                while (rs1.next()) {
                    {
                        // row.add(rs1.getString(i));
                        combomas.getItems().add(rs1.getString("mas_nombre"));
                        String mascota = rs1.getString("mas_nombre");
                        codMascota = rs1.getInt("mas_codmascotas");
                    }
                }
                masc = combomas.getValue();

                combomas.selectionModelProperty();
                // buscarMascotas(nombre);
                //   lbCodCliente.setText(rs.getString("cli_codcliente"));
                //  lbClienteNombre.setText(rs.getString("cli_nombre"));

                //lbCodiCliente.setText(rs.getString("cli_codcliente"));
                lbNomiCliente.setText(rs.getString("cli_nombre"));

                //    tfBuscarCliente.setText(rs.getString("cli_cedula"));
                //  Action1.setText(rs.getString("cli_nombre"));
                //  final ComboBox comboBox = new ComboBox();
                System.out.println("Apagado");

                //   clientes.addAll(row);
            }
            //  tablaClientes.setItems(clientes);

            rs.close();

        } catch (SQLException e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    private String compararFechasConDate(String fecha1, String fechaActual) {
        System.out.println("Parametro String Fecha 1 = " + fecha1 + "\n"
                + "Parametro String fechaActual = " + fechaActual + "\n");
        String resultado = "";
        try {
            /**
             * Obtenemos las fechas enviadas en el formato a comparar
             */
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaDate1 = formateador.parse(fecha1);
            Date fechaDate2 = formateador.parse(fechaActual);

            System.out.println("Parametro Date Fecha 1 = " + fechaDate1 + "\n"
                    + "Parametro Date fechaActual = " + fechaDate2 + "\n");

            if (fechaDate1.before(fechaDate2)) {
                resultado = "La Fecha 1 es menor ";
            } else {
                if (fechaDate2.before(fechaDate1)) {
                    resultado = "La Fecha 1 es Mayor ";
                } else {
                    resultado = "Las Fechas Son iguales ";
                }
            }
        } catch (ParseException e) {
            System.out.println("Se Produjo un Error!!!  " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public void setScreenParent(ScreensController pantallaPadre) {
        controlador = pantallaPadre;
    }

    public void cargarDatosTabla() {
        Vacunacion = FXCollections.observableArrayList();

        try {
            conexion = DBConnection.connect();

            String sql = "SELECT vac_codvacmascota, "
                    + " vac_nombre, "
                    + " vac_fechavac, "
                    + " vac_fechavacprox, "
                    + " vac_mascota"
                    + " FROM vacunacion "
                    + " ORDER BY vac_nombre DESC";
            //ResultSet
            ResultSet rs = conexion.createStatement().executeQuery(sql);
            // Títulos de las columnas
            String[] titulos = {
                "Cod",
                "Nombre",
                "Fecha",
                "Fecha Proxima",
                "Mascota"};

            /**
             * ********************************
             * Columna cargada dinamicamente * ********************************
             */
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                col = new TableColumn(titulos[i]);
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> parametro) {
                        return new SimpleStringProperty((String) parametro.getValue().get(j));
                    }
                });
                tablaVacunacion.getColumns().addAll(col);
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
                Vacunacion.addAll(row);
            }
            //FINALLY ADDED TO TableView
            tablaVacunacion.setItems(Vacunacion);
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error " + e);
        }
    }

    public void cargarVacunacionText(String valor) {

        try {

            conexion = DBConnection.connect();
            String sql = "SELECT * "
                    + " FROM vacunacion "
                    + " WHERE vac_codvacmascota = " + valor;
            ResultSet rs = conexion.createStatement().executeQuery(sql);
            while (rs.next()) {
                ibVacunacion = rs.getInt("vac_codvacmascota");
                //   lbCodigoVacunacion.setText(rs.getString("vac_codvacmascota"));
                tfNombreVacunacion.setText(rs.getString("vac_nombre"));
                tfFechaVacunacion.setText(rs.getString("vac_fechavac"));
                tfFechaProximaVacunacion.setText(rs.getString("vac_fechavacprox"));
                combomas.setValue(rs.getString("vac_mascota"));

            }
            String sql1 = "SELECT c.cli_nombre "
                    + " FROM clientes "
                    + " WHERE vac_codvacmascota = " + valor;

            ResultSet rs1 = conexion.createStatement().executeQuery(sql1);
            while (rs1.next()) {

            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error " + ex);
        }

    }

    @FXML
    private void getVacunacionSeleccionado(MouseEvent event) {
        tablaVacunacion.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (tablaVacunacion != null) {

                    btAddVacunacion.setDisable(true);
                    btEliminarVacunacion.setDisable(false);
                    btModificarVacunacion.setDisable(false);
                    btAddVacunacion.setStyle("-fx-background-color:grey");
                    btEliminarVacunacion.setStyle("-fx-background-color:#66CCCC");
                    btModificarVacunacion.setStyle("-fx-background-color:#66CCCC");

                    String valor = tablaVacunacion.getSelectionModel().getSelectedItems().get(0).toString();

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
                        cargarVacunacionText(cincoDigitos);

                    } else {
                        if (m4.find()) {
                            cargarVacunacionText(cuatroDigitos);

                        } else {
                            if (m3.find()) {
                                cargarVacunacionText(tresDigitos);

                            } else {
                                if (m2.find()) {
                                    cargarVacunacionText(dosDigitos);

                                } else {
                                    cargarVacunacionText(unDigitos);

                                }
                            }
                        }
                    }
                }
            }
        });
    }

    @FXML
    private void addVacunacion(ActionEvent event) {

        boolean fecha = isFechaValida(tfFechaVacunacion.getText());
        boolean fechaProx = isFechaValida(tfFechaProximaVacunacion.getText());

        try {
            if (tfNombreVacunacion.getText().matches("[0-9]*")) {

                JOptionPane.showMessageDialog(null, "No puede ser numerico");
            }
            if (tfNombreVacunacion.getText().trim().length() == 0) {

                JOptionPane.showMessageDialog(null, "Nombre no puede ser nulo");

            } else if (tfFechaVacunacion.getText().trim().length() == 0) {

                JOptionPane.showMessageDialog(null, "Fecha de Vacunación no puede ser nulo");

            } else if (tfFechaProximaVacunacion.getText().trim().length() == 0) {

                JOptionPane.showMessageDialog(null, "Fecha de Próxima Vacunación no puede ser nulo");
            } else if (combomas.getValue().trim().length() == 0) {
                JOptionPane.showMessageDialog(null, "Nombre Mascota no puede ser nulo");
            } else if (fecha != true) {
                JOptionPane.showMessageDialog(null, "Fecha invalida");
            } else if (fechaProx != true) {
                JOptionPane.showMessageDialog(null, "Fecha invalida");
            } else {
                conexion = DBConnection.connect();
                String sql2 = "SELECT mas_codmascotas, mas_nombre FROM mascotas m "
                        + "WHERE m.mas_codpropietario='" + nombre + "'";

                ResultSet rs2 = conexion.createStatement().executeQuery(sql2);
                while (rs2.next()) {
                    {
                        // row.add(rs1.getString(i));
                        // combomas.getItems().add(rs1.getString("mas_nombre"));
                        // String mascota = rs1.getString("mas_nombre");

                        if (rs2.getString("mas_nombre").equals(masc)) {
                            codMascota = rs2.getInt("mas_codmascotas");
                        }
                    }
                }
                String sql = "INSERT INTO vacunacion "
                        + " (vac_nombre, vac_fechavac,vac_fechavacprox, vac_mascota, vac_codmascota) "
                        + " VALUES (?, ?, ?, ?, ?)";
                PreparedStatement estado = conexion.prepareStatement(sql);
                estado.setString(1, (tfNombreVacunacion.getText()));
                estado.setString(2, tfFechaVacunacion.getText());
                estado.setString(3, tfFechaProximaVacunacion.getText());
                estado.setString(4, (combomas.getValue()));
                estado.setInt(5, codMascota);
                // estado.setString(4, mas_nombre.getText());

                int n = estado.executeUpdate();

                if (n > 0) {
                    tablaVacunacion.getColumns().clear();
                    tablaVacunacion.getItems().clear();
                    cargarDatosTabla();
                }
                combomas.getItems().clear();
                tfBuscarCliente.clear();
                tfNombreVacunacion.clear();
                tfFechaVacunacion.clear();
                tfFechaProximaVacunacion.clear();

                estado.close();
            }
        } catch (SQLException e) {
            System.out.println("Error " + e);
        }

    }

    @FXML
    private void modificarVacunacion(ActionEvent event) {

        try {
            conexion = DBConnection.connect();
            tfNombreVacunacion.getText().toUpperCase();

            String sql = "UPDATE vacunacion "
                    + " SET vac_nombre = ?, "
                    + " vac_fechavac = ?, "
                    + " vac_fechavacprox = ?,"
                    + " vac_mascota = ? "
                    + " WHERE vac_codvacmascota = " + ibVacunacion + "";

            PreparedStatement estado = conexion.prepareStatement(sql);

            estado.setString(1, (tfNombreVacunacion.getText()));
            estado.setString(2, (tfFechaVacunacion.getText()));
            estado.setString(3, (tfFechaProximaVacunacion.getText()));
            estado.setString(4, (combomas.getValue()));

            int n = estado.executeUpdate();

            if (n > 0) {
                tablaVacunacion.getColumns().clear();
                tablaVacunacion.getItems().clear();
                cargarDatosTabla();
            }

            estado.close();
            tfNombreVacunacion.setText(null);
            tfFechaVacunacion.setText(null);
            tfFechaProximaVacunacion.setText(null);
            combomas.setValue("Mascota");
            lbNomiCliente.setText(null);
        } catch (SQLException e) {
            System.out.println("Error " + e);
        }

    }

    @FXML
    private void eliminarVacunacion(ActionEvent event) {

        int confirmarEliminar = JOptionPane.showConfirmDialog(null, "Realmente desea eliminar esta VACUNACION??");

        if (confirmarEliminar == 0) {
            try {
                conexion = DBConnection.connect();
                String sql = "DELETE FROM vacunacion WHERE vac_codvacmascota = " + ibVacunacion + "";
                PreparedStatement estado = conexion.prepareStatement(sql);
                int n = estado.executeUpdate();
                if (n > 0) {
                    tablaVacunacion.getColumns().clear();
                    tablaVacunacion.getItems().clear();
                    cargarDatosTabla();
                }

                estado.close();
                tfNombreVacunacion.setText(null);
                tfFechaVacunacion.setText(null);
                tfFechaProximaVacunacion.setText(null);
                combomas.setValue("Mascota");
                lbNomiCliente.setText(null);

            } catch (SQLException e) {
                System.out.println("Error " + e);
            }
        }
    }

    @FXML
    private void buscarMedicamentos(ActionEvent event) {
        String fechaVencimiento = null;
        String codigo = null;
        Date fechaActual = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        String fechaSistema = formateador.format(fechaActual);

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
                    + " WHERE upper(med_nombre) LIKE '%" + tfBuscarMedicamentos.getText().toUpperCase() + "%'"
                    + " ORDER BY med_nombre DESC";

            ResultSet rs = conexion.createStatement().executeQuery(sql);

            while (rs.next()) {

                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    codigo = (rs.getString("med_lote"));
                    fechaVencimiento = (rs.getString("med_vencimiento"));

                    if (fechaVencimiento.equals(fechaSistema)) {

                        String sql1 = "UPDATE medicamentos "
                                + " SET med_Descripcion  = ?"
                                + " WHERE med_lote = '" + codigo + "'";
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
    private void buscarTipo(ActionEvent event) {
        String fechaVencimiento = null;
        String codigo = null;
        Date fechaActual = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        String fechaSistema = formateador.format(fechaActual);

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
                    + " WHERE upper(med_tipo) LIKE '%" + tfBuscarTipo.getText().toUpperCase() + "%'"
                    + " ORDER BY med_nombre DESC";

            ResultSet rs = conexion.createStatement().executeQuery(sql);

            while (rs.next()) {

                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    codigo = (rs.getString("med_lote"));
                    fechaVencimiento = (rs.getString("med_vencimiento"));

                    if (fechaVencimiento.equals(fechaSistema)) {

                        String sql1 = "UPDATE medicamentos "
                                + " SET med_Descripcion  = ?"
                                + " WHERE med_lote = '" + codigo + "'";
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
    private void buscarVacunacion(ActionEvent event) {

        tablaVacunacion.getItems().clear();
        try {
            conexion = DBConnection.connect();
            String sql = "SELECT vac_codvacmascota, "
                    + " vac_nombre, "
                    + " vac_fechavac, "
                    + " vac_fechavacprox, "
                    + " vac_mascota "
                    + " FROM vacunacion "
                    + " WHERE upper(vac_nombre) LIKE '%" + tfBuscarVacunacion.getText().toUpperCase() + "%'"
                    + " ORDER BY vac_nombre DESC";

            ResultSet rs = conexion.createStatement().executeQuery(sql);

            while (rs.next()) {

                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {

                    row.add(rs.getString(i));
                }
                Vacunacion.addAll(row);
            }
            tablaVacunacion.setItems(Vacunacion);
            rs.close();

        } catch (SQLException e) {
            System.out.println("Error " + e.getMessage());
        }

    }

    @FXML
    private void buscarVacunacionMascota(ActionEvent event) {

        tablaVacunacion.getItems().clear();
        try {
            conexion = DBConnection.connect();

            String sql = "select  vac_codvacmascota, vac_nombre, vac_fechavac,  vac_fechavacprox, vac_mascota "
                    + " from vacunacion v "
                    + "inner join mascotas m on v.vac_codmascota = mas_codmascotas "
                    + " where m.mas_codpropietario = (select cl.cli_codcliente "
                    + " from clientes cl where cl.cli_cedula = " + tfBuscarMascota.getText() + ")";

            ResultSet rs = conexion.createStatement().executeQuery(sql);

            while (rs.next()) {

                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {

                    row.add(rs.getString(i));
                }
                Vacunacion.addAll(row);
            }
            tablaVacunacion.setItems(Vacunacion);
            rs.close();
            tfBuscarMascota.clear();

        } catch (SQLException e) {
            System.out.println("Error " + e.getMessage());
        }

    }

    @FXML
    private void nuevoVacunacion(ActionEvent event) {

        btAddVacunacion.setDisable(false);
        btEliminarVacunacion.setDisable(true);
        btModificarVacunacion.setDisable(true);
        btAddVacunacion.setStyle("-fx-background-color:#66CCCC");
        btEliminarVacunacion.setStyle("-fx-background-color:grey");
        btModificarVacunacion.setStyle("-fx-background-color:grey");
        tfNombreVacunacion.setText(null);
        tfFechaVacunacion.setText(null);
        tfFechaProximaVacunacion.setText(null);
        combomas.setValue("Mascota");
        lbNomiCliente.setText(null);
    }

    public static boolean isFechaValida(String fecha) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            formatoFecha.setLenient(false);
            formatoFecha.parse(fecha);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public void cargarServiciosTabla() {
        servicios = FXCollections.observableArrayList();

        try {
            conexion = DBConnection.connect();
            /* String sql = "SELECT m.*,c.cli_nombre as Propietario FROM mascotas m "
             + "INNER JOIN clientes c ON c.cli_codcliente = m.mas_codpropietario "
             + "ORDER BY m.mas_nombre";*/

            String sql = "SELECT ser_codservicio, ser_descripcion, ser_precio FROM servicios "
                    + "UNION "
                    + " SELECT med_codmedicamentos as ser_codservicio, med_nombre as ser_descripcion,"
                    + " med_precio as ser_precio "
                    + " FROM medicamentos";
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
            servicios = null;
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error " + e);
        }
    }

    @FXML
    private void getMedicamentosSeleccionado(MouseEvent event) {
        tablaMedicamentos.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (tablaMedicamentos != null) {

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
                    String codigo = null;
                    if (m5.find()) {
                        codigo = cincoDigitos;
                        //  tfNombreVacunacion.setText(cincoDigitos);
                    } else {
                        if (m4.find()) {
                            codigo = cuatroDigitos;
                            // tfNombreVacunacion.setText(cuatroDigitos);
                        } else {
                            if (m3.find()) {
                                codigo = tresDigitos;
                                // tfNombreVacunacion.setText(tresDigitos);
                            } else {
                                if (m2.find()) {
                                    codigo = dosDigitos;
                                    //  tfNombreVacunacion.setText(dosDigitos);
                                } else {
                                    codigo = cincoDigitos;
                                    // tfNombreVacunacion.setText(cincoDigitos);
                                }
                            }
                        }
                    }
                    try {
                        conexion = DBConnection.connect();
                        /* String sql = "SELECT m.*,c.cli_nombre as Propietario FROM mascotas m "
                         + "INNER JOIN clientes c ON c.cli_codcliente = m.mas_codpropietario "
                         + "ORDER BY m.mas_nombre";*/
                    } catch (SQLException ex) {
                        Logger.getLogger(VacunacionController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    String sql = "SELECT med_nombre, med_observacion"
                            + " FROM medicamentos"
                            + " WHERE med_codmedicamentos= " + Integer.parseInt(codigo) + "";
                    try {
                        ResultSet rs = conexion.createStatement().executeQuery(sql);
                        while (rs.next()) {
                            //Iterate Row
                            ObservableList<String> row = FXCollections.observableArrayList();
                            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                                if (rs.getString("med_observacion").equals("VENCIDO")) {
                                    JOptionPane.showMessageDialog(null, "MEDICAMENTO VENCIDO");

                                } else {
                                    tfNombreVacunacion.setText(rs.getString("med_nombre").toUpperCase());
                                }
                                //Iterate Column
                                row.add(rs.getString(i));
                            }
                            //System.out.println("Row [1] added "+row );

                        }

                    } catch (SQLException ex) {
                        Logger.getLogger(VacunacionController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        });
    }

    @FXML
    private void irMantenimientoCliente(ActionEvent event) {

        controlador.setScreen(ScreensFramework.mantenimientoClienteID);
    }

    public void cargarDatosTablaMedicamentos() {
        medicamentos = FXCollections.observableArrayList();

        try {
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
                "Descripcion",
                "Vencimiento",
                "Lote",
                "Precio",
                "Stock",
                "Observacion",
                "Tipo"
            };

            /**
             * ********************************
             * Columna cargada dinamicamente * ********************************
             */
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                col = new TableColumn(titulos[i]);
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> parametro) {
                        return new SimpleStringProperty((String) parametro.getValue().get(j));
                    }
                });
                tablaMedicamentos.getColumns().addAll(col);
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
                medicamentos.addAll(row);
            }
            //FINALLY ADDED TO TableView
            tablaMedicamentos.setItems(medicamentos);
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error " + e);
        }
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
