package screensframework;

/**
 * Controlador de la ventana Clientes
 */
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import screensframework.DBConnect.DBConnection;

public class ClientesController implements Initializable, ControlledScreen {

    ScreensController controlador;
    private ControlesBasicos controlesBasicos = new ControlesBasicos();
    @FXML
    private Button btAddCliente;
    @FXML
    private Button btModificarCliente;
    @FXML
    private Button btEliminarCliente;
    @FXML
    private Button btNuevoCliente;

    @FXML
    private Button btReporteClientes;

    @FXML
    private TextField tfNombreCliente;
    @FXML
    private TextField tfPrecioCliente;
    @FXML
    private TextField tfBuscarCliente;
    @FXML
    private TextField tfClienteDir;
    @FXML
    private TextField tfClienteApellido;
    @FXML
    private TextField tfClienteTel;
    @FXML
    private TextField tfClienteCiudad;
    @FXML
    private TextField tfmascotaM;
    @FXML
    private TextField tfReporteClientes;
    @FXML
    private TextField tfClienteNombre;
    @FXML
    private ComboBox cbCategoriaCliente;
    @FXML
    private ComboBox cbMarcaCliente;
    @FXML
    private Label lbCodigoCliente;

    @FXML
    private TableView tablaCliente;
    @FXML
    private TableColumn col;
    private Connection conexion;

    ObservableList<ObservableList> producto;

    @FXML
    private void irMantenimientoMascotas(ActionEvent event) {
        controlador.setScreen(ScreensFramework.mantenimientoMascotaID);
    }

