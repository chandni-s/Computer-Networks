package mySocket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class A1Receiver extends Thread {

	private Socket socket;

	public A1Receiver(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		DataInputStream din = null;
		DataOutputStream dout = null;
		String inMsg = null;
		FileOutputStream fileOut = null;
		byte data[] = new byte[65536];

		// set the connection
		try {
			din = new DataInputStream(new BufferedInputStream(
					socket.getInputStream()));
			dout = new DataOutputStream(new BufferedOutputStream(
					socket.getOutputStream()));
			inMsg = din.readUTF(); // hear INITIAL_GREETING. This should be
									// "/<IP_address>:<port#>" of the sender.
			dout.writeUTF("A1Receiver at your service: "); // send a hello back
		} catch (IOException ex) {
			System.out.println("A1Receiver: error in connection setup " + ex);
			try {
				socket.close();
			} catch (IOException e) {
			}
			System.exit(1);
		}

		System.out.println("A1Receiver: Set the connection successfully with "
				+ inMsg);
		int count;
		try {
			fileOut = new FileOutputStream("StreamReceived.txt");
		} catch (FileNotFoundException e1) {
			try {
				socket.close();
			} catch (IOException e) {
			}
			System.exit(1);
		}

		// keep receiving
		for (;;) {
			try {
				while ((count = din.read(data)) != -1) {
					fileOut.write(data, 0, count);
					fileOut.flush();
				}

				// check whether the client closed
				try {
					dout.write(0);
				} catch (IOException e) {
					System.out.println("A1Receiver: End of stream");
					fileOut.close();
					break;
				}

			} catch (Exception e) {
				System.out.println("A1Receiver: some other error " + e);
				break;
			}
		}

		// close the socket
		try {
			socket.close();
			System.out.println("A1Receiver: socket closed.");
		} catch (IOException ex) {
			System.out.println("A1Receiver: error while closing the socket.");
		}
	}

}