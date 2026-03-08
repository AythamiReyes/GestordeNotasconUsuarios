package NotasUsuario.view;

import NotasUsuario.model.Nota;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

// Ventana principal
public class NotasVista extends JFrame {

    JLabel     etiquetaUsuario = new JLabel("Usuario: -");
    JTextField campoBusqueda   = new JTextField(15);
    public JButton    btnLogout       = new JButton("Cerrar sesión");

    DefaultListModel<Nota> modeloLista = new DefaultListModel<>();
    JList<Nota>            listaNotas  = new JList<>(modeloLista);

    JTextField campoTitulo    = new JTextField();
    JTextArea  campoContenido = new JTextArea(8, 25);

    public JButton btnCrear      = new JButton("Crear nota");
    public JButton btnEditar     = new JButton("Guardar cambios");
    public JButton btnEliminar   = new JButton("Eliminar nota");
    public JButton btnLimpiar    = new JButton("Limpiar campos");
    public JButton btnBorrarTodo = new JButton("Borrar TODAS");

    JLabel    etiquetaMensaje = new JLabel("Bienvenido");
    JTextArea areaLog         = new JTextArea(3, 0);

    public NotasVista() {
        setTitle("Gestor de Notas MVC");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));
        add(zonaNorte(),  BorderLayout.NORTH);
        add(zonaCentro(), BorderLayout.CENTER);
        add(zonaEste(),   BorderLayout.EAST);
        add(zonaSur(),    BorderLayout.SOUTH);
    }

    private JPanel zonaNorte() {
        JPanel panel    = new JPanel(new BorderLayout(10, 0));
        JPanel busqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        busqueda.add(new JLabel("Buscar:"));
        busqueda.add(campoBusqueda);
        panel.add(etiquetaUsuario, BorderLayout.WEST);
        panel.add(busqueda,        BorderLayout.CENTER);
        panel.add(btnLogout,       BorderLayout.EAST);
        return panel;
    }

    private JSplitPane zonaCentro() {
        listaNotas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JPanel izquierda = new JPanel(new BorderLayout());
        izquierda.setBorder(BorderFactory.createTitledBorder("Mis notas"));
        izquierda.add(new JScrollPane(listaNotas), BorderLayout.CENTER);

        campoContenido.setLineWrap(true);
        campoContenido.setWrapStyleWord(true);
        JPanel filaTitulo = new JPanel(new BorderLayout(5, 0));
        filaTitulo.add(new JLabel("Título:  "), BorderLayout.WEST);
        filaTitulo.add(campoTitulo,              BorderLayout.CENTER);
        JPanel derecha = new JPanel(new BorderLayout(5, 5));
        derecha.setBorder(BorderFactory.createTitledBorder("Contenido de la nota"));
        derecha.add(filaTitulo,                      BorderLayout.NORTH);
        derecha.add(new JScrollPane(campoContenido), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, izquierda, derecha);
        split.setDividerLocation(200);
        return split;
    }

    private JPanel zonaEste() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 0, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));
        panel.add(btnCrear);
        panel.add(btnEditar);
        panel.add(btnEliminar);
        panel.add(btnLimpiar);
        panel.add(btnBorrarTodo);
        btnBorrarTodo.setBackground(new Color(192, 57, 43));
        btnBorrarTodo.setForeground(Color.WHITE);
        btnBorrarTodo.setOpaque(true);
        return panel;
    }

    private JPanel zonaSur() {
        JPanel panel = new JPanel(new BorderLayout(0, 3));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
        etiquetaMensaje.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        areaLog.setEditable(false);
        areaLog.setBackground(new Color(40, 40, 40));
        areaLog.setForeground(new Color(0, 220, 0));
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder("Registro de acciones (log)"));
        panel.add(etiquetaMensaje, BorderLayout.NORTH);
        panel.add(scrollLog,       BorderLayout.CENTER);
        return panel;
    }


    public void mostrarNotas(ArrayList<Nota> notas) {
        modeloLista.clear();
        for (int i = 0; i < notas.size(); i++) modeloLista.addElement(notas.get(i));
    }

    public void cargarNota(Nota n) {
        campoTitulo.setText(n.getTitulo());
        campoContenido.setText(n.getContenido());
    }

    public void vaciarFormulario() {
        campoTitulo.setText("");
        campoContenido.setText("");
        listaNotas.clearSelection();
        campoTitulo.requestFocus();
    }

    public void mostrarMensaje(String texto, Color color) {
        etiquetaMensaje.setText(texto);
        etiquetaMensaje.setForeground(color);
    }

    public void agregarLog(String texto) {
        areaLog.append(texto + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }

    public void mostrarError(String msg) {
        mostrarMensaje("ERROR: " + msg, Color.RED);
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.WARNING_MESSAGE);
    }

    public boolean pedirConfirmacion(String msg, String titulo) {
        return JOptionPane.showConfirmDialog(this, msg, titulo,
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    }

    public void setNombreUsuario(String nombre) {
        etiquetaUsuario.setText("Usuario: " + nombre);
        setTitle("Gestor de Notas — " + nombre);
    }

    public String getTitulo()           { return campoTitulo.getText().trim(); }
    public String getContenido()        { return campoContenido.getText().trim(); }
    public String getBusqueda()         { return campoBusqueda.getText().trim(); }
    public Nota   getNotaSeleccionada() { return listaNotas.getSelectedValue(); }
    public JList<Nota> getListaNotas()     { return listaNotas; }
    public JTextField  getCampoBusqueda()  { return campoBusqueda; }
}