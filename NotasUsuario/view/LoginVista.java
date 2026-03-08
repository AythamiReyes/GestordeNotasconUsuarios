package NotasUsuario.view;

import java.awt.*;
import javax.swing.*;

// LoginVista es la ventana de inicio de sesión y registro
// Se muestra antes que la ventana principal
public class LoginVista extends JDialog {

    // Campos de texto
    JTextField     campoNombre     = new JTextField(18);
    JPasswordField campoContrasena = new JPasswordField(18);
    // JPasswordField es igual que JTextField pero oculta lo que escribes con *

    // Botones
    JButton btnLogin     = new JButton("Iniciar sesión");
    JButton btnRegistrar = new JButton("Registrarse");

    // Etiqueta para mostrar errores dentro del diálogo
    JLabel etiquetaError = new JLabel(" ");

    // Constructor: recibe la ventana principal como "padre" para centrarse sobre ella
    public LoginVista(JFrame padre) {
        // true = modal, bloquea la ventana padre hasta que se cierre este diálogo
        super(padre, "Gestor de Notas Acceso", true);

        setSize(320, 240);
        setLocationRelativeTo(padre);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        construirUI();
    }

    private void construirUI() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Título del diálogo
        JLabel titulo = new JLabel("Iniciar sesión o registrarse", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(titulo, BorderLayout.NORTH);

        // Panel central con los campos
        JPanel campos = new JPanel(new GridLayout(2, 2, 8, 10));
        campos.add(new JLabel("Usuario:"));
        campos.add(campoNombre);
        campos.add(new JLabel("Contraseña:"));
        campos.add(campoContrasena);
        panel.add(campos, BorderLayout.CENTER);

        // Panel inferior con los botones y la etiqueta de error
        JPanel sur = new JPanel(new BorderLayout(5, 5));

        JPanel botones = new JPanel(new GridLayout(1, 2, 8, 0));
        btnLogin.setBackground(new Color(41, 128, 185));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setOpaque(true);
        botones.add(btnLogin);
        botones.add(btnRegistrar);

        etiquetaError.setForeground(Color.RED);
        etiquetaError.setHorizontalAlignment(SwingConstants.CENTER);
        etiquetaError.setFont(new Font("SansSerif", Font.PLAIN, 11));

        sur.add(botones,        BorderLayout.CENTER);
        sur.add(etiquetaError,  BorderLayout.SOUTH);

        panel.add(sur, BorderLayout.SOUTH);

        add(panel);
    }

    // --- Métodos que usa el Controlador ---

    // Devuelve el nombre escrito (sin espacios)
    public String getNombre() {
        return campoNombre.getText().trim();
    }

    // Devuelve la contraseña escrita como String
    // getPassword() devuelve char[] por seguridad; lo convertimos a String
    public String getContrasena() {
        return new String(campoContrasena.getPassword());
    }

    // Muestra un mensaje de error en rojo dentro del diálogo
    public void mostrarError(String mensaje) {
        etiquetaError.setText(mensaje);
    }

    // Limpia los campos y el error
    public void limpiar() {
        campoNombre.setText("");
        campoContrasena.setText("");
        etiquetaError.setText(" ");
        campoNombre.requestFocus();
    }

    // Expone los botones para que el Controlador les ponga los ActionListener
    public JButton getBtnLogin()     { return btnLogin;     }
    public JButton getBtnRegistrar() { return btnRegistrar; }
}