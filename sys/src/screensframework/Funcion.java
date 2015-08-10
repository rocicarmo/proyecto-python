package screensframework;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Funcion {
	public static boolean envioSms(String texto, String asunto, String numero) {
		boolean verdadero = true;
		String usuario = "rocicarmo@hotmail.com"; //tu correo
		String password = "ciruelita"; //tu contraseña
		String mensaje = texto;
		String titulo = asunto;
		String to = usuario;
		String from="595"+numero+"@vox.com.py";
		try {
			Properties props = new Properties();
			props.setProperty("mail.smtp.host", "smtp.live.com");//host de hotmail
			props.setProperty("mail.smtp.starttls.enable", "true");
			props.setProperty("mail.smtp.port","587");//puerto de conexion
			props.setProperty("mail.smtp.user", usuario); //verificacion del correo
			props.setProperty("mail.smtp.auth", "false");
			Session session = Session.getDefaultInstance(props);
			session.setDebug(true);
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(to));
			message.addRecipients(Message.RecipientType.TO, from); // remitente
			message.setSubject(titulo);
			message.setText(mensaje,"ISO-8859-1","html");
			Transport t = session.getTransport("smtp");
			t.connect(usuario, password); // aquie es lo delicado donde se loguea
			t.sendMessage(message, message.getAllRecipients());// aqi envia el mensaje
			t.close();
		} catch (Exception e) {
			verdadero = false;
			e.printStackTrace();
		}
		return verdadero;
	}
}
