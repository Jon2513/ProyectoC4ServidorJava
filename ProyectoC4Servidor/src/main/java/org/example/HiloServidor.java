package org.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class HiloServidor implements Runnable {

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private int ra;
    private int J[][];
    private boolean turno;
    private ArrayList<Socket> usuarios = new ArrayList<Socket>();
    public HiloServidor(Socket socket, ArrayList<Socket> usuarios, int ra, int[][] j) {
    this.socket = socket;
        this.usuarios = usuarios;
        this.ra = ra;
        J = j;
    }

    @Override
    public void run() {
        try{
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
           if(ra==1){
               turno=true;}
              else{
               turno=false;}
            String msg = "";
            if (turno) {
                msg += "R/";
            } else {
                msg += "A/";
            }

            msg += turno;
            out.writeUTF(msg);
            System.out.println(msg);

            while (true) {


                String mensaje = in.readUTF();
                System.out.println(mensaje);
                String[] datos = mensaje.split("/");
                int r = Integer.parseInt(datos[0]);
                int a = Integer.parseInt(datos[1]);
                J[r][a] = ra;

                String msj = "";
                msj +=( ra+"/"+r + "/" + a+"/");
                boolean ganador=comprobarGanador(ra) ;
                boolean completo=comprobarCompleto();

                if(!ganador && !completo){
                    msj += "NADIE";
                }
                else if(!ganador && completo){
                    msj += "EMPATE";
                }
                else if(ganador){
                    msj+= ra == 1 ? "R":"A";
                }

                for (Socket s : usuarios) {
                    DataOutputStream out = new DataOutputStream(s.getOutputStream());
                    out.writeUTF(msj);
                    System.out.println(msj);

                }

            }
        }catch (Exception e){
            System.out.println("Error en el hilo servidor");
        }

    }
    //comprobar si hay ganador
    public boolean comprobarGanador( int ra) {
        int cont = 0;
        //comprobar horizontal
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                if (J[i][j] == ra) {
                    cont++;
                    if (cont == 4) {
                        return true;
                    }
                } else {
                    cont = 0;
                }
            }
        }
        //comprobar vertical
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (J[j][i] == ra) {
                    cont++;
                    if (cont == 4) {
                        return true;
                    }
                } else {
                    cont = 0;
                }
            }
        }
        //comporbar diagonal
        for(int i =0;i<6;i++){
            if(i+3<6){
                for(int j=0;j<7;j++){
                    if(j+3<7){
                        if(J[j][i]==ra && J[j+1][i+1]==ra && J[j+2][i+2]==ra && J[j+3][i+3]==ra){
                            return true;
                        }
                    }
                }
            }
            else{
                cont=0;
            }
        }
        return false;
    }

    public boolean comprobarCompleto() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                if (J[i][j] == -1) {
                    return false;
                }
            }
        }
        return true;
    }
}
