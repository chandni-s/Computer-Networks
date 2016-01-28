/**
 * 
 */
package clientSide;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;


/**
 * @author 
 *
 */
public class A1Sender {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		sendInitialGreeting();
		sendTheseFiles(3);

	}
	
	public A1Sender() {};

	private static void sendTheseFiles(int k) {
		
		
		
		if (k < 0) {
			System.out.println("No files exist to send");
		}
		System.out.println("Blahhh");
	}
	
	public static void sendInitialGreeting() {
		String server = "localhost";
		int port = 7777;

		
		try {
			System.out.println("Client socket connecting on: " + server + " port: " + port);
			
			Socket clientSock = new Socket(server, port);
	
			SocketAddress ip_address = clientSock.getLocalSocketAddress();
			System.out.println("IP Address: " + ip_address);

			
			clientSock.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
