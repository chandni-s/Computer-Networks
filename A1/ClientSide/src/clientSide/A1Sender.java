/**
 *
 */
package clientSide;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Chandini Saigal, Srihitha Maryada 999829164
 *
 */
public class A1Sender {
    
    /**
     * @param args
     */
    private final static String server = "localhost";
    private final static int port = 7777;
    static Socket clientSock;
    
    public static void main(String[] args) {
        
        sendTheseFiles(3);
        
    }
    
    public A1Sender() {
    };
    
    private static void sendTheseFiles(int k) {
        
        sendInitialGreeting();
        
        if (k < 0) {
            System.out.println("No files exist to send");
        }
        
        File folder = new File("./");
        File[] files = folder.listFiles();
        FileReader fr = null;
        BufferedReader br = null;
        String content = "";
        try {
            
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile() && files[i].getName().endsWith(".txt")) {
                    System.out.println(files[i].getName());
                    
                    fr = new FileReader(files[i].getName());
                    br = new BufferedReader(fr);
                    String line;
                    
                    while ((line = br.readLine()) != null) {
                        content += line + "\n";
                    }
                }
            }
            writeToFile(content);
            //System.out.println(content);
            
            try {
                fr.close();
                br.close();
                clientSock.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void writeToFile(String content){
    	File file = new File ("../ServerSide/StreamReceived.txt");
    	try {
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void connect(){
        
        try{
            //Connecting to server
            System.out.println("Client socket connecting on: " + server
                               + " port: " + port);
            
            clientSock = new Socket(server, port);
            System.out.println("Client connected to Server: "
                               +clientSock.isConnected());
            
        }
        
        catch (IOException e) {
            e.printStackTrace();
            try {
                clientSock.close();
            } catch (IOException ex) {
                ex.getMessage();
            }
            System.exit(1);
        }
        
        
    }
    
    public static void sendInitialGreeting() {
        
        
        OutputStream outToServer = null;
        DataOutputStream toServer = null;
        
        InputStream inFromServer = null;
        DataInputStream inServer = null;
        
        
        try {
            
            //Connect to server
            connect();
            
            SocketAddress ip_address = clientSock.getLocalSocketAddress();
            System.out.println("<IP Address>:" + ip_address);
            
            //Send initial greeting to server
            outToServer = clientSock.getOutputStream();
            toServer = new DataOutputStream(outToServer);
            toServer.writeUTF(ip_address + ":<" + port + ">");
            toServer.writeUTF("\n");  // delimiter
            
            //Read from server
            inFromServer = clientSock.getInputStream();
            inServer = new DataInputStream(new BufferedInputStream (inFromServer));
            inServer = new DataInputStream(inFromServer);
            //String serverMsg = new String (inServer.read());
            System.out.println("FromServer:" + inServer.read()); 
            
            toServer.close();
            inServer.close();
            
        } catch (IOException e) {
            e.printStackTrace();
            try {
                toServer.close();
                inServer.close();
                clientSock.close();
            } catch (IOException ex) {
                ex.getMessage();
            }
            System.exit(1);
        }
    }
}
