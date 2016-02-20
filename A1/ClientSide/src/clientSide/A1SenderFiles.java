/**
 * 
 */
package clientSide;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
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

	public static void main(String[] args) {
		sendTheseFiles(3);
	}

	private static void sendTheseFiles(int k) {

		connect();

		if (k < 1) {
			System.out.println("No files to send, please specify > 0");

		}

		// TODO
		// send the file name and length of file to server
		// set a delimiter and send it to server to mark end of file
		File folder = new File("./");
		File[] files = folder.listFiles();
		FileReader fr = null;
		BufferedReader br = null;
		String name = null;
		long size;

		int count = 0;
		try {

			writeToServer("Got to SenderFiles on client side\n");
			for (int i = 0; i < files.length && count < k; i++) {

				name = files[i].getName();
				size = files[i].length();

				if (files[i].isFile() && name.endsWith(".txt")) {
					writeToServer(";;/"+ name+"\n");
					//writeToServer(Double.toString(size));

					count += 1;

					// System.out.println(files[i].getName());

					fr = new FileReader(name);
					br = new BufferedReader(fr);
					
					String line;
					while ((line = br.readLine()) != null) {
						System.out.println(line);
						writeToServer(line);
					}		
					System.out.println(">>/");
					writeToServer(">>/");
				}
				
			}

			fr.close();
			br.close();
			clientFileSock.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void connect() {

		try {
			// Connecting to server
			System.out.println("ClientFiles socket connecting on: " + serverFile + " port: " + port);

			clientFileSock = new Socket(serverFile, port);
			System.out.println("ClientFiles connected to ServerFiles: " + clientFileSock.isConnected());

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
