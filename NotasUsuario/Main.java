import controller.NotasControlador;
import model.NotasModelo;
import view.NotasVista;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                NotasModelo modelo = new NotasModelo();

                NotasVista vista = new NotasVista();

                NotasControlador controlador = new NotasControlador(modelo, vista);
            }
        });
    }
}