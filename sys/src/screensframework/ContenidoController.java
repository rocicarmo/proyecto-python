package screensframework;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import screensframework.DBConnect.DBConnection;
import screensframework.Funcion;
import static screensframework.Funcion.envioSms;

/**
 * Controller de la ventana principal
 *
 * @author Rocio Carmona
 */
public class ContenidoController implements Initializable, ControlledScreen {

    @FXML
    private Label Usuario;

    ScreensController controlador;
    @FXML
    private Label prueba;
    private ControlesBasicos controlesBasicos = new ControlesBasicos();
    private Connection conexion;

    /**
     * Inicializa controladores.
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
    public void Usuario(ActionEvent event) {

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
    private void irMantenimientoVacunacion(ActionEvent event) {

        controlador.setScreen(ScreensFramework.mantenimientoVacunacionID);
    }

    @FXML
    private void irMantenimientoMedicamentos(ActionEvent event) {

        controlador.setScreen(ScreensFramework.mantenimientoMedicamentosID);
    }

    @FXML
    private void salir(ActionEvent event) {
        this.controlesBasicos.salirSistema();
    }

    @FXML
    public void usuarioNombre(String usu) {
        Usuario.setText(usu);
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        controlador.setScreen(ScreensFramework.loginID);
    }

    public void prueba(String prueba1) {
        prueba.setText(prueba1);
    }
public void cargarDatosTabla(){
 envioSms("", "", "961414640");
}
    public void cargarDatosTabla1() throws ParseException {
        //   producto = FXCollections.observableArrayList();
        Date fechaActual = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd");
        String fechaSistema = formateador.format(fechaActual);
        Date fecha = formateador.parse(fechaSistema);
        long fechaLong = fecha.getTime();
        String valor = null;
        try {
            conexion = DBConnection.connect();

            String sql = "SELECT vac_fechavacprox, "
                    + " FROM vacunacion "
                    + " WHERE vac_fechavacprox=" + fechaSistema;
            //ResultSet
            ResultSet rs = conexion.createStatement().executeQuery(sql);
            valor = rs.getString("vac_fechavacprox");
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error " + e);
        }

        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
        String strFecha = "2014-12-02";
        Date fecha1 = null;
        fecha = formatoDelTexto.parse(strFecha);
        long fechaproximalong = fecha.getTime();
        if (fechaproximalong != fechaLong) {
            try {
                conexion = DBConnection.connect();
                valor="2014-12-02";

                String sql1 = "select c.cli_telefono from " +
"clientes c inner join mascotas m on c.cli_codcliente=m.mas_codpropietario " +
"inner join vacunacion v on m.mas_codmascota=v.vac_codvacmascota " +
"where v.vac_fechavac= "+valor;
                ResultSet rs1 = conexion.createStatement().executeQuery(sql1);
                
               int numero= Integer.parseInt(rs1.getString("c.cli_telefono"));
           //    String num= snumero;
                valor = rs1.getString("vac_fechavacprox");
                envioSms("hola", "que tal", "961414640");
                rs1.close();
            } catch (SQLException e) {
                System.out.println("Error " + e);
            }

        }
    }
}
