package controller;

import model.Nota;
import model.NotasModelo;
import view.LoginVista;
import view.NotasVista;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class NotasControlador {

    private NotasModelo modelo;
    private NotasVista  vista;
    private LoginVista  loginVista;

    public NotasControlador(NotasModelo modelo, NotasVista vista) {
        this.modelo = modelo;
        this.vista  = vista;

        conectarBotonesNotas();

        mostrarLogin();
    }

    public void mostrarLogin() {
        loginVista = new LoginVista(vista);
        conectarBotonesLogin();
        loginVista.setVisible(true);
    }

    private void conectarBotonesLogin() {

        loginVista.getBtnLogin().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hacerLogin();
            }
        });

        loginVista.getBtnRegistrar().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hacerRegistro();
            }
        });
    }

    private void hacerLogin() {
        String nombre     = loginVista.getNombre();
        String contrasena = loginVista.getContrasena();

        if (nombre.isEmpty() || contrasena.isEmpty()) {
            loginVista.mostrarError("Rellena todos los campos.");
            return;
        }

        boolean ok = modelo.login(nombre, contrasena);

        if (ok) {
            loginVista.dispose(); // cerramos el diálogo de login
            abrirVentanaPrincipal();
        } else {
            loginVista.mostrarError("Usuario o contraseña incorrectos.");
            vista.agregarLog("[LOGIN] Intento fallido para el usuario: " + nombre);
        }
    }

    private void hacerRegistro() {
        String nombre     = loginVista.getNombre();
        String contrasena = loginVista.getContrasena();

        if (nombre.isEmpty() || contrasena.isEmpty()) {
            loginVista.mostrarError("Rellena todos los campos.");
            return;
        }

        try {
            modelo.registrar(nombre, contrasena);
            // Tras registrarse hacemos login automáticamente
            modelo.login(nombre, contrasena);
            loginVista.dispose();
            abrirVentanaPrincipal();
            vista.agregarLog("[REGISTRO] Nuevo usuario registrado: " + nombre);

        } catch (IllegalArgumentException ex) {
            loginVista.mostrarError(ex.getMessage());
            vista.agregarLog("[ERROR] Registro fallido: " + ex.getMessage());
        }
    }

    private void abrirVentanaPrincipal() {
        String nombre = modelo.getUsuarioActual().getNombre();
        vista.setNombreUsuario(nombre);
        vista.vaciarFormulario();
        refrescarLista();
        vista.mostrarMensaje("Bienvenido, " + nombre + ". Tienes "
                + modelo.totalNotasDelUsuario() + " nota(s).", new Color(0, 100, 180));
        vista.agregarLog("[LOGIN] Sesión iniciada: " + nombre);
        vista.setVisible(true);
    }

    private void conectarBotonesNotas() {

        vista.btnCrear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                crearNota();
            }
        });

        vista.btnEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editarNota();
            }
        });

        vista.btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarNota();
            }
        });

        vista.btnLimpiar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });

        vista.btnBorrarTodo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                borrarTodas();
            }
        });

        vista.btnLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cerrarSesion();
            }
        });

        // Seleccionar una nota de la lista
        vista.getListaNotas().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    seleccionarNota();
                }
            }
        });

        // Escribir en el campo de búsqueda
        vista.getCampoBusqueda().getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { buscar(); }
            public void removeUpdate(DocumentEvent e)  { buscar(); }
            public void changedUpdate(DocumentEvent e) { buscar(); }
        });
    }

    private void seleccionarNota() {
        Nota nota = vista.getNotaSeleccionada();
        if (nota != null) {
            vista.cargarNota(nota);
            vista.mostrarMensaje("Nota seleccionada: " + nota.getTitulo(), Color.BLUE);
            vista.agregarLog("Nota cargada: " + nota.getTitulo());
        }
    }

    private void crearNota() {
        String titulo    = vista.getTitulo();
        String contenido = vista.getContenido();

        try {
            Nota nueva = modelo.crearNota(titulo, contenido);
            refrescarLista();
            vista.vaciarFormulario();
            vista.mostrarMensaje("Nota creada: " + nueva.getTitulo(), new Color(0, 128, 0));
            vista.agregarLog("Nueva nota: " + nueva.getTitulo());

        } catch (IllegalArgumentException ex) {
            vista.mostrarError(ex.getMessage());
            vista.agregarLog("Crear fallido: " + ex.getMessage());
        }
    }

    private void editarNota() {
        Nota seleccionada = vista.getNotaSeleccionada();

        // Caso atípico: editar sin seleccionar
        if (seleccionada == null) {
            vista.mostrarError("Selecciona una nota de la lista para editarla.");
            vista.agregarLog("Edición sin nota seleccionada.");
            return;
        }

        String nuevoTitulo    = vista.getTitulo();
        String nuevoContenido = vista.getContenido();

        try {
            String anterior = seleccionada.getTitulo();
            modelo.editarNota(seleccionada, nuevoTitulo, nuevoContenido);
            refrescarLista();
            vista.vaciarFormulario();
            vista.mostrarMensaje("Nota editada correctamente.", new Color(0, 100, 180));
            vista.agregarLog("[EDITAR] " + anterior + "' → '" + nuevoTitulo + "'");

        } catch (IllegalArgumentException ex) {
            vista.mostrarError(ex.getMessage());
            vista.agregarLog("Edición fallida: " + ex.getMessage());
        }
    }

    private void eliminarNota() {
        Nota seleccionada = vista.getNotaSeleccionada();

        // Caso atípico: eliminar sin seleccionar
        if (seleccionada == null) {
            vista.mostrarError("Selecciona una nota de la lista para eliminarla.");
            vista.agregarLog("Eliminación sin nota seleccionada.");
            return;
        }

        boolean confirma = vista.pedirConfirmacion(
                "¿Seguro que quieres eliminar:\n'" + seleccionada.getTitulo() + "'?",
                "Confirmar eliminación");

        if (confirma) {
            String titulo = seleccionada.getTitulo();
            modelo.eliminarNota(seleccionada);
            refrescarLista();
            vista.vaciarFormulario();
            vista.mostrarMensaje("Nota eliminada: " + titulo, Color.RED);
            vista.agregarLog("Nota borrada: " + titulo);
        } else {
            vista.mostrarMensaje("Eliminación cancelada.", Color.GRAY);
            vista.agregarLog("Eliminación cancelada por el usuario.");
        }
    }

    private void limpiarCampos() {
        vista.vaciarFormulario();
        vista.mostrarMensaje("Campos limpiados. Las notas no han cambiado.", Color.GRAY);
        vista.agregarLog("Campos del formulario vaciados.");
    }

    private void borrarTodas() {
        if (modelo.totalNotasDelUsuario() == 0) {
            vista.mostrarError("No tienes notas que borrar.");
            vista.agregarLog("Borrar todo: lista ya vacía.");
            return;
        }

        boolean primera = vista.pedirConfirmacion(
                "Vas a borrar TODAS tus notas (" + modelo.totalNotasDelUsuario() + " notas).\n"
                + "Esta acción NO se puede deshacer.\n\n¿Continuar?",
                "ADVERTENCIA - Borrar todas las notas");

        if (!primera) {
            vista.mostrarMensaje("Borrado cancelado.", Color.GRAY);
            vista.agregarLog("[INFO] Borrar todo cancelado en 1ª confirmación.");
            return;
        }

        // Segunda confirmación
        boolean segunda = vista.pedirConfirmacion(
                "¿Confirmas que quieres borrar TODAS tus notas para siempre?",
                "Confirmación final");

        if (segunda) {
            int cantidad = modelo.totalNotasDelUsuario();
            modelo.eliminarTodasMisNotas();
            refrescarLista();
            vista.vaciarFormulario();
            vista.mostrarMensaje("Se borraron " + cantidad + " notas.", Color.RED);
            vista.agregarLog("[BORRAR TODO] " + cantidad + " notas eliminadas.");
        } else {
            vista.mostrarMensaje("Borrado cancelado.", Color.GRAY);
            vista.agregarLog("[INFO] Borrar todo cancelado en 2ª confirmación.");
        }
    }

    private void buscar() {
        String texto = vista.getBusqueda();
        ArrayList<Nota> resultado = modelo.buscar(texto);
        vista.mostrarNotas(resultado);

        if (!texto.isEmpty()) {
            vista.agregarLog("[BUSCAR] '" + texto + "' → " + resultado.size() + " resultado(s).");
        }
    }

    private void cerrarSesion() {
        String nombre = modelo.getUsuarioActual().getNombre();
        modelo.logout();
        vista.setVisible(false);     // ocultamos la ventana principal
        vista.vaciarFormulario();
        vista.mostrarNotas(new ArrayList<>()); // vaciamos la lista en pantalla
        vista.agregarLog("[LOGOUT] Sesión cerrada: " + nombre);
        mostrarLogin();              // volvemos a mostrar el login
    }

    // Refresca la lista respetando el filtro activo
    private void refrescarLista() {
        ArrayList<Nota> notas = modelo.buscar(vista.getBusqueda());
        vista.mostrarNotas(notas);
    }
}