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
public class IS_TP1_ServerSocketCow {

    private static final int portServer = 4444;

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        IS_TP1_ServerSocketCow server = new IS_TP1_ServerSocketCow();
        server.run();
    }

    public void run() throws Exception {
        try{
            ServerSocket serverSocket = new ServerSocket(portServer);
            
            while(true){
            Socket client = serverSocket.accept();

            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            
            
               System.out.println("success cow server");    
               
               
               System.out.println("ready to read line");
               String input = in.readLine();//lê o input para uma string
               
               
               System.out.println("read line"); 
               TMyPlace place = MessageManagement.retrievePlaceStateObject(input);//deserializa a mens
               
               System.out.println("deserialized message"); 
               
               int[] free_places = new int[9];
               
               for(int i = 0; i < place.getPlace().size(); i++){
                   if (place.getPlace().get(i).getGrass() > 0 && place.getPlace().get(i).isWolf() == false && place.getPlace().get(i).isObstacle() == false)
                       free_places[i]=1;
                   else free_places[i] = 0;
               }  
                Random rand = new Random();
                int x = rand.nextInt(8);
               
               do{
                   x= rand.nextInt(8);
               }while(free_places[x]==0);
               
               place.getPlace().get(0).setPosition(place.getPlace().get(x).getPosition());
               place.getPlace().get(0).setCow(true);
                   
              /* for (int i = 0; i < place.getPlace().size(); i++){
                    if (place.getPlace().get(i).getGrass() > 0 && place.getPlace().get(i).isWolf() == false && place.getPlace().get(i).isObstacle() == false){
                        place.getPlace().get(0).setPosition(place.getPlace().get(i).getPosition());
                        place.getPlace().get(0).setCow(true);
                        break;
                    }
                    
                }*/
               
                
               
               
               System.out.println("calculated place");
               String s = MessageManagement.createPlaceStateContent(place);//serialize
               
               out.println(s);
               //out.println();
              
               System.out.println("sent cow");  
               client.close();
                  
            }
            
            

        }catch(IOException e){
            System.out.println("fail cow server");
        }
       
    }
}