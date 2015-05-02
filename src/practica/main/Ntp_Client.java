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
import java.util.Date;

/**
 *
 * @author marco
 */
public class Ntp_Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try {
            Socket socket = new Socket("127.0.0.1", 5000);
            DataOutputStream salida = new DataOutputStream(socket.getOutputStream());
            Date tiempo = new Date();
            String envio=Long.toString(tiempo.getTime());
            salida.writeUTF(envio);
            socket.close();
            DataInputStream entrada =  new DataInputStream(socket.getInputStream());
            //sudo su
            // date -s 17:32

        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
}
