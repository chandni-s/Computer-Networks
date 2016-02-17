package mySocket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class A1ReceiverFiles extends Thread {

	private Socket socket;

	// call this in A1Listener
	public A1ReceiverFiles(Socket socket) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
	}

	public void run() {

		readWriteData();

		try {
			socket.close();
		} catch (Exception ex) {
			System.out.println("ReceiverFiles error in closing server: "
					+ ex.getMessage());
		}
	}

	private void readWriteData() {

		// set the connection
		DataInputStream dataInFromClient = null;
		DataOutputStream dataOutToClient = null;
		String msgFromServer = null;

		// receive and send data to and from clientFiles
		try {
			dataInFromClient = new DataInputStream(new BufferedInputStream(
					socket.getInputStream()));

			dataOutToClient = new DataOutputStream(new BufferedOutputStream(
					socket.getOutputStream()));

			msgFromServer = dataInFromClient.readUTF();

			System.out.println("Message from client: " + msgFromServer);

			dataOutToClient.writeUTF("Server Listening to clientFiles");
			
			readWriteDataToFile();
			
		}

		// if any error in connecting to clientFiles - close socket and exit
		catch (Exception e) {

			System.out.println("Error in setting up ServerFiles: "
					+ e.getMessage());

			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.exit(1);

			e.printStackTrace();
		}
	}

	private void readWriteDataToFile() {
		// TODO Auto-generated method stub
		
		// file name and file size length from
		// client - create test dir if it doens't exist 
		// create empty files of same name
		// write content of files received from client to files just created
		// once the content is delimiter and size of this new file that is
		// being written matches the file size sent by client - end the file
		// and start writing new one if needed.
	}
}
