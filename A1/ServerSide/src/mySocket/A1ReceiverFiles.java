package mySocket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class A1ReceiverFiles extends Thread {

	private Socket socket;
	private DataInputStream dataInFromClient = null;
	private DataOutputStream dataOutToClient = null;
	private String msgFromServer = null;
	private byte data[] = new byte[65536];

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

		// keep receiving

		// if the Test directory does not exist, create it
		File testDir = new File("./Test");

		if (!testDir.exists()) {
			try {
				testDir.mkdir();
			} catch (SecurityException se) {
				se.printStackTrace();
			}
		}
		// FileOutputStream fileOut = null;
		int count = 0;
		// OutputStream out = null;
		String info = null;
		String name = "";
		double size = 0;
		File file = null;
		FileWriter fileOut = null;
		BufferedWriter bufOut = null;

		for (;;) {
			try {
				// as long as data is available
				while (dataInFromClient.available() > 0) {
					
					// read string output from client
					info = dataInFromClient.readUTF(); 

					// make sure all file and buffer outputs are closed
					if (info.startsWith(";;/")) {

						if ((file != null)) {
							bufOut.close();
							fileOut.close();
						}

						// Get the file name from string recevied from client
						// create file with that name and open it
						name = info.substring(3);
						file = new File("./Test/" + name);
						file.createNewFile();

						fileOut = new FileWriter(file);
						bufOut = new BufferedWriter(fileOut);
					}

					// if string output from clients starts with ::/ - get file size
					else if (info.startsWith("::/")) {
						size = Double.parseDouble(info.substring(3));
					}
					
					// else the string is content of file and write it to file
					else {
						if (file.length() != size) {
							bufOut.write(info);
						}
					}
				}
				
				
				// check whether the client closed
				try {
					dataOutToClient.write(0);
				} catch (IOException e) {
					System.out.println("A1ReceiverFiles: End of stream" + e);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("A1ReceiverFiles: some other error " + e);
				break;
			}
		}
	}
}
