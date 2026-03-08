package NotasUsuario.model;

public class Nota {

    private String titulo;
    private String contenido;
    private String propietario;

    public Nota(String titulo, String contenido, String propietario){
        this.titulo = titulo;
        this.contenido = contenido;
        this.propietario = propietario;
    }

    public String getTitulo(){
        return titulo;
    }

    public String getContenido(){
        return contenido;
    }

    public String getPropietario(){
        return propietario;
    }

    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

    public void setContenido(String contenido){
        this.contenido = contenido;
    }

    public void setPropietario(String propietario){
        this.propietario = propietario;
    }

    @Override
    public String toString(){
        return titulo;
    }

}