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
public class IS_TP1_ServerSocketDog {

    private static final int portServer = 4446;

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        IS_TP1_ServerSocketDog server = new IS_TP1_ServerSocketDog();
        server.run();
    }

    public void run() throws Exception {
        try{
            ServerSocket serverSocket = new ServerSocket(portServer);
            
            while(true){
            Socket client = serverSocket.accept();

            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            
            
               System.out.println("success dog server");    
               
               
               System.out.println("ready to read line");
               String input = in.readLine();//lê o input para uma string
               
               
               System.out.println("read line"); 
               TMyPlace place = MessageManagement.retrievePlaceStateObject(input);//deserializa a mens
               
               System.out.println("deserialized message"); 
               
               for (int i = 0; i < place.getPlace().size(); i++){
                if (place.getPlace().get(i).isWolf() == true && place.getPlace().get(i).isCow() == false && place.getPlace().get(i).isObstacle() == false){
                    place.getPlace().get(0).setPosition(place.getPlace().get(i).getPosition());
                    place.getPlace().get(0).setDog(true);
                }
            }
               
               System.out.println("calculated place");
               String s = MessageManagement.createPlaceStateContent(place);//serialize
               
               out.println(s);
               //out.println();
              
               System.out.println("sent dog");  
               client.close();
                  
            }
            
            

        }catch(IOException e){
            System.out.println("fail dog server");
        }
       
    }
}