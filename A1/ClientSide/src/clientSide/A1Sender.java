/**
 * 
 */
package clientSide;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;

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

	public A1Sender() {
	};

	private static void sendTheseFiles(int k) {

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
						content += line+"\n";
					}
				}
			}
			System.out.println(content);
			fr.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendInitialGreeting() {
		String server = "localhost";
		int port = 7777;

		Socket clientSock = null;
		DataOutputStream toServer = null;

		try {
			System.out.println("Client socket connecting on: " + server
					+ " port: " + port);

			clientSock = new Socket(server, 7777);
			System.out.println("Client connected to Server: "+clientSock.isConnected());

			SocketAddress ip_address = clientSock.getLocalSocketAddress();
			System.out.println("IP Address: " + ip_address);

			toServer = new DataOutputStream(clientSock.getOutputStream());
			toServer.writeBytes(clientSock.getLocalSocketAddress() + ":" + port);
			toServer.writeBytes("\n");  // delimiter

		} catch (IOException e) {
			e.printStackTrace();
			try {
				toServer.close();
				clientSock.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}
}
