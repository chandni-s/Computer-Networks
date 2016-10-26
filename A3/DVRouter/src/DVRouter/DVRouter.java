package DVRouter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
<<<<<<< HEAD
import java.util.List;

/**
 * @Name Chandni Sehgal StudentNo: 998973375
 * @Name Srihitha Maryada StudentNo: 999829164
 *
 */
public class DVRouter {
	
	protected static String seqNum = null;
	protected static String routerID = null;

	// Routing Table = Destination: <Next Hop, Cost>
	private static HashMap<String, RoutingTableType> dvrouter = new HashMap<String, RoutingTableType>();
	
	//DVUpdate
	private static HashMap<String, Integer> dvUpdate = new HashMap<String, Integer>();
	

	protected class RoutingTableType {
		String destination;
		String nextHop;
		int totalCost;

		public RoutingTableType(String destination, String nextHop, int totalCost) {
			this.destination = destination;
=======

/**
 * @Name Chandni Sehgal StudentNo: 998973375
 * @Name Srihitha Maryada StudentNo: Infinity :)
 *
 */
public class DVRouter {

	// Destination <NextHop:Cost>
	private static HashMap<String, RoutingTable> dvrouter = new HashMap<String, RoutingTable>();

	protected class RoutingTable {
		String nextHop;
		int totalCost;

		public RoutingTable(String nextHop, int totalCost) {
>>>>>>> 9081eaa... Added A3
			this.nextHop = nextHop;
			this.totalCost = totalCost;
		}
	}

<<<<<<< HEAD
	public static List<String> readRoutingTable(String filename) {
=======
	public static void readRoutingTable(String filename) {
>>>>>>> 9081eaa... Added A3

		File file = null;
		FileReader readFile = null;
		BufferedReader bufRead = null;
		// ArrayList<String> content = new ArrayList<String>();
		String[] hopCost = null;

		int sequenceNum = 0;
		String routerID = null;
<<<<<<< HEAD
		List<String> updateList = new ArrayList<String>();
=======
>>>>>>> 9081eaa... Added A3

		try {
			file = new File(filename);
			readFile = new FileReader(file);
			bufRead = new BufferedReader(readFile);

			String line;
<<<<<<< HEAD
			String update = "";
			

			int count = 0;
			while ((line = bufRead.readLine()) != null) {
				if (line.length() != 0){
					update += line + "\n";
					//System.out.println(update);
				}
				else{
					//System.out.println(update);
					updateList.add(update);
					update = "";
				}
				

				/*// Get the sequence number
=======

			int count = 0;
			while ((line = bufRead.readLine()) != null) {

				// Get the sequence number
>>>>>>> 9081eaa... Added A3
				if (count == 0) {
					sequenceNum = Integer.parseInt(line);
					count += 1;
				}

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
<<<<<<< HEAD
						//System.out.println(hopCost[0]);

						// Create object
						//RoutingTable rt = updateRoutingTable(routerID, hopCost);
						// put routerID and Routing object into hashMap
						
					}
					if (line.length() == 0) {
						count = 0;
						//System.out.println("Setting null");
						dvUpdate = null;
					}
				}*/
				
			}
			updateList.add(update);
=======

						updateRoutingTable(routerID, hopCost);
					}
					if (line.length() == 0) {
						System.out.println("line is empty");
						count = 0;
					}
				}
			}
>>>>>>> 9081eaa... Added A3

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
<<<<<<< HEAD
		//System.out.print(updateList);
		return updateList;
	}

	public static RoutingTableType updateRoutingTable(String dest, int cost) {

		RoutingTableType rt = null;
		DVRouter dv = new DVRouter();

		// Create object
		rt = dv.new RoutingTableType(dest, routerID, cost);
		//System.out.println(rt.nextHop + " GOOOT " + rt.totalCost);
		
		
		dvrouter.put(rt.destination, rt);
		//System.out.println("Keys: " + dvrouter.keySet() + "Values: ");
		
		return rt;
	}
	
	public static void dvUpdateTable(String update){
		
		String [] attributes = update.split("\n");
		seqNum = attributes[0];
		routerID = attributes[1];
		//System.out.println(seqNum + ":" + routerID);
		
		//dvUpdate.put(routerID, 0);
		
		for (int i = 2; i < attributes.length; i++){
			String [] dvKeys = attributes[i].split(";");
			int cost = Integer.parseInt(dvKeys[1].trim());
			dvUpdate.put(dvKeys[0], cost);
		}
		/*for (String key: dvUpdate.keySet()) {
		//RoutingTable obj = dvrouter.get(key);
		System.out.println("Key:" + key + " Values:" + dvUpdate.get(key));
		}*/
		}
	
	public static void routingTable(){
		
		String dest;
		int costUpdate;
		int toDv = 0;
		int newCost = 0;
		
		for (String key: dvUpdate.keySet()){
			dest = key;
			costUpdate = dvUpdate.get(key);
			
			System.out.println("key " + key);
			
			if (key.equals("DV")){
				dest = routerID;
				toDv = dvUpdate.get(key);
				newCost = toDv;
				//System.out.println(newCost);	
			}
			
			if (dvrouter.containsKey(dest)){
				//System.out.println("Has Key: "+ dest);
				RoutingTableType rt = dvrouter.get(dest);
				int dvCost = rt.totalCost;
				System.out.println("DVCost " + dvCost);
				System.out.println("Update " + costUpdate);
				if (costUpdate < dvCost){
					//System.out.println("Update " + costUpdate);
					rt.nextHop = routerID;
					
					if (newCost == 0){
						newCost = toDv + costUpdate;
						rt.totalCost = newCost;
					}
					rt.totalCost = newCost;
					
					//System.out.println(costUpdate);
					
				}
			}
			
			else{
				//Add new RoutingTableType
				if (newCost == 0){
					newCost = toDv + costUpdate;
				}
				updateRoutingTable(dest, newCost);
				
			}
			newCost = 0;
			toDv = 0;
			costUpdate = 0;
		}
		for (String key: dvrouter.keySet()) {
			RoutingTableType obj = dvrouter.get(key);
			System.out.println("Key:" + key + " Values:" + obj.nextHop + " " + obj.totalCost);
		}
	}

	public static void main(String[] args) {
		
		List<String> updateList = new ArrayList<String>();
		updateList = readRoutingTable("InUpdates.txt");
		//dvUpdateTable(updateList.get(0));
		//routingTable();
		
		
		//System.out.println(updateList);
		for (int i=0; i < updateList.size(); i++){
			dvUpdateTable(updateList.get(i));
			routingTable();
		}
		
		
		
		/*for (String key: dvrouter.keySet()) {
			RoutingTable obj = dvrouter.get(key);
			System.out.println("Key:" + key + " Values:" + obj.nextHop + " " + obj.totalCost);
		}*/
=======
	}

	public static RoutingTable updateRoutingTable(String routerID, String[] hopCost) {

		RoutingTable rt = null;
		DVRouter dv = new DVRouter();

		String[] result = Arrays.toString(hopCost).split(",");

		
		// Create object
		rt = dv.new RoutingTable(result[0].toString().trim().replace("[", ""),
				Integer.parseInt(result[1].toString().trim().replace("]", "")));

		
		// Update the HashMap: Destination, Values: <NextHop-totalCost>
		dvrouter.put(routerID, dv.new RoutingTable(rt.nextHop, rt.totalCost));
		System.out.println(dvrouter.keySet());
		System.out.println(dvrouter.values().toString());

		return rt;
	}

	public static void main(String[] args) {

		readRoutingTable("InUpdates.txt");
>>>>>>> 9081eaa... Added A3
	}
}
