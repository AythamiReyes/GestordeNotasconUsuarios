package NotasUsuario.model;

import java.io.*;
import java.util.ArrayList;

// Guarda y carga datos en ficheros
public class GestorFicheros {

    private static final String FICHERO_USUARIOS = "usuarios.txt";
    private static final String FICHERO_NOTAS = "notas.txt";
    private static final String SEP = "||";


    public static void guardarUsuarios(ArrayList<Usuario> lista) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FICHERO_USUARIOS));
            for (int i = 0; i < lista.size(); i++) {
                Usuario u = lista.get(i);
                bw.write(u.getNombre() + SEP + u.getHashContrasena());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Error al guardar usuarios: " + e.getMessage());
        }
    }

    public static ArrayList<Usuario> cargarUsuarios() {
        ArrayList<Usuario> lista = new ArrayList<>();
        File f = new File(FICHERO_USUARIOS);
        if (!f.exists()) return lista;

        try {
            BufferedReader br = new BufferedReader(new FileReader(FICHERO_USUARIOS));
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|\\|");
                if (partes.length == 2) lista.add(new Usuario(partes[0], partes[1]));
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error al cargar usuarios: " + e.getMessage());
        }
        return lista;
    }


    public static void guardarNotas(ArrayList<Nota> lista) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FICHERO_NOTAS));
            for (int i = 0; i < lista.size(); i++) {
                Nota n = lista.get(i);
                String contenido = n.getContenido().replace("\n", "<BR>");
                bw.write(n.getPropietario() + SEP + n.getTitulo() + SEP + contenido);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Error al guardar notas: " + e.getMessage());
        }
    }

    public static ArrayList<Nota> cargarNotas() {
        ArrayList<Nota> lista = new ArrayList<>();
        File f = new File(FICHERO_NOTAS);
        if (!f.exists()) return lista;

        try {
            BufferedReader br = new BufferedReader(new FileReader(FICHERO_NOTAS));
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|\\|", 3);
                if (partes.length == 3) {
                    String contenido = partes[2].replace("<BR>", "\n");
                    lista.add(new Nota(partes[1], contenido, partes[0]));
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error al cargar notas: " + e.getMessage());
        }
        return lista;
    }
}