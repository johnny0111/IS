package is_tp1_sockets;

import Common.MessageManagement;
import java.io.BufferedReader;
import java.io.IOException;
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
//
//        //TODO - Lab3
//        //Start your server socket
//        //Read content received from client (Simulation)
//        //Calculate new Cow position
//        //Send new cow position to the client (Simulation)
//
//        try{
//            System.out.println("success wolf server");
//            ServerSocket serverSocket = new ServerSocket(portServer);
//            Socket client = serverSocket.accept();
//            
//            while(true){
//            
//
//            InputStream inputToServer = client.getInputStream();//input stream da socket
//            OutputStream outputFromServer = client.getOutputStream();//output stream da socket
//
//            PrintWriter writer = new PrintWriter(outputFromServer, true);//buffer para escrever para a socket
//
//            InputStreamReader in = new InputStreamReader(inputToServer);
//            BufferedReader reader = new BufferedReader(in);//buffer de leitura
//            String line = reader.readLine();    // reads a line of text
//
//            TMyPlace place = new TMyPlace();
//
//            place = MessageManagement.retrievePlaceStateObject(line);//deserializa a mens
//
//            for (int i = 0; i < place.getPlace().size(); i++){
//                if (place.getPlace().get(i).isCow() == true && place.getPlace().get(i).isObstacle() == false){
//                    place.getPlace().get(0).setPosition(place.getPlace().get(i).getPosition());
//                    place.getPlace().get(0).setWolf(true);
//                }
//            }
//
//            String s = MessageManagement.createPlaceStateContent(place);//serialize
//
//            writer.print(s);//sends to outputs stream
//
//            //client.close();
//            //serverSocket.close();            
//            }
//
//        }catch(IOException e){
//            System.out.println("fail wolf server");
//          e.printStackTrace();
//        }
        try{
            ServerSocket serverSocket = new ServerSocket(portServer);
            
            while(true){
            Socket client = serverSocket.accept();

            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            
            
               System.out.println("success wolf server");    
               
               
               System.out.println("ready to read line");
               String input = in.readLine();//lê o input para uma string
               
               
               System.out.println("read line"); 
               TMyPlace place = MessageManagement.retrievePlaceStateObject(input);//deserializa a mens
               
               System.out.println("deserialized message"); 
               
            for (int i = 0; i < place.getPlace().size(); i++){
                if (place.getPlace().get(i).isCow() == true && place.getPlace().get(i).isObstacle() == false){
                    place.getPlace().get(0).setPosition(place.getPlace().get(i).getPosition());
                    place.getPlace().get(0).setWolf(true);
                }
            }
               
               System.out.println("calculated place");
               String s = MessageManagement.createPlaceStateContent(place);//serialize
               
               out.println(s);
               //out.println();
              
               System.out.println("sent wolf");  
               client.close();
                  
            }
            
            

        }catch(IOException e){
            System.out.println("fail wolf server");
        }

    }


}