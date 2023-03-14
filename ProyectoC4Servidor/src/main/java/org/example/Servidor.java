package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class Servidor {
    public static void main(String[] args) throws IOException {
        Servidor servidor = new Servidor();
        servidor.observador();
    }


    private final int puerto = 5000;
    private final int nConexiones=2;
    private ArrayList<Socket> usuarios = new ArrayList<>();

    private Boolean turno = true;
     private int turnos=1;

     private int J[][] = new int[7][6];

     public void observador() throws IOException {
         try{
             for (int i=0; i<7; i++){
                 for (int j=0; j<6; j++){
                     J[i][j]=-1;
                 }
             }
             ServerSocket servidor = new ServerSocket(puerto, nConexiones);
             while (true) {
                 System.out.println("Esperando cliente...");
                 Socket cliente = servidor.accept();
                 System.out.println("Cliente conectado");
                 usuarios.add(cliente);
                 int ra = turnos%2==0?1:0;
                 turnos++;

                        HiloServidor hilo = new HiloServidor(cliente, usuarios,ra, J);
                        Thread t = new Thread(hilo);
                        t.start();
                if (usuarios.size()==2){
                    break;
                }
             }
             servidor.close();

         }
         catch (Exception e){
             System.out.println("Error en el observador");
         }

     }
}