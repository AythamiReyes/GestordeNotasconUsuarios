package model;

import java.io.*;
import java.util.ArrayList;

public class GestorFicheros {

    public static void guardarUsuarios(ArrayList<Usuario> usuarios) {
        try {
            FileWriter escritor = new FileWriter(FICHERO_USUARIOS);
            BufferedWriter bw   = new BufferedWriter(escritor);

            for (int i = 0; i < usuarios.size(); i++) {
                Usuario u = usuarios.get(i);
                bw.write(u.getNombre() + SEPARADOR + u.getHashContrasena());
                bw.newLine(); // salto de línea entre usuarios
            }

            bw.close();

        } catch (IOException e) {
            System.out.println("No se pudo guardar usuarios: " + e.getMessage());
        }
    }

    public static ArrayList<Usuario> cargarUsuarios() {
        ArrayList<Usuario> lista = new ArrayList<>();

        File fichero = new File(FICHERO_USUARIOS);
        if (!fichero.exists()) {
            return lista; // el fichero aún no existe, devolvemos lista vacía
        }

        try {
            FileReader lector = new FileReader(FICHERO_USUARIOS);
            BufferedReader br = new BufferedReader(lector);

            String linea;
            // readLine() devuelve null cuando llega al final del fichero
            while ((linea = br.readLine()) != null) {
                if (!linea.isEmpty()) {
                    // Separamos la línea en partes usando el separador
                    String[] partes = linea.split("\\|\\|"); // \|\| es || en regex
                    if (partes.length == 2) {
                        lista.add(new Usuario(partes[0], partes[1]));
                    }
                }
            }

            br.close();

        } catch (IOException e) {
            System.out.println("No se pudo cargar usuarios: " + e.getMessage());
        }

        return lista;
    }

    public static void guardarNotas(ArrayList<Nota> notas) {
        try {
            FileWriter escritor = new FileWriter(FICHERO_NOTAS);
            BufferedWriter bw   = new BufferedWriter(escritor);

            for (int i = 0; i < notas.size(); i++) {
                Nota n = notas.get(i);

                // Reemplazamos saltos de línea por <BR> para que quepa en una línea
                String contenidoSeguro = n.getContenido().replace("\n", "<BR>");

                bw.write(n.getPropietario() + SEPARADOR + n.getTitulo() + SEPARADOR + contenidoSeguro);
                bw.newLine();
            }

            bw.close();

        } catch (IOException e) {
            System.out.println("No se pudo guardar notas: " + e.getMessage());
        }
    }

    public static ArrayList<Nota> cargarNotas() {
        ArrayList<Nota> lista = new ArrayList<>();

        File fichero = new File(FICHERO_NOTAS);
        if (!fichero.exists()) {
            return lista;
        }

        try {
            FileReader lector = new FileReader(FICHERO_NOTAS);
            BufferedReader br = new BufferedReader(lector);

            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.isEmpty()) {
                    String[] partes = linea.split("\\|\\|", 3); // máximo 3 partes
                    if (partes.length == 3) {
                        // Recuperamos los saltos de línea originales
                        String contenido = partes[2].replace("<BR>", "\n");
                        lista.add(new Nota(partes[1], contenido, partes[0]));
                    }
                }
            }

            br.close();

        } catch (IOException e) {
            System.out.println("No se pudo cargar notas: " + e.getMessage());
        }

        return lista;
    }
}