import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
<<<<<<< HEAD
=======
import java.util.Set;
>>>>>>> 2a08e6c... wrote outupdates func

/**
 * @Name Chandni Sehgal StudentNo: 998973375
 * @Name Srihitha Maryada StudentNo: 999829164
 *
 */
public class DVRouter {

<<<<<<< HEAD
	protected static String seqNum = null;
	protected static String routerID = null;
	protected static int routerToDv = 0;

	private static HashMap<String, RoutingTableType> dvrouter = new HashMap<String, RoutingTableType>();
	private static HashMap<String, Integer> dvUpdate = new HashMap<String, Integer>();
=======
	// Destination <NextHop:Cost>
	private static HashMap<String, RoutingTable> dvrouter = new HashMap<String, RoutingTable>();
>>>>>>> 2a08e6c... wrote outupdates func
	static List<String> update = new ArrayList<String>();

	protected class RoutingTableType {
		String destination;
		String nextHop;
		int totalCost;

		public RoutingTableType(String destination, String nextHop, int totalCost) {
			this.destination = destination;
			this.nextHop = nextHop;
			this.totalCost = totalCost;
		}
	}

	public static List<String> readRoutingTable(String filename) {

		/*
		 * Read each update and add it into a list of string Return a list of
		 * updates
		 */

		File file = null;
		FileReader readFile = null;
		BufferedReader bufRead = null;

<<<<<<< HEAD
		List<String> updateList = new ArrayList<String>();
=======
		int sequenceNum = 0;
		String routerID = null;
		
		
>>>>>>> 2a08e6c... wrote outupdates func

		try {
			file = new File(filename);
			readFile = new FileReader(file);
			bufRead = new BufferedReader(readFile);

			String line;
			String update = "";

			while ((line = bufRead.readLine()) != null) {

				if (line.length() != 0) {
					update += line + "\n";

<<<<<<< HEAD
				} else {
					updateList.add(update);
					update = "";
=======
				// get routerID
				else if (count == 1) {
					routerID = line;
					count += 1;
				}

				// get Hops and Cost
				else {
					hopCost = line.trim().split(";");
					count += 1;
					
					if (hopCost != null && hopCost.length > 1) {
						
						update.add(sequenceNum+"");
						update.add(routerID);//" + hopCost[0] + " " + hopCost[1].trim() + "\n");

						// Create object
						//RoutingTable rt = updateRoutingTable(routerID, hopCost);
						// put routerID and Routing object into hashMap
						
					}
					if (line.length() == 0) {
						count = 0;
						update.add("\n");
					}
>>>>>>> 2a08e6c... wrote outupdates func
				}
			}

			// add the last update to string array
			updateList.add(update);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (file != null) {
					bufRead.close();
					readFile.close();

				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}

		return updateList;
	}

	public static void dvUpdateTable(String update) {
		/*
		 * Parses DVUpdate and puts all the data into dvUpdate Hashmap. Calls
		 * helper method to update router Dv's routing table.
		 */

		// Get the sequence number and routerID from
		String[] attributes = update.split("\n");
		seqNum = attributes[0];
		routerID = attributes[1];

		// get all the destinations and its cost and put them into dvUpdate
		// HashMap
		for (int i = 2; i < attributes.length; i++) {
			String[] dvKeys = attributes[i].split(";");
			int cost = Integer.parseInt(dvKeys[1].trim());
			String dvDest = dvKeys[0];
			dvUpdate.put(dvDest, cost);
			// Gets cost from current router to DV and updates global variable
			if (dvDest.equals("DV")) {
				routerToDv = cost;
			}
		}
		// Call helper method that updates dv router's routing table
		DVUpdateHopCost();
	}

	public static void DVUpdateHopCost() {
		/*
		 * Updates DV router's routing table with the given DVUpdat.
		 */

		for (String key : dvUpdate.keySet()) {

			int costUpdate;
			int newCost = 0;
			String dest;
			costUpdate = dvUpdate.get(key);

			// if DVUpdate has "DV" for destination then update
			// destination to routerID
			if (key.equals("DV")) {
				dest = routerID;
				newCost = costUpdate;
			}

			else {
				dest = key;
			}

			// if RoutingTableType already in DV's routing table
			if (dvrouter.containsKey(dest)) {
				RoutingTableType rt = dvrouter.get(dest);
				int dvCost = rt.totalCost;

				if (costUpdate < dvCost) {
					rt.nextHop = routerID;

					// update totalCost
					if (newCost == 0) {
						// System.out.println(routerToDv);
						newCost = routerToDv + costUpdate;
						rt.totalCost = newCost;
					}
					rt.totalCost = newCost;
				}
			}

			else {
				// Add new RoutingTableType to DV routing table
				if (newCost == 0) {
					newCost = routerToDv + costUpdate;
				}
				updateRoutingTable(dest, newCost);

			}
		}
		// Call helper method to write contents to file
		writeOutFile();
	}

	public static RoutingTableType updateRoutingTable(String dest, int cost) {

		/*
		 * Create and return an object for each update and put it into a routing
		 * HashMap
		 */

		RoutingTableType rt = null;
		DVRouter dv = new DVRouter();

		rt = dv.new RoutingTableType(dest, routerID, cost);

		dvrouter.put(rt.destination, rt);

		return rt;
	}
	
	public static void writeOutFile() {
		
		File msgFile = null;
		FileWriter msgout = null;
		BufferedWriter bufMsg = null;

		try {

			msgFile = new File("./OutUpdates.txt");

			msgout = new FileWriter(msgFile, true);
			bufMsg = new BufferedWriter(msgout);

			bufMsg.write("");
			bufMsg.write("\n");
			bufMsg.flush();


		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			if (msgFile != null) {
				try {
					bufMsg.close();
					msgout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	public static void writeOutFile() {
		/*
		 * Writes the processed updates to OutUpdates.txt
		 */

		File msgFile = null;
		FileWriter msgout = null;
		BufferedWriter bufMsg = null;

		try {

			msgFile = new File("./OutUpdates.txt");

			msgout = new FileWriter(msgFile, true);
			bufMsg = new BufferedWriter(msgout);

			// write the sequence number
			bufMsg.write("");
			bufMsg.write(seqNum + "\n");
			bufMsg.flush();

			// write the hops and its corresponding update cost
			for (String key : dvrouter.keySet()) {
				RoutingTableType obj = dvrouter.get(key);

				bufMsg.write("");
				bufMsg.write(key + "; " + obj.nextHop + "; " + obj.totalCost + "\n");
				bufMsg.flush();
			}

			// add new line after each sequence update
			bufMsg.write("");
			bufMsg.write("\n");
			bufMsg.flush();

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			if (msgFile != null) {
				try {
					bufMsg.close();
					msgout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void clearLogFile(String fileName) {

		/*
		 * Clears the text file if exists, creates a file if it doesn't exist.
		 * Used to clear OutUpdates.txt
		 */

		File file = new File(fileName);

		if (file.exists()) {
			try {
				FileWriter fw = new FileWriter(file.getAbsolutePath());
				PrintWriter pw = new PrintWriter(fw);
				pw.write("");
				pw.flush();
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {

		clearLogFile("OutUpdates.txt");

		List<String> updateList = new ArrayList<String>();
		updateList = readRoutingTable("InUpdates.txt");

<<<<<<< HEAD
		for (int i = 0; i < updateList.size(); i++) {
			dvUpdate.clear();
			dvUpdateTable(updateList.get(i));
=======
			//Set<String> keyss = dvrouter.keySet();
			System.out.println("Values: " + dvrouter.get(i));
>>>>>>> 2a08e6c... wrote outupdates func
		}
		
		System.out.println(update);
	}
}