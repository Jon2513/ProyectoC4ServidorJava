package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Cliente implements Runnable{
    Color color;
    private Socket cliente;
    private DataInputStream entrada;
    private DataOutputStream salida;
    private int puerto=5000;
    private String ip="localhost";

    private String mensaje;
    private ClienteF ventana;

    private JLabel[][] tablero;
    private JButton[] jbutones;

    private JLabel jcolor,jgana;
    private Color rojo = Color.red;
    private Color amarillo = Color.yellow;
    private boolean turno;

    public Cliente(ClienteF ventana) {
        try{
            this.ventana = ventana;
            cliente = new Socket(ip, puerto);
            entrada = new DataInputStream(cliente.getInputStream());
            salida = new DataOutputStream(cliente.getOutputStream());
            tablero = ventana.getLabels();
            jcolor = ventana.getLabelC();
            jgana = ventana.getLabelG();
            jbutones= ventana.getBotones();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        try{
                mensaje=entrada.readUTF();
                String[] datos = mensaje.split("/");
                String partida = datos[0];
               if (partida.equals("R")) {
                jcolor.setIcon(new ImageIcon(getClass().getResource("/juegas_rojas.png")));
               }else if (partida.equals("A")) {
                   jcolor.setIcon(new ImageIcon(getClass().getResource("/juegas_amarillas.png")));
               }
                System.out.println(partida);
                turno = Boolean.parseBoolean(datos[1]);
            while(true){
                mensaje = entrada.readUTF();
                datos = mensaje.split("/");
                int ra=  Integer.parseInt(datos[0]);
                int r = Integer.parseInt(datos[1]);
                int a = Integer.parseInt(datos[2]);
                String ganador = datos[3];
                
                if (ra==1) {
                    color = rojo;
                    tablero[r][a].setBackground(color);
                    tablero[r][a].setIcon(new ImageIcon(getClass().getResource("/rojas.png")));
                } else {
                    color = amarillo;
                    tablero[r][a].setBackground(color);
                    tablero[r][a].setIcon(new ImageIcon(getClass().getResource("/amarilla.png")));
                }
                if (ganador.equals("NADIE")) {
                    if (turno) {
                        jgana.setText("ESPERA TURNO");
                    } else {

                        jgana.setText("JUEGAS TÚ");
                    }
                } else if (ganador.equals("R") && partida.equals("R")) {
                    jgana.setText("GANASTE");
                    JOptionPane.showMessageDialog(ventana, "Enhorabuena, has ganado");
                    for (int i = 0; i < 7; i++) {
                        jbutones[i].setEnabled(false);
                    }

                } else if (ganador.equals("A")&& partida.equals("R")) {
                    jgana.setText("PERDISTE");
                    JOptionPane.showMessageDialog(ventana, "Vaya, has perdido");
                    for (int i = 0; i < 7; i++) {
                        jbutones[i].setEnabled(false);
                    }
                } else if (ganador.equals("R") && partida.equals("A")) {
                    jgana.setText("PERDISTE");
                    JOptionPane.showMessageDialog(ventana, "Vaya, has perdido");
                    for (int i = 0; i < 7; i++) {
                        jbutones[i].setEnabled(false);
                    }
                } else if (ganador.equals("A")&& partida.equals("A")) {
                    jgana.setText("GANASTE");
                    JOptionPane.showMessageDialog(ventana, "Enhorabuena, has ganado");
                    for (int i = 0; i < 7; i++) {
                        jbutones[i].setEnabled(false);
                    }
                }
                else if (ganador.equals("EMPATE")) {
                    jgana.setText("EMPATE");
                    JOptionPane.showMessageDialog(ventana, "Empate");
                    for (int i = 0; i < 7; i++) {
                        jbutones[i].setEnabled(false);
                    }
                }
                turno=!turno;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

        public void enviarTurno(int x, int y) {
        try{
            if(turno){
                String datos="";
                datos = x + "/" + y;
                salida.writeUTF(datos);

            }else {
                JOptionPane.showMessageDialog(ventana, "No es tu turno");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
