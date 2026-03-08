package NotasUsuario.model;

public class Usuario {

    private String nombre;
    private String hashContrasena;

    public Usuario(String nombre, String hashContrasena){
        this.nombre = nombre;
        this.hashContrasena = hashContrasena;
    }

    public String getNombre(){
        return nombre;
    }

    public String getHashContrasena(){
        return hashContrasena;
    }

    public void setHashContrasena(String hashContrasena){
        this.hashContrasena = hashContrasena;
    }
}