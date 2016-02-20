package mySocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class A1Listener {

	private final static int port = 7777; // the dedicated port

	static private ServerSocket serverSocket;

	public static void main(String[] args) throws IOException {
		serverSocket = new ServerSocket(port);
		System.out.println("Server listening on: " + port);
		// serverSocket.setSoTimeout(300000); // timeout after 5 minutes
		serverSocket.setSoTimeout(0); // listen forever.
		for (Socket socket;;) { // keep listening
			socket = serverSocket.accept();
			// new A1Receiver(socket).start();
			new A1ReceiverFiles(socket).start();
		}
	}
}
