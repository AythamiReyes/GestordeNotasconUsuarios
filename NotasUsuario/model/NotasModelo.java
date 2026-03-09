package NotasUsuario.model;

import java.security.MessageDigest;
import java.util.ArrayList;

// Lógica de la aplicación: usuarios, hash y notas
public class NotasModelo {

    private ArrayList<Usuario> usuarios;
    private ArrayList<Nota> notas;
    private Usuario usuarioActual;

    public NotasModelo() {
        usuarios = GestorFicheros.cargarUsuarios();
        notas = GestorFicheros.cargarNotas();
        usuarioActual = null;
    }

    // SHA-256: guarda la huella de la contraseña, no el texto real
    public String hashear(String contrasena) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(contrasena.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02x", bytes[i]));
            }
            return sb.toString();
        } catch (Exception e) {
            return contrasena;
        }
    }


    public void registrar(String nombre, String contrasena) {
        if (nombre.isEmpty())
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        if (contrasena.length() < 4)
            throw new IllegalArgumentException("La contraseña debe tener al menos 4 caracteres.");
        if (existeUsuario(nombre))
            throw new IllegalArgumentException("Ese nombre de usuario ya existe.");

        usuarios.add(new Usuario(nombre, hashear(contrasena)));
        GestorFicheros.guardarUsuarios(usuarios);
    }

    public boolean login(String nombre, String contrasena) {
        String hash = hashear(contrasena);
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.get(i);
            if (u.getNombre().equals(nombre) && u.getHashContrasena().equals(hash)) {
                usuarioActual = u;
                return true;
            }
        }
        return false;
    }

    public void logout() { usuarioActual = null; }

    public Usuario getUsuarioActual() { return usuarioActual; }

    private boolean existeUsuario(String nombre) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getNombre().equalsIgnoreCase(nombre)) return true;
        }
        return false;
    }

    // NOTAS

    public ArrayList<Nota> buscar(String filtro) {
        ArrayList<Nota> resultado = new ArrayList<>();
        if (usuarioActual == null) return resultado;

        for (int i = 0; i < notas.size(); i++) {
            Nota n = notas.get(i);
            boolean esMia = n.getPropietario().equals(usuarioActual.getNombre());
            boolean coincide = filtro.isEmpty() || n.getTitulo().toLowerCase().contains(filtro.toLowerCase());
            if (esMia && coincide) resultado.add(n);
        }
        return resultado;
    }

    public Nota crearNota(String titulo, String contenido) {
        if (titulo.isEmpty()) throw new IllegalArgumentException("El título no puede estar vacío.");
        Nota n = new Nota(titulo, contenido, usuarioActual.getNombre());
        notas.add(n);
        GestorFicheros.guardarNotas(notas);
        return n;
    }

    public void editarNota(Nota nota, String titulo, String contenido) {
        if (titulo.isEmpty()) throw new IllegalArgumentException("El título no puede estar vacío.");
        nota.setTitulo(titulo);
        nota.setContenido(contenido);
        GestorFicheros.guardarNotas(notas);
    }

    public void eliminarNota(Nota nota) {
        notas.remove(nota);
        GestorFicheros.guardarNotas(notas);
    }

    public void eliminarTodasMisNotas() {
        for (int i = notas.size() - 1; i >= 0; i--) {
            if (notas.get(i).getPropietario().equals(usuarioActual.getNombre())) notas.remove(i);
        }
        GestorFicheros.guardarNotas(notas);
    }

    public int totalNotas() { return buscar("").size(); }
}