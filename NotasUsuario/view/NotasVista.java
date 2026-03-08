package view;

import model.Nota;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class NotasVista extends JFrame {

    // Campo de búsqueda
    JTextField campoBusqueda = new JTextField();

    // Lista de notas
    DefaultListModel<Nota> modeloLista = new DefaultListModel<>();
    JList<Nota> listaNotas = new JList<>(modeloLista);

    // Formulario de nota
    JTextField campoTitulo    = new JTextField();
    JTextArea  campoContenido = new JTextArea(6, 20);

    // Botones de gestión de notas
    JButton btnCrear      = new JButton("Crear nota");
    JButton btnEditar     = new JButton("Guardar cambios");
    JButton btnEliminar   = new JButton("Eliminar nota");
    JButton btnLimpiar    = new JButton("Limpiar campos");
    JButton btnBorrarTodo = new JButton("Borrar TODAS");

    // Botón de cerrar sesión (en la cabecera)
    JButton btnLogout = new JButton("Cerrar sesión");

    // Etiqueta con el nombre del usuario logueado
    JLabel etiquetaUsuario = new JLabel("Usuario: -");

    // Barra de estado y log
    JLabel   etiquetaMensaje = new JLabel("Bienvenido");
    JTextArea areaLog        = new JTextArea(4, 40);

    public NotasVista() {
        setTitle("Gestor de Notas MVC");
        setSize(860, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        construirNorte();
        construirCentro();
        construirEste();
        construirSur();
    }

    private void construirNorte() {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setBackground(new Color(52, 73, 94));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));

        JLabel titulo = new JLabel("Gestor de Notas");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);

        etiquetaUsuario.setForeground(new Color(189, 195, 199));
        etiquetaUsuario.setFont(new Font("SansSerif", Font.PLAIN, 12));

        btnLogout.setBackground(new Color(192, 57, 43));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setOpaque(true);
        btnLogout.setFont(new Font("SansSerif", Font.PLAIN, 11));

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        derecha.setOpaque(false);
        derecha.add(etiquetaUsuario);
        derecha.add(btnLogout);

        JPanel busqueda = new JPanel(new BorderLayout(5, 0));
        busqueda.setOpaque(false);
        JLabel lblBuscar = new JLabel("Buscar: ");
        lblBuscar.setForeground(Color.WHITE);
        campoBusqueda.setPreferredSize(new Dimension(180, 26));
        busqueda.add(lblBuscar,     BorderLayout.WEST);
        busqueda.add(campoBusqueda, BorderLayout.CENTER);

        JPanel centro = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centro.setOpaque(false);
        centro.add(busqueda);

        panel.add(titulo,  BorderLayout.WEST);
        panel.add(centro,  BorderLayout.CENTER);
        panel.add(derecha, BorderLayout.EAST);

        add(panel, BorderLayout.NORTH);
    }

    private void construirCentro() {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(210);

        listaNotas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollLista = new JScrollPane(listaNotas);
        JPanel panelLista = new JPanel(new BorderLayout());
        panelLista.setBorder(BorderFactory.createTitledBorder("Mis notas"));
        panelLista.add(scrollLista, BorderLayout.CENTER);

        JPanel panelForm = new JPanel(new BorderLayout(5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Contenido de la nota"));

        JPanel fila = new JPanel(new BorderLayout(5, 0));
        fila.add(new JLabel("Título:   "), BorderLayout.WEST);
        fila.add(campoTitulo, BorderLayout.CENTER);

        campoContenido.setLineWrap(true);
        campoContenido.setWrapStyleWord(true);
        JScrollPane scrollContenido = new JScrollPane(campoContenido);

        panelForm.add(fila,            BorderLayout.NORTH);
        panelForm.add(scrollContenido, BorderLayout.CENTER);

        split.setLeftComponent(panelLista);
        split.setRightComponent(panelForm);

        add(split, BorderLayout.CENTER);
    }

    private void construirEste() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 5, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));

        panel.add(btnCrear);
        panel.add(btnEditar);
        panel.add(btnEliminar);
        panel.add(btnLimpiar);
        panel.add(btnBorrarTodo);

        btnBorrarTodo.setBackground(new Color(192, 57, 43));
        btnBorrarTodo.setForeground(Color.WHITE);
        btnBorrarTodo.setOpaque(true);

        add(panel, BorderLayout.EAST);
    }

    private void construirSur() {
        JPanel panel = new JPanel(new BorderLayout(0, 4));

        etiquetaMensaje.setFont(new Font("SansSerif", Font.BOLD, 12));
        etiquetaMensaje.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        areaLog.setEditable(false);
        areaLog.setBackground(new Color(40, 40, 40));
        areaLog.setForeground(new Color(0, 220, 0));
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder("Registro de acciones (log)"));

        panel.add(etiquetaMensaje, BorderLayout.NORTH);
        panel.add(scrollLog,       BorderLayout.CENTER);

        add(panel, BorderLayout.SOUTH);
    }

    public void mostrarNotas(ArrayList<Nota> notas) {
        modeloLista.clear();
        for (int i = 0; i < notas.size(); i++) {
            modeloLista.addElement(notas.get(i));
        }
    }

    public void cargarNota(Nota nota) {
        campoTitulo.setText(nota.getTitulo());
        campoContenido.setText(nota.getContenido());
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

    public void mostrarError(String mensaje) {
        mostrarMensaje("ERROR: " + mensaje, Color.RED);
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.WARNING_MESSAGE);
    }

    public boolean pedirConfirmacion(String mensaje, String titulo) {
        int r = JOptionPane.showConfirmDialog(this, mensaje, titulo,
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        return r == JOptionPane.YES_OPTION;
    }

    // Actualiza la etiqueta con el nombre del usuario logueado
    public void setNombreUsuario(String nombre) {
        etiquetaUsuario.setText("Usuario: " + nombre);
        setTitle("Gestor de Notas MVC " + nombre);
    }


    public String getTitulo(){ return campoTitulo.getText().trim(); }
    public String getContenido(){ return campoContenido.getText().trim(); }
    public String getBusqueda(){ return campoBusqueda.getText().trim(); }
    public Nota   getNotaSeleccionada(){ return listaNotas.getSelectedValue(); }
    public JList<Nota>  getListaNotas(){ return listaNotas; }
    public JTextField   getCampoBusqueda() { return campoBusqueda; }
}