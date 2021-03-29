/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is_tp1_serversocket;

import Common.MessageManagement;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import org.netbeans.xml.schema.updateschema.TMyPlace;
import org.netbeans.xml.schema.updateschema.TPlace;
import org.netbeans.xml.schema.updateschema.TPosition;

/**
 *
 * @author adroc
 */
public class IS_TP1_ServerSocketWolf {

    private static final int portServer = 4445;

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        IS_TP1_ServerSocketWolf server = new IS_TP1_ServerSocketWolf();
        server.run();
    }

 public void run() throws Exception {

        //TODO - Lab3
        //Start your server socket
        //Read content received from client (Simulation)
        //Calculate new Cow position
        //Send new cow position to the client (Simulation)


        ServerSocket serverSocket = new ServerSocket(portServer);
        Socket client = serverSocket.accept();
            
        InputStream inputToServer = client.getInputStream();//input stream da socket
        OutputStream outputFromServer = client.getOutputStream();//output stream da socket
        
        PrintWriter writer = new PrintWriter(outputFromServer, true);//buffer para escrever para a socket

        InputStreamReader in = new InputStreamReader(inputToServer);
        BufferedReader reader = new BufferedReader(in);//buffer de leitura
        String line = reader.readLine();    // reads a line of text
        
        TMyPlace place = new TMyPlace();
        
        place = MessageManagement.retrievePlaceStateObject(line);//deserializa a mens
        
        for (int i = 0; i < place.getPlace().size(); i++){
            if (place.getPlace().get(i).isCow() == true && place.getPlace().get(i).isObstacle() == false){
                place.getPlace().get(0).setPosition(place.getPlace().get(i).getPosition());
                place.getPlace().get(0).setWolf(true);
            }
        }
        
        String s = MessageManagement.createPlaceStateContent(place);//serialize
        
        writer.print(s);//sends to outputs stream
        
        client.close();
        serverSocket.close();

    }


}
 

