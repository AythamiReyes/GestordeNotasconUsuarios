package NotasUsuario;

import NotasUsuario.controller.NotasControlador;
import NotasUsuario.model.NotasModelo;
import NotasUsuario.view.NotasVista;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

                NotasModelo modelo = new NotasModelo();

                NotasVista vista = new NotasVista();

                new NotasControlador(modelo, vista);
        });
    }
}