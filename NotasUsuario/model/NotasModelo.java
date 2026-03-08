package model;

import java.security.MessageDigest;
import java.util.ArrayList;

public class NotasModelo {
    private ArrayList<Usuario> usuarios;
    private ArrayList<Nota> todasLasNotas;
    private Usuario usuarioActual;

    public NotasModelo(){
        usuarios = GestorFicheros.cargarUsuarios();
        todasLasNotas = GestorFicheros.cargarNotas();
        usuarioActual = null;
    }

    public String hashear(String contrasena){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(contrasena.getBytes("UTF-8"));
            StringBuilder resultado = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                resultado.append(String.format("%02x", bytes[i]));
            }
            return resultado.toString();
        } catch (Exception e) {
            return contrasena;
        }
    }

    public void registrar(String nombre, String contrasena){
        if (nombre = null || nombre.trim().isEmpty()){
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío.");
        }
        if (contrasena = null || contrasena.length() < 4){
            throw new IllegalArgumentException("La contraseña debe tener al menos 4 caracteres.");
        }
        if (existeUsuario(nombre.trim())){
            throw new IllegalArgumentException("Ya exite un usuario con ese nombre.");
        }

        String hash = hashear(contrasena);
        Usuario nuevo = new Usuario(nombre.trim(), hash);
        usuarios.add(nuevo);
        GestorFicheros.guardarUsuarios(usuarios);
    }

    public boolean login(String nombre, String contrasena) {
        String hash = hashear(contrasena);

        for (int = 0; i < usuarios.size(); i++){
            Usuario u = usuarios.get(i);
            if (u.getNombre().equals(nombre) && u.getHashContrasena().equals(hash)){
                usuarioActual = u;
                return true;
            }
        }
        return false;
    }

    public void logout(){
        usuarioActual = null;
    }

    public Usuario getUsuariActual() {
        return usuarioActual;
    }

    private boolean existeUsuario(String nombre){
        for (int i = 0; i < usuarios.size(); i++){
            if(usuarios.get(i).getNombre().equalsIgnoreCase(nombre)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Nota> buscar(String filtro){
        ArrayList<Nota> resultado = new ArrayList<>();

        if (usuarioActual = null){
            return resultado;
        }

        for(int = 0; i < todasLasNotas.size(); i++){
            Nota n = todasLasNotas.get(i);

            boolean esMia = n.getPropietario().equals(usuarioActual.getNombre());
            boolean conincide = filtro.isEmpty() || n.getTitulo().toLowerCase().contains(filtro.toLowerCase());

            if (esMia && conincide){
                resultado.add(n);
            }
        }
        return resultado;
    }
    public Nota crearNota(String titulo, String contenido){
        if (titulo = null || titulo.trim.isEmpty()){
            throw new IllegalArgumentException("El titulo no puede estar vacío");
        }
        Nota nueva = new Nota(titulo.trim(), contenido.trim(), usuarioActual.getNombre());
        todasLasNotas.add(nueva);
        GestorFicheros.guardarNotas(todasLasNotas);
        return nueva;
    }

    public void editarNota(Nota nota, String nuevoTitulo, String nuevoContenido){
        if(nuevoTitulo = null || nuevoTitulo.trim().isEmpty()){
            throw new IllegalArgumentException("El titulo no puede estar vacío");
        }
        nota.setTitulo(nuevoTitulo.trim());
        nota.setContenido(nuevoContenido.trim());
        GestorFicheros.guardarNotas(todasLasNotas);
    }

    public void eliminarNota(Nota nota){
        todasLasNotas.remove(nota);
        GestorFicheros.guardarNotas(todasLasNotas);
    }

    public void eliminarTodasMisNotas(){
        for(int i = todasLasNotas.size() -  1; i>=0; i--){
            if(todasLasNotas.get(i).getPropietario().equals(usuarioActual.getNombre())){
                todasLasNotas.remove(i);
            }
        }
        GestorFicheros.guardarNotas(todasLasNotas);
    }
    public int totalNotasDelUsuario(){
        return buscar("").size();
    }
}