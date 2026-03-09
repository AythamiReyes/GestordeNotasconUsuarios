package NotasUsuario.controller;

import NotasUsuario.model.Nota;
import NotasUsuario.model.NotasModelo;
import NotasUsuario.view.LoginVista;
import NotasUsuario.view.NotasVista;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// Conecta el Modelo con la Vista
public class NotasControlador {

    private NotasModelo modelo;
    private NotasVista  vista;

    public NotasControlador(NotasModelo modelo, NotasVista vista) {
        this.modelo = modelo;
        this.vista = vista;
        conectarBotonesNotas();
        mostrarLogin();
    }


    private void mostrarLogin() {
        LoginVista login = new LoginVista(vista);

        login.getBtnLogin().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombre = login.getNombre();
                String pass = login.getContrasena();
                if (nombre.isEmpty() || pass.isEmpty()) {
                    login.mostrarError("Rellena todos los campos.");
                    return;
                }
                if (modelo.login(nombre, pass)) {
                    login.dispose();
                    abrirVentanaPrincipal();
                } else {
                    login.mostrarError("Usuario o contraseña incorrectos.");
                    vista.agregarLog("[LOGIN] Intento fallido: " + nombre);
                }
            }
        });

        login.getBtnRegistrar().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombre = login.getNombre();
                String pass = login.getContrasena();
                if (nombre.isEmpty() || pass.isEmpty()) {
                    login.mostrarError("Rellena todos los campos.");
                    return;
                }
                try {
                    modelo.registrar(nombre, pass);
                    modelo.login(nombre, pass);
                    login.dispose();
                    abrirVentanaPrincipal();
                    vista.agregarLog("[REGISTRO] Nuevo usuario: " + nombre);
                } catch (IllegalArgumentException ex) {
                    login.mostrarError(ex.getMessage());
                }
            }
        });

        login.setVisible(true);
    }

    private void abrirVentanaPrincipal() {
        String nombre = modelo.getUsuarioActual().getNombre();
        vista.setNombreUsuario(nombre);
        vista.vaciarFormulario();
        refrescarLista();
        vista.mostrarMensaje("Bienvenido, " + nombre + ". Tienes " + modelo.totalNotas() + " nota(s).", Color.DARK_GRAY);
        vista.agregarLog("[LOGIN] Sesión iniciada: " + nombre);
        vista.setVisible(true);
    }


    private void conectarBotonesNotas() {

        vista.btnCrear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { crearNota(); }
        });
        vista.btnEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { editarNota(); }
        });
        vista.btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { eliminarNota(); }
        });
        vista.btnLimpiar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { limpiarCampos(); }
        });
        vista.btnBorrarTodo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { borrarTodas(); }
        });
        vista.btnLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { cerrarSesion(); }
        });

        vista.getListaNotas().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) seleccionarNota();
            }
        });

        vista.getCampoBusqueda().getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { refrescarLista(); }
            public void removeUpdate(DocumentEvent e)  { refrescarLista(); }
            public void changedUpdate(DocumentEvent e) { refrescarLista(); }
        });
    }


    private void seleccionarNota() {
        Nota n = vista.getNotaSeleccionada();
        if (n != null) {
            vista.cargarNota(n);
            vista.mostrarMensaje("Nota seleccionada: " + n.getTitulo(), Color.BLUE);
            vista.agregarLog("[SELECCIONAR] " + n.getTitulo());
        }
    }

    private void crearNota() {
        try {
            Nota n = modelo.crearNota(vista.getTitulo(), vista.getContenido());
            refrescarLista();
            vista.vaciarFormulario();
            vista.mostrarMensaje("Nota creada: " + n.getTitulo(), new Color(0, 128, 0));
            vista.agregarLog("[CREAR] " + n.getTitulo());
        } catch (IllegalArgumentException ex) {
            vista.mostrarError(ex.getMessage());
            vista.agregarLog("[ERROR] " + ex.getMessage());
        }
    }

    private void editarNota() {
        Nota n = vista.getNotaSeleccionada();
        if (n == null) { // sin selección
            vista.mostrarError("Selecciona una nota para editarla.");
            vista.agregarLog("[ERROR] Edición sin nota seleccionada.");
            return;
        }
        try {
            String anterior = n.getTitulo();
            modelo.editarNota(n, vista.getTitulo(), vista.getContenido());
            refrescarLista();
            vista.vaciarFormulario();
            vista.mostrarMensaje("Nota editada correctamente.", Color.BLUE);
            vista.agregarLog("[EDITAR] '" + anterior + "' → '" + n.getTitulo() + "'");
        } catch (IllegalArgumentException ex) {
            vista.mostrarError(ex.getMessage());
            vista.agregarLog("[ERROR] " + ex.getMessage());
        }
    }

    private void eliminarNota() {
        Nota n = vista.getNotaSeleccionada();
        if (n == null) { // sin selección
            vista.mostrarError("Selecciona una nota para eliminarla.");
            vista.agregarLog("[ERROR] Eliminación sin nota seleccionada.");
            return;
        }
        if (vista.pedirConfirmacion("¿Eliminar '" + n.getTitulo() + "'?", "Confirmar")) {
            String titulo = n.getTitulo();
            modelo.eliminarNota(n);
            refrescarLista();
            vista.vaciarFormulario();
            vista.mostrarMensaje("Nota eliminada: " + titulo, Color.RED);
            vista.agregarLog("[ELIMINAR] " + titulo);
        } else {
            vista.agregarLog("[INFO] Eliminación cancelada.");
        }
    }

    private void limpiarCampos() {
        vista.vaciarFormulario();
        vista.mostrarMensaje("Campos limpiados. Las notas no han cambiado.", Color.GRAY);
        vista.agregarLog("[LIMPIAR] Campos vaciados.");
    }

    private void borrarTodas() {
        if (modelo.totalNotas() == 0) {
            vista.mostrarError("No hay notas que borrar.");
            return;
        }
        if (!vista.pedirConfirmacion(
                "Vas a borrar TODAS tus notas (" + modelo.totalNotas() + ").\nEsta acción no se puede deshacer. ¿Continuar?",
                "ADVERTENCIA")) {
            vista.agregarLog("[INFO] Borrar todo cancelado.");
            return;
        }
        if (!vista.pedirConfirmacion("¿Confirmas que quieres borrarlas todas?", "Confirmación final")) {
            vista.agregarLog("[INFO] Borrar todo cancelado.");
            return;
        }
        int cantidad = modelo.totalNotas();
        modelo.eliminarTodasMisNotas();
        refrescarLista();
        vista.vaciarFormulario();
        vista.mostrarMensaje("Se borraron " + cantidad + " notas.", Color.RED);
        vista.agregarLog("[BORRAR TODO] " + cantidad + " notas eliminadas.");
    }

    private void cerrarSesion() {
        String nombre = modelo.getUsuarioActual().getNombre();
        modelo.logout();
        vista.setVisible(false);
        vista.mostrarNotas(new ArrayList<>());
        vista.vaciarFormulario();
        vista.agregarLog("[LOGOUT] Sesión cerrada: " + nombre);
        mostrarLogin();
    }

    private void refrescarLista() {
        vista.mostrarNotas(modelo.buscar(vista.getBusqueda()));
    }
}