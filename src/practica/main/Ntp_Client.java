/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import practica.tools.Tiempo;

/**
 *
 * @author marco
 */
public class Ntp_Client extends Thread {
    
    static Double[] latencia = new Double[8];
    static Double[] compensacion = new Double[8];
    static Double[][] tab = new Double[8][2];
    private static ArrayList<Tiempo> tiempos= new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        for(int i=0; i < 8; i++) {
            Thread t = new Ntp_Client();
            t.start();
            t.join();
        }
        long lat=0,com=0;
        //System.out.println("el tamanio de tiempo es: "+tiempos.size());
        for(Tiempo ti:tiempos){
            if(ti.getLatencia() <= lat){
                lat = ti.getLatencia();
                com = ti.getCompensacion();
            }
        }
        
        actualiza(com);
        //System.out.println("lat es: "+lat+" y com es: "+com);

    }//end main 

    public void run() {

        DataOutputStream send;
        DataInputStream receive;
        String T1server, T2server;
        double A, B, C;
        int cont = 0;
        
       // while (cont < 8) {
            try {
                Socket socket = new Socket("10.20.3.90", 5000);
                send = new DataOutputStream(socket.getOutputStream());
                Date tiempo1 = new Date();//hora cliente
                String envio = Long.toString(tiempo1.getTime()); //convertimos a cadena 
                send.writeUTF(envio); //enviamos hora de cliente

                receive = new DataInputStream(socket.getInputStream());
                String horaS = receive.readUTF(); //hora cliente y servidor
        //        System.out.println("el horario "+cont+" en server es: " + horaS);

                Date tiempo2 = new Date();//hora cliente
                //obtenemos las horas
                String[] palabras = horaS.split("@");
                // for (String palabra:palabras) 
                T1server = palabras[0]; //tiempo de recibo
                T2server = palabras[1]; //tiempo de envio
                // System.out.println (T1server);
                //System.out.println (T2server);

                A = tiempo1.getTime() - Double.parseDouble(T1server);  // T2-T1
                B = Double.parseDouble(T2server) - tiempo2.getTime();  // T3-T4
                C = tiempo2.getTime() - Double.parseDouble(T2server);  // T4-T3

                latencia[cont] = (A + C) / 2; //delay
                compensacion[cont] = (A + B) / 2; //offset
                
                Tiempo ti =  new Tiempo(Math.round(latencia[cont]),Math.round(compensacion[cont]) );
                tiempos.add(ti);
                
                tab[cont][0] = latencia[cont]; // creamos una matriz
                tab[cont][1] = compensacion[cont];

                //System.out.println("lat: " + Latencia[cont]); 
                //System.out.println("Com: " + Compensacion[cont]);

                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            cont++;
        //}//end while
       // Actualiza();
    }//end Run

    public static void actualiza(long compensacion){
              
        /*Arrays.sort(tab);// ordenamos el arrays de forma ascendente
        //imprimimos la tab
        for(int f=0;f<tab.length;f++) {
            for(int c=0;c<tab[f].length;c++) {
                System.out.print(tab[f][c]+" ");
            }
            System.out.println();
        }*/
        //obtenemos la min latecia y compensacion [0]
        //Arrays.sort(Latencia);// ordenamos el arrays de forma ascendente
        //Arrays.sort(Compensacion);// ordenamos el arrays de forma ascendente
        //mostramos el arrays
        // for (Double lat : Latencia) { 
        //    System.out.println("lat: " + lat);
        //}
        Date T =new Date(); //obtenemos la fecha
        Long Hora = T.getTime() + compensacion; //sumamos la compensacion a  la fecha

        String H =Long.toString(Hora);
        
        try{
        Runtime.getRuntime().exec("date -s @ " + H); //actualizamos la fecha
        
        System.out.println(" Establecido correctamente \n");
        }
        catch(Exception e){
           System.out.println(e.toString());
        }
    }//end actualizar
    
}//end class
