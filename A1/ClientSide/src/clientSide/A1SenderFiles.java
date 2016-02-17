/**
 * 
 */
package clientSide;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author 
 *
 */
public class A1SenderFiles {
	
	
	private final static String serverFile = "localhost";
	private final static int port = 7777;
	static Socket clientFileSock;
	
	
	public static void main (String[] args) {
		sendTheseFiles(3);
	}

	private static void sendTheseFiles(int k) {
		
		connect();
		
		if (k < 1) {
			System.out.println("No files to send, please specify > 0");
			
		}
		
		
		
		try {
			writeToServer("Got to SenderFiles on client side");
			
			//TODO
			// send the file name and length of file to server
			// set a delimiter and send it to server to mark end of file
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void connect() {

		try {
			// Connecting to server
			System.out.println("ClientFiles socket connecting on: " + serverFile
					+ " port: " + port);

			clientFileSock = new Socket(serverFile, port);
			System.out.println("ClientFiles connected to ServerFiles: "
					+ clientFileSock.isConnected());
			
			
		} catch (IOException e) {
			
			System.out.println("ClientFiles socket failed to connect to serverFiles " + e.getMessage());
			e.printStackTrace();

			try {
				clientFileSock.close();
			} catch (IOException e1) {
				System.out.println("ClientFiles socket failed at closing connection: " + e1.getMessage());
				e1.printStackTrace();
			}
			System.exit(1);
		}
	}
	
	public static void writeToServer(String content) throws IOException {

		OutputStream outToServer = clientFileSock.getOutputStream();
		DataOutputStream toServer = new DataOutputStream(outToServer);
		toServer.writeUTF(content);
		toServer.flush();
	}
}