    @FXML
    private void reporteClientes(ActionEvent event) {//GEN-FIRST:event_jButton7ActionPerformed
        //limpiarPanel();
        try {

            Connection conexion = null;
            try {
                conexion = DBConnection.connect();

            } catch (SQLException e) {
                System.out.println("Error " + e);
            }
            /*  JasperReport reporte = JasperCompileManager.compileReport("ReporteClientes.jrxml");
             JasperPrint print = JasperFillManager.fillReport(reporte, null, conexion);
             JasperViewer visor = new JasperViewer(print, false);
             // visor.setVisible(true);
             // visor.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);*/

            InputStream reportStream = this.getClass().getResourceAsStream("ReporteClientes.jrxml");

// Convert template to JasperDesign  
            JasperDesign jd = JRXmlLoader.load(reportStream);

// Compile design to JasperReport  
            JasperReport jr = JasperCompileManager.compileReport(jd);

// Create the JasperPrint object  
// Make sure to pass the JasperReport, report parameters, and data source  
            JasperPrint jp = JasperFillManager.fillReport(jr, null, conexion);
            JasperViewer jv = new JasperViewer(jp);
//jv.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  
            jv.setVisible(true);
        } catch (Exception e) {
            System.out.println("Error al generar el reporte" + e);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.cargarDatosTabla();

        btEliminarCliente.setDisable(true);
        btModificarCliente.setDisable(true);
        btEliminarCliente.setStyle("-fx-background-color:grey");
        btModificarCliente.setStyle("-fx-background-color:grey");

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

    public void cargarDatosTabla() {
        producto = FXCollections.observableArrayList();

        try {
            conexion = DBConnection.connect();

            String sql = "SELECT cli_cedula, "
                    + " cli_nombre, "
                     + " cli_apellido, "
                    + " cli_direccion, "
                    + " cli_telefono, "
                    + " cli_ciudad "
                    + " FROM clientes "
                    + " ORDER BY cli_nombre DESC";
            //ResultSet
            ResultSet rs = conexion.createStatement().executeQuery(sql);
            // Títulos de las columnas
            String[] titulos = {
                // "Cod",
                "Cedula",
                "Nombre",
                "Apellido",
                "Direccion",
                "Telefono",
                "Ciudad"
            };
            /**
             * ********************************
             * Tabla cargada dinamicamente * ********************************
             */

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                col = new TableColumn(titulos[i]);
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> parametro) {
                        return new SimpleStringProperty((String) parametro.getValue().get(j));
                    }
                });
                tablaCliente.getColumns().addAll(col);
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
                producto.addAll(row);
            }
            //FINALLY ADDED TO TableView
            tablaCliente.setItems(producto);
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error " + e);
        }
    }

    public void cargarClienteText(String valor) {

        try {

            conexion = DBConnection.connect();
            String sql = "SELECT * "
                    + " FROM clientes "
                    + " WHERE cli_cedula = " + valor;
            ResultSet rs = conexion.createStatement().executeQuery(sql);
            /*
             cli_cedula, "
             + " cli_nombre, "
             + " cli_direccion, "
             + " cli_telefono "
             + " FROM clientes
             */
            while (rs.next()) {
                //  lbCodigoCliente.setText(rs.getString("cli_codcliente").toUpperCase());
                tfNombreCliente.setText(rs.getString("cli_cedula").toUpperCase());
                tfPrecioCliente.setText(rs.getString("cli_nombre").toUpperCase());
                tfClienteApellido.setText(rs.getString("cli_apellido").toUpperCase());
                tfClienteDir.setText(rs.getString("cli_direccion").toUpperCase());
                tfClienteTel.setText(rs.getString("cli_telefono").toUpperCase());
                tfClienteCiudad.setText(rs.getString("cli_ciudad").toUpperCase());

               // tfmascotaM.setText(rs.getString("cli_nombre"));
                //cbCategoriaCliente.setValue(rs.getString("cli_direccion"));
                //cbMarcaCliente.setValue(rs.getString("cli_telefono"));

            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error " + ex);
        }

    }

    @FXML
    private void getClienteSeleccionado(MouseEvent event) {
        tablaCliente.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (tablaCliente != null) {

                    btAddCliente.setDisable(true);
                    btEliminarCliente.setDisable(false);
                    btModificarCliente.setDisable(false);
                    btAddCliente.setStyle("-fx-background-color:grey");
                    btEliminarCliente.setStyle("-fx-background-color:#66CCCC");
                    btModificarCliente.setStyle("-fx-background-color:#66CCCC");

                    String valor = tablaCliente.getSelectionModel().getSelectedItems().get(0).toString();
                    String sieteDigitos = valor.substring(1, 8);
                    String cincoDigitos = valor.substring(1, 6);
                    String cuatroDigitos = valor.substring(1, 5);
                    String tresDigitos = valor.substring(1, 4);
                    String dosDigitos = valor.substring(1, 3);
                    String unDigitos = valor.substring(1, 2);

                    Pattern p = Pattern.compile("^[0-9]*$");
                    Matcher m7 = p.matcher(sieteDigitos);
                    Matcher m5 = p.matcher(cincoDigitos);
                    Matcher m4 = p.matcher(cuatroDigitos);
                    Matcher m3 = p.matcher(tresDigitos);
                    Matcher m2 = p.matcher(dosDigitos);

                    if (m7.find()) {
                        cargarClienteText(sieteDigitos);
                    } else {
                        if (m4.find()) {
                            cargarClienteText(cuatroDigitos);
                        } else {
                            if (m3.find()) {
                                cargarClienteText(tresDigitos);
                            } else {
                                if (m2.find()) {
                                    cargarClienteText(dosDigitos);
                                } else {
                                    cargarClienteText(unDigitos);
                                }

                            }
                        }
                    }
                }
            }
        });
    }

    @FXML
    private void addCliente(ActionEvent event) throws SQLException {

        //int indiceCategoria = cbCategoriaCliente.getSelectionModel().getSelectedIndex() + 1;
        //int indiceMarca = cbMarcaCliente.getSelectionModel().getSelectedIndex() + 1;
        if (tfNombreCliente.getText().trim().length() == 0) {

            JOptionPane.showMessageDialog(null, "Cédula no puede ser nulo");

        } else if (tfNombreCliente.getText().matches("[^0-9]*")) {
            JOptionPane.showMessageDialog(null, "Cédula debe ser numérico");

        } else if (tfNombreCliente.getText().trim().length() >= 8) {

                JOptionPane.showMessageDialog(null, "Cédula invalido");

            }else {
            conexion = DBConnection.connect();
        }
        String sql1 = "SELECT * "
                + " FROM clientes"
                + " WHERE (cli_cedula)= " + tfNombreCliente.getText();
        ResultSet rs1 = conexion.createStatement().executeQuery(sql1);
        int contador = 0;
        boolean existeUsuario = rs1.next();
        while (rs1.next()) {

            contador++;
        }
        try {
            if (contador != 0) {
                JOptionPane.showMessageDialog(null, "Cliente que desea crear ya existe");

            } else if (tfPrecioCliente.getText().trim().length() == 0) {

                JOptionPane.showMessageDialog(null, "Nombre no puede ser nulo");

            } else if (tfClienteApellido.getText().trim().length() == 0) {

                JOptionPane.showMessageDialog(null, "Apellido no puede ser nulo");

            } else if (tfClienteDir.getText().trim().length() == 0) {

                JOptionPane.showMessageDialog(null, "Dirección no puede ser nulo");

            } else if (tfClienteTel.getText().trim().length() == 0) {

                JOptionPane.showMessageDialog(null, "Número de telefono no puede ser nulo");

            } else if (tfClienteTel.getText().matches("[^0-9]*")) {

                JOptionPane.showMessageDialog(null, "Número de telefono debe ser numérico");

            } else if (tfClienteCiudad.getText().trim().length() == 0) {

                JOptionPane.showMessageDialog(null, "Ciudad no puede ser nulo");

            } else {
                // conexion = DBConnection.connect();
                String sql = "INSERT INTO clientes "
                        + " (cli_cedula, cli_nombre, cli_apellido, cli_direccion, cli_telefono, cli_ciudad) "
                        + " VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement estado = conexion.prepareStatement(sql);
                estado.setInt(1, Integer.parseInt(tfNombreCliente.getText()));
                estado.setString(2, tfPrecioCliente.getText().toUpperCase());
                estado.setString(3, tfClienteApellido.getText().toUpperCase());
                estado.setString(4, tfClienteDir.getText().toUpperCase());
                estado.setString(5, tfClienteTel.getText().toUpperCase());
                estado.setString(6, tfClienteCiudad.getText().toUpperCase());

                int n = estado.executeUpdate();

                if (n > 0) {
                    tablaCliente.getColumns().clear();
                    tablaCliente.getItems().clear();
                    cargarDatosTabla();
                }
                tfNombreCliente.setText("");
                tfPrecioCliente.setText("");
                tfClienteApellido.setText("");
                tfClienteDir.setText("");
                tfClienteTel.setText("");
                tfClienteCiudad.setText("");
                estado.close();
            }
        } catch (SQLException e) {
            System.out.println("Error " + e);
        }

    }

    @FXML
    private void modificarCliente(ActionEvent event) {

        //int indiceCategoria = cbCategoriaCliente.getSelectionModel().getSelectedIndex() + 1;
        //int indiceMarca = cbMarcaCliente.getSelectionModel().getSelectedIndex() + 1;
        try {
            conexion = DBConnection.connect();
            if (tfNombreCliente.getText().trim().length() == 0) {

                JOptionPane.showMessageDialog(null, "Cédula no puede ser nulo");

            } else if (tfNombreCliente.getText().matches("[^0-9]*")) {

                JOptionPane.showMessageDialog(null, "Cédula no puede ser nulo");

            } else if (tfNombreCliente.getText().trim().length() >= 8) {

                JOptionPane.showMessageDialog(null, "Cédula invalido");

            } else if (tfPrecioCliente.getText().trim().length() == 0) {

                JOptionPane.showMessageDialog(null, "Nombre no puede ser nulo");

            } else if (tfClienteApellido.getText().trim().length() == 0) {

                JOptionPane.showMessageDialog(null, "Apellido no puede ser nulo");

            } else if (tfClienteDir.getText().trim().length() == 0) {

                JOptionPane.showMessageDialog(null, "Dirección no puede ser nulo");

            } else if (tfClienteTel.getText().trim().length() == 0) {
                JOptionPane.showMessageDialog(null, "Número de telefono no puede ser nulo");
            } else if (tfClienteCiudad.getText().trim().length() == 0) {
                JOptionPane.showMessageDialog(null, "Ciudad no puede ser nulo");
            } else {
                String sql = "UPDATE clientes "
                        + " SET cli_cedula = ?, "
                        + " cli_nombre = ?, "
                        + " cli_apellido = ?, "
                        + " cli_direccion = ?, "
                        + " cli_telefono = ?, "
                        + " cli_ciudad = ?"
                        + " WHERE cli_cedula = " + Integer.parseInt(tfNombreCliente.getText()) + "";

                PreparedStatement estado = conexion.prepareStatement(sql);
                estado.setInt(1, Integer.parseInt(tfNombreCliente.getText()));
                estado.setString(2, (tfPrecioCliente.getText().toUpperCase()));
                estado.setString(3, tfClienteApellido.getText().toUpperCase());
                estado.setString(4, tfClienteDir.getText().toUpperCase());
                estado.setString(5, tfClienteTel.getText().toUpperCase());
                estado.setString(6, tfClienteCiudad.getText().toUpperCase());

                int n = estado.executeUpdate();

                if (n > 0) {
                    tablaCliente.getColumns().clear();
                    tablaCliente.getItems().clear();
                    cargarDatosTabla();
                }
                tfNombreCliente.setText("");
                tfPrecioCliente.setText("");
                tfClienteApellido.setText("");
                tfClienteDir.setText("");
                tfClienteTel.setText("");
                tfClienteCiudad.setText("");
                estado.close();
            }
        } catch (SQLException e) {
           // System.out.println("Error " + e);
              JOptionPane.showMessageDialog(null, "Error " + e);
        }
    }

    @FXML
    private void eliminarCliente(ActionEvent event) {

        int confirmarEliminar = JOptionPane.showConfirmDialog(null, "Realmente desea eliminar este CLIENTE??");

        if (confirmarEliminar == 0) {
            try {
                conexion = DBConnection.connect();
                String sql = "DELETE FROM clientes WHERE cli_cedula = " + Integer.parseInt(tfNombreCliente.getText()) + "";
                PreparedStatement estado = conexion.prepareStatement(sql);
                int n = estado.executeUpdate();
                if (n > 0) {
                    tablaCliente.getColumns().clear();
                    tablaCliente.getItems().clear();
                    cargarDatosTabla();
                }
                tfNombreCliente.setText("");
                tfPrecioCliente.setText("");
                tfClienteApellido.setText("");
                tfClienteDir.setText("");
                tfClienteTel.setText("");
                tfClienteCiudad.setText("");
                estado.close();

            } catch (SQLException e) {
              //  System.out.println("Error " + e);
                 JOptionPane.showMessageDialog(null, "Cliente ya tiene mascota asociada");
            }
        }
    }

    @FXML
    private void buscarCliente(ActionEvent event) {

        tablaCliente.getItems().clear();
        try {
            conexion = DBConnection.connect();
            String sql = "SELECT cli_codcliente, "
                    + " cli_cedula, "
                    + " cli_nombre, "
                    + " cli_apellido, "
                    + " cli_direccion, "
                    + " cli_telefono, "
                    + " cli_ciudad "
                    + " FROM clientes"
                    + " WHERE (cli_cedula)= " + tfBuscarCliente.getText()
                    + " ORDER BY cli_nombre DESC";

            ResultSet rs = conexion.createStatement().executeQuery(sql);

            while (rs.next()) {

                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {

                    row.add(rs.getString(i));
                }
                producto.addAll(row);
            }
            tablaCliente.setItems(producto);
            rs.close();

        } catch (SQLException e) {
            System.out.println("Error " + e.getMessage());
        }

    }

    @FXML
    private void nuevoCliente(ActionEvent event) {

        btAddCliente.setDisable(false);
        btEliminarCliente.setDisable(true);
        btModificarCliente.setDisable(true);
        btAddCliente.setStyle("-fx-background-color:#66CCCC");
        btEliminarCliente.setStyle("-fx-background-color:grey");
        btModificarCliente.setStyle("-fx-background-color:grey");
        tfNombreCliente.setText("");
        tfPrecioCliente.setText("");
        tfClienteApellido.setText("");
        tfClienteDir.setText("");
        tfClienteTel.setText("");
        tfClienteCiudad.setText("");
    }

    @FXML
    private void irMantenimientoCliente(ActionEvent event) {

        controlador.setScreen(ScreensFramework.mantenimientoClienteID);
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
