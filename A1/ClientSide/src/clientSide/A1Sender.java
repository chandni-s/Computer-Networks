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
import java.io.OutputStream;

/**
 * @Chandini Sehgal 998973375, Srihitha Maryada 999829164
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

		if (k < 1) {
			System.out.println("No files to send");
			try {
				clientSock.close();
			} catch (IOException e) {
				System.out.println("No files to send, failed in closing client socket" + e.getMessage());
				e.printStackTrace();
			}
		}

		File folder = new File("./");
		File[] files = folder.listFiles();
		FileReader fr = null;
		BufferedReader br = null;
		
		try {

			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile() && files[i].getName().endsWith(".txt")) {
					System.out.println(files[i].getName());

					fr = new FileReader(files[i].getName());
					br = new BufferedReader(fr);
					String line;

					while ((line = br.readLine()) != null) {
						System.out.println(line);
						writeToServer(line+"\n");
					}
				}
			}

			fr.close();
			br.close();
			clientSock.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendInitialGreeting() {

		try {

			// Connect to server
			connect();

			SocketAddress ip_address = clientSock.getLocalSocketAddress();
			System.out.println("<IP Address>:" + ip_address);

			String ip_adr = ip_address + ":" + port;
			writeToServer(ip_adr);

			/*
			 * //Read from server inFromServer = clientSock.getInputStream();
			 * inServer = new DataInputStream(new BufferedInputStream
			 * (inFromServer)); inServer = new DataInputStream(inFromServer);
			 * //String serverMsg = new String (inServer.read());
			 * System.out.println("FromServer:" + inServer.read());
			 */

		} catch (IOException e) {
			
			e.printStackTrace();
			try {
				clientSock.close();
			} catch (IOException e1) {
				System.out.println("SendingInitialGreeting failed: " + e1.getMessage());
				e1.printStackTrace();
			}
			System.exit(1);
		}
	}

	public static void connect() {

		try {
			// Connecting to server
			System.out.println("Client socket connecting on: " + server
					+ " port: " + port);

			clientSock = new Socket(server, port);
			System.out.println("Client connected to Server: "
					+ clientSock.isConnected());
		} catch (IOException e) {
			
			System.out.println("Client socket failed to connect to server " + e.getMessage());
			e.printStackTrace();

			try {
				clientSock.close();
			} catch (IOException e1) {
				System.out.println("Client socket failed at closing connection: " + e1.getMessage());
				e1.printStackTrace();
			}
			System.exit(1);
		}
	}

	public static void writeToServer(String content) throws IOException {

		OutputStream outToServer = clientSock.getOutputStream();
		DataOutputStream toServer = new DataOutputStream(outToServer);
		toServer.writeUTF(content);
		toServer.flush();
	}
}
