/*
 * Controlador de la ventana Mascotas
 * @author Rocio Carmona
 */
package screensframework;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
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

public class MascotasController implements Initializable, ControlledScreen {

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
    private Label lbCodApellido;
    @FXML
    private Label lbClienteApellido;
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
    private Label lbCodC;
    @FXML
    private Label lbCodCliente;
    @FXML
    private Label lbCodClie;
    @FXML
    private TableView tablaClientes;
    @FXML
    private TableView tablaMascotas;
    @FXML
    private TableColumn col;
    private Connection conexion;
    int cedula = 0;
    int codCliente = 0;
    ObservableList<ObservableList> clientes;
    ObservableList<ObservableList> mascotas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.cargarClientesTabla();
        this.cargarMascotasTabla();

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

    public void cargarClientesTabla() {
        clientes = FXCollections.observableArrayList();

        try {
            conexion = DBConnection.connect();

            String sql = "SELECT cli_cedula, cli_nombre, cli_apellido FROM CLIENTES ORDER BY cli_nombre";
            //ResultSet
            ResultSet rs = conexion.createStatement().executeQuery(sql);
            // Títulos de las columnas
            String[] titulos = {
                //"Cod",
                "Cédula",
                "Nombre",
                "Apellido"
            };
            /**
             * ********************************
             * Tabla cargada dinamicamente *
             *********************************
             */

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                col = new TableColumn(titulos[i]);
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> parametro) {
                        return new SimpleStringProperty((String) parametro.getValue().get(j));
                    }
                });
                tablaClientes.getColumns().addAll(col);
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
             * Cargamos de la base de datos *
             *******************************
             */
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                //System.out.println("Row [1] added "+row );
                clientes.addAll(row);
            }
            //FINALLY ADDED TO TableView
            tablaClientes.setItems(clientes);
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error " + e);
        }
    }

    public void cargarMascotasTabla() {
        mascotas = FXCollections.observableArrayList();

        try {
            conexion = DBConnection.connect();
            String sql = "SELECT m.*,c.cli_nombre as Propietario FROM mascotas m "
                    + "INNER JOIN clientes c ON c.cli_codcliente = m.mas_codpropietario "
                    + "ORDER BY m.mas_nombre";
            //ResultSet
            ResultSet rs = conexion.createStatement().executeQuery(sql);
            // Títulos de las columnas
            String[] titulos = {
                "Cod",
                "CodProp",
                "Nombre",
                "Sexo",
                "Raza",
                "FechaNac",
                "Edad",
                "Propietario"
            };
            /**
             * ********************************
             * TABLE COLUMN ADDED DYNAMICALLY *
             *********************************
             */

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                col = new TableColumn(titulos[i]);
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> parametro) {
                        return new SimpleStringProperty((String) parametro.getValue().get(j));
                    }
                });
                tablaMascotas.getColumns().addAll(col);
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
             * Cargamos de la base de datos *
             *******************************
             */
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                //System.out.println("Row [1] added "+row );
                mascotas.addAll(row);
            }
            //FINALLY ADDED TO TableView
            tablaMascotas.setItems(mascotas);
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
                    + " WHERE mas_codmascotas = " + valor;
            ResultSet rs = conexion.createStatement().executeQuery(sql);

            while (rs.next()) {

                lbCodMascota.setText(rs.getString("mas_codmascotas"));
                tfNombreMascota.setText(rs.getString("mas_nombre"));
                tfSexoMascota.setText(rs.getString("mas_sexo"));
                tfRazaMascota.setText(rs.getString("mas_raza"));
                tfFechaNacMascota.setText(rs.getString("mas_fechanac"));
              //  tfEdadMascota.setText(rs.getString("mas_edad"));
                // lbCodC.setText(rs.getString("mas_cod"));
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error " + ex);
        }

    }

    public void cargarClientesText(String valor) {

        try {

            conexion = DBConnection.connect();
            String sql = "SELECT cli_codcliente, cli_cedula,"
                    + "cli_nombre, "
                    + " cli_apellido "
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
               // lbCodCliente.setText(rs.getString("cli_codcliente"));

                codCliente = Integer.parseInt(rs.getString("cli_codcliente"));

                lbClienteNombre.setText(rs.getString("cli_nombre"));
                lbCodApellido.setText(rs.getString("cli_apellido"));
                 //cedula=Integer.parseInt(rs.getString("cli_cedula"));
                //   lbCodClie.setText(rs.getString("cli_codcliente"));

            }
            codCliente = Integer.parseInt(rs.getString("cli_codcliente"));
            lbClienteNombre.setText(rs.getString("cli_nombre"));
            lbCodApellido.setText(rs.getString("cli_apellido"));
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error " + ex);
        }
    }

    @FXML
    private void getClienteSeleccionado(MouseEvent event) {
        tablaClientes.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (tablaClientes != null) {

                    String valor = tablaClientes.getSelectionModel().getSelectedItems().get(0).toString();
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
                        cargarClientesText(sieteDigitos);
                    } else {
                        if (m4.find()) {
                            cargarClientesText(cuatroDigitos);
                        } else {
                            if (m3.find()) {
                                cargarClientesText(tresDigitos);
                            } else {
                                if (m2.find()) {
                                    cargarClientesText(dosDigitos);
                                } else {
                                    cargarClientesText(unDigitos);
                                }

                            }
                        }
                    }
                }
            }
        });
    }

    @FXML
    private void buscarCliente(ActionEvent event) {
        tablaClientes.getItems().clear();
        try {
            conexion = DBConnection.connect();
            String sql1 = "SELECT  cli_codcliente, cli_cedula"
                    + " FROM clientes "
                    + " WHERE (cli_cedula)= " + tfBuscarCliente.getText()
                    + " ORDER BY cli_nombre DESC";
            ResultSet rs1 = conexion.createStatement().executeQuery(sql1);
            //codCliente=Integer.parseInt(rs1.getString("cli_codcliente"));  
            String sql = "SELECT  cli_cedula,"
                    + "cli_nombre, "
                    + " cli_apellido "
                    + " FROM clientes "
                    + " WHERE (cli_cedula)= " + tfBuscarCliente.getText()
                    + " ORDER BY cli_nombre DESC";
            ResultSet rs = conexion.createStatement().executeQuery(sql);
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));

                }
                clientes.addAll(row);
            }
            tablaClientes.setItems(clientes);
            rs.close();

        } catch (SQLException e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    @FXML
    private void addMascota(ActionEvent event) {

        //int indiceCategoria = cbCategoriaProducto.getSelectionModel().getSelectedIndex() + 1;
        //int indiceMarca = cbMarcaProducto.getSelectionModel().getSelectedIndex() + 1;
        boolean fecha = false;
        java.sql.Date da = null;
        int edad = 0;
        if (tfNombreMascota.getText().trim().length() != 0) {
            fecha = isFechaValida(tfFechaNacMascota.getText());

            edad = calcularEdad(tfFechaNacMascota.getText());
        }
        try {
            if (codCliente == 0) {

                JOptionPane.showMessageDialog(null, "Seleccione Cliente");

            } else if (tfNombreMascota.getText().trim().length() == 0) {

                JOptionPane.showMessageDialog(null, "Nombre de Mascota no puede ser nulo");

            } else if (tfSexoMascota.getText().trim().length() == 0) {

                JOptionPane.showMessageDialog(null, "Sexo no puede ser nulo");

            } else if (tfRazaMascota.getText().trim().length() == 0) {
                JOptionPane.showMessageDialog(null, "Raza no puede ser nulo");
            } else if (fecha != true) {
                JOptionPane.showMessageDialog(null, "Fecha invalida");
            } else {
                conexion = DBConnection.connect();

                String fechanac = tfFechaNacMascota.getText();
                try {
                    da = java.sql.Date.valueOf(fechanac);
                } catch (Exception e) {
                    System.out.println("Error " + e);
                }

                String sql = "INSERT INTO mascotas "
                        + " (mas_codpropietario,mas_nombre,mas_sexo,mas_raza,mas_fechanac, mas_edad) "
                        + " VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement estado = conexion.prepareStatement(sql);
                estado.setInt(1, codCliente);
                estado.setString(2, tfNombreMascota.getText().toUpperCase());
                estado.setString(3, tfSexoMascota.getText().toUpperCase());
                estado.setString(4, tfRazaMascota.getText().toUpperCase());
                estado.setDate(5, da);
                estado.setInt(6, edad);

                int n = estado.executeUpdate();

                if (n > 0) {
                    tablaMascotas.getColumns().clear();
                    tablaMascotas.getItems().clear();
                    cargarMascotasTabla();
                }

                estado.close();
            }
            // lbCodCliente.setText("");
            codCliente = 0;
            lbClienteNombre.setText("");
            lbCodApellido.setText("");
            tfNombreMascota.setText("");
            tfSexoMascota.setText("");
            tfRazaMascota.setText("");
            tfFechaNacMascota.setText("");

        } catch (SQLException e) {
            System.out.println("Error " + e);
        }

    }

    @FXML
    private void modificarMascota(ActionEvent event) {

        //int indiceCategoria = cbCategoriaProducto.getSelectionModel().getSelectedIndex() + 1;
        //int indiceMarca = cbMarcaProducto.getSelectionModel().getSelectedIndex() + 1;
        java.sql.Date da = null;
        try {
            conexion = DBConnection.connect();
            String strDate = tfFechaNacMascota.getText();
            try {
                da = java.sql.Date.valueOf(strDate);
            } catch (Exception e) {
                System.out.println("Error " + e);
            }

            String sql = "UPDATE mascotas "
                    + " SET mas_nombre = ?, "
                    //  + " mas_nombre = ?, "
                    + " mas_sexo = ?, "
                    + " mas_raza = ?,"
                    + " mas_fechanac = ?"
                    //  + " mas_edad = ?"
                    + " WHERE mas_codmascotas = " + lbCodMascota.getText() + "";

            PreparedStatement estado = conexion.prepareStatement(sql);

            // estado.setInt(1, Integer.parseInt(lbCodCliente.getText()));
            estado.setString(1, tfNombreMascota.getText().toUpperCase());
            estado.setString(2, tfSexoMascota.getText().toUpperCase());
            estado.setString(3, tfRazaMascota.getText().toUpperCase());
            estado.setDate(4, da);

            //  estado.setInt(6, Integer.parseInt(tfEdadMascota.getText()));
            int n = estado.executeUpdate();

            if (n > 0) {
                tablaMascotas.getColumns().clear();
                tablaMascotas.getItems().clear();
                lbCodMascota.setText("");
                    lbCodApellido.setText("");
                    tfNombreMascota.setText("");
                    tfSexoMascota.setText("");
                    tfRazaMascota.setText("");
                    tfFechaNacMascota.setText("");
                    lbClienteNombre.setText(null);
                    lbCodApellido.setText(null);
                    //tfEdadMascota.setText("");
                    tablaMascotas.getColumns().clear();
                    tablaMascotas.getItems().clear();
                cargarMascotasTabla();
            }

            estado.close();
        } catch (SQLException e) {
            System.out.println("Error " + e);
        }
    }

    @FXML
    private void eliminarMascota(ActionEvent event) {

        int confirmarEliminar = JOptionPane.showConfirmDialog(null, "Realmente desea eliminar esta Mascota??");

        if (confirmarEliminar == 0) {
            try {
                conexion = DBConnection.connect();
                String sql = "DELETE FROM mascotas WHERE mas_codmascotas = " + lbCodMascota.getText() + "";
                PreparedStatement estado = conexion.prepareStatement(sql);
                int n = estado.executeUpdate();
                if (n > 0) {
                    lbCodMascota.setText("");
                    lbCodApellido.setText("");
                    tfNombreMascota.setText("");
                    tfSexoMascota.setText("");
                    tfRazaMascota.setText("");
                    tfFechaNacMascota.setText("");
                    lbClienteNombre.setText(null);
                    lbCodApellido.setText(null);
                    //tfEdadMascota.setText("");
                    tablaMascotas.getColumns().clear();
                    tablaMascotas.getItems().clear();
                    cargarMascotasTabla();
                }

                estado.close();
                //  lbCodCliente.setText("");
                lbClienteNombre.setText("");
                tfNombreMascota.setText("");
                tfSexoMascota.setText("");
                tfRazaMascota.setText("");
                tfFechaNacMascota.setText("");
            } catch (SQLException e) {
                System.out.println("Error " + e);
            }
        }
    }

    @FXML
    private void buscarMascota(ActionEvent event) {

        tablaMascotas.getItems().clear();
        try {
            conexion = DBConnection.connect();
            String sql = "SELECT m.*,c.cli_nombre FROM mascotas m "
                    + "INNER JOIN clientes c ON c.cli_codcliente = m.mas_codpropietario"
                    + " WHERE upper(mas_nombre) LIKE '%" + tfBuscarMascota.getText().toUpperCase() + "%'"
                    + " ORDER BY mas_nombre DESC";

            ResultSet rs = conexion.createStatement().executeQuery(sql);

            while (rs.next()) {

                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {

                    row.add(rs.getString(i));
                }
                mascotas.addAll(row);
            }
            tablaMascotas.setItems(mascotas);
            rs.close();

        } catch (SQLException e) {
            System.out.println("Error " + e.getMessage());
        }

    }

    @FXML
    private void nuevaMascota(ActionEvent event) {

        lbCodMascota.setText("");
        tfNombreMascota.setText("");
        tfSexoMascota.setText("");
        tfRazaMascota.setText("");
        tfFechaNacMascota.setText("");
        lbClienteNombre.setText("");
        lbCodApellido.setText("");
        btAddMascota.setDisable(false);
        btEliminarMascota.setDisable(true);
        btModificarMascota.setDisable(true);
        btAddMascota.setStyle("-fx-background-color:#66CCCC");
        btEliminarMascota.setStyle("-fx-background-color:grey");
        btModificarMascota.setStyle("-fx-background-color:grey");
    }

    @FXML
    private void getMascotaSeleccionada(MouseEvent event) {
        tablaMascotas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (tablaMascotas != null) {

                    btAddMascota.setDisable(false);
                    btEliminarMascota.setDisable(false);
                    btModificarMascota.setDisable(false);
                    btAddMascota.setStyle("-fx-background-color:grey");
                    btEliminarMascota.setStyle("-fx-background-color:#66CCCC");
                    btModificarMascota.setStyle("-fx-background-color:#66CCCC");

                    String valor = tablaMascotas.getSelectionModel().getSelectedItems().get(0).toString();

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
                        cargarMascotasText(cincoDigitos);
                    } else {
                        if (m4.find()) {
                            cargarMascotasText(cuatroDigitos);
                        } else {
                            if (m3.find()) {
                                cargarMascotasText(tresDigitos);
                            } else {
                                if (m2.find()) {
                                    cargarMascotasText(dosDigitos);
                                } else {
                                    cargarMascotasText(unDigitos);
                                }
                            }
                        }
                    }

                }
            }
        });
    }

    public static boolean isFechaValida(String fecha) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            formatoFecha.setLenient(false);
            formatoFecha.parse(fecha);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    @FXML
    public static int calcularEdad(String fecha) {
        String datetext = fecha;
        try {
            Calendar birth = new GregorianCalendar();
            Calendar today = new GregorianCalendar();
            int age = 0;
            int factor = 0;
            String diaArray[] = fecha.split("-");
            String fechaCadena = diaArray[2] + '-' + diaArray[1] + '-' + diaArray[0];

            java.util.Date birthDate = new SimpleDateFormat("dd-MM-yyyy").parse(fechaCadena);
            java.util.Date currentDate = new java.util.Date(); //current date
            birth.setTime(birthDate);
            today.setTime(currentDate);
            if (today.get(Calendar.MONTH) <= birth.get(Calendar.MONTH)) {
                if (today.get(Calendar.MONTH) == birth.get(Calendar.MONTH)) {
                    if (today.get(Calendar.DATE) > birth.get(Calendar.DATE)) {
                        factor = -1; //Aun no celebra su cumpleaÃ±os
                    }
                } else {
                    factor = -1; //Aun no celebra su cumpleaÃ±os
                }
            }
            age = (today.get(Calendar.YEAR) - birth.get(Calendar.YEAR)) + factor;
            return age;
        } catch (ParseException e) {
            return -1;
        }
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
