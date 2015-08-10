package screensframework;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class EnvioSms extends JFrame {

	private JPanel contentPane;
	private JTextField txtNumero;
	private JTextField txtTitulo;
	private JTextArea txtMensaje;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EnvioSms frame = new EnvioSms();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
					frame.setAlwaysOnTop(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public EnvioSms() {
		Image icono = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sms/sms.png"));
		setIconImage(icono);
		setTitle("Env\u00EDo SMS");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 434, 411);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblEnvioDeSms = new JLabel("ENVIO DE SMS A CLIENTES");
		lblEnvioDeSms.setFont(new Font("Arial", Font.PLAIN, 20));
		lblEnvioDeSms.setBounds(70, 21, 283, 21);
		contentPane.add(lblEnvioDeSms);
		
		JLabel lblCelular = new JLabel("Celular:");
		lblCelular.setFont(new Font("Arial", Font.PLAIN, 14));
		lblCelular.setBounds(10, 59, 71, 29);
		contentPane.add(lblCelular);
		
		txtNumero = new JTextField();
		txtNumero.setFont(new Font("Arial", Font.PLAIN, 12));
		txtNumero.setBounds(70, 64, 144, 20);
		contentPane.add(txtNumero);
		txtNumero.setColumns(10);
		
		JLabel lblEj = new JLabel("EJ.: 961123132");
		lblEj.setFont(new Font("Arial", Font.PLAIN, 12));
		lblEj.setBounds(242, 67, 113, 14);
		contentPane.add(lblEj);
		
		JLabel lblTtulo = new JLabel("T\u00EDtulo:");
		lblTtulo.setFont(new Font("Arial", Font.PLAIN, 14));
		lblTtulo.setBounds(10, 110, 58, 21);
		contentPane.add(lblTtulo);
		
		txtTitulo = new JTextField();
		txtTitulo.setFont(new Font("Arial", Font.PLAIN, 12));
		txtTitulo.setBounds(70, 111, 187, 20);
		contentPane.add(txtTitulo);
		txtTitulo.setColumns(10);
		
		JLabel lblMensaje = new JLabel("Mensaje:");
		lblMensaje.setFont(new Font("Arial", Font.PLAIN, 14));
		lblMensaje.setBounds(10, 153, 71, 21);
		contentPane.add(lblMensaje);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 185, 385, 141);
		contentPane.add(scrollPane);
		
		txtMensaje = new JTextArea();
		txtMensaje.setFont(new Font("Arial", Font.PLAIN, 12));
		scrollPane.setViewportView(txtMensaje);
		
		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Funcion fun = new Funcion();
				if(fun.envioSms(txtMensaje.getText(), txtTitulo.getText(), txtNumero.getText())==true){
					JOptionPane.showMessageDialog(EnvioSms.this,"MENSAJE ENVIADO CON EXITO");
				}else{
					JOptionPane.showMessageDialog(EnvioSms.this,"ERROR AL ENVIAR MENSAJE", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnEnviar.setFont(new Font("Arial", Font.PLAIN, 14));
		btnEnviar.setBounds(88, 337, 89, 23);
		contentPane.add(btnEnviar);
		
		JButton btnSalir = new JButton("Salir");
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnSalir.setFont(new Font("Arial", Font.PLAIN, 14));
		btnSalir.setBounds(242, 337, 89, 23);
		contentPane.add(btnSalir);
	}
}
