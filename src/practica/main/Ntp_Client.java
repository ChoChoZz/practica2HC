/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica.main;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
    static String T1server, T2server;
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
        
        actualiza(com,T2server);
        //System.out.println("lat es: "+lat+" y com es: "+com);

    }//end main 

    public void run() {

        DataOutputStream send;
        DataInputStream receive;
        
        double A, B, C;
        int cont = 0;
        
       // while (cont < 8) {
            try {
                Socket socket = new Socket("127.0.0.1", 5000);
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

                T1server = palabras[0]; //tiempo de recibo
                T2server = palabras[1]; //tiempo de envio

                A = tiempo1.getTime() - Double.parseDouble(T1server);  // T2-T1
                B = Double.parseDouble(T2server) - tiempo2.getTime();  // T3-T4
                C = tiempo2.getTime() - Double.parseDouble(T2server);  // T4-T3

                latencia[cont] = (A + C) / 2; //delay
                compensacion[cont] = (A + B) / 2; //offset
                
                Tiempo ti =  new Tiempo(Math.round(latencia[cont]),Math.round(compensacion[cont]) );
                tiempos.add(ti);
                
                tab[cont][0] = latencia[cont]; // creamos una matriz
                tab[cont][1] = compensacion[cont];

                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            cont++;
        //}//end while
       // Actualiza();
    }//end Run

    public static void actualiza(long compensacion,String T2server){
     
        Long Hora = Long.parseLong(T2server) + compensacion; //sumamos la compensacion a  la fecha
        System.out.println("la compensacion es: "+compensacion);
        String H =Long.toString(Hora);
        
        try{
            Date fecha = new Date(Hora);
            System.out.println("la hora que asigno es: "+fecha.toString());
            //Runtime.getRuntime().exec("sudo su");
            //Runtime.getRuntime().exec("Marco18346839");
            String array[]={"sudo","-S","su"};
            String pass ="Marco18346839";
            Process proc = null;
            BufferedWriter writer = null;
            proc = Runtime.getRuntime().exec(array);
            writer = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream()));
            writer.write(pass);
            
            Runtime.getRuntime().exec("date --set '"+fecha+"'"); //actualizamos la fecha
        
        System.out.println(" Establecido correctamente \n");
        }
        catch(Exception e){
           System.out.println(e.toString());
        }
    }//end actualizar
    
}//end class
