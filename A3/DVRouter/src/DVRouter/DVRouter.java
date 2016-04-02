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
	private static HashMap<String, RoutingTable> dvrouter = new HashMap<String, RoutingTable>();
	
	//DVUpdate
	private static HashMap<String, Integer> dvUpdate = new HashMap<String, Integer>();
	

	protected class RoutingTable {
		String destination;
		String nextHop;
		int totalCost;

		public RoutingTable(String destination, String nextHop, int totalCost) {
			this.destination = destination;
			this.nextHop = nextHop;
			this.totalCost = totalCost;
		}
	}

	public static List<String> readRoutingTable(String filename) {

		File file = null;
		FileReader readFile = null;
		BufferedReader bufRead = null;
		// ArrayList<String> content = new ArrayList<String>();
		String[] hopCost = null;

		int sequenceNum = 0;
		String routerID = null;
		List<String> updateList = new ArrayList<String>();

		try {
			file = new File(filename);
			readFile = new FileReader(file);
			bufRead = new BufferedReader(readFile);

			String line;
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
		//System.out.print(updateList);
		return updateList;
	}

	public static RoutingTable updateRoutingTable(String routerID, String[] hopCost) {

		RoutingTable rt = null;
		DVRouter dv = new DVRouter();

		// Create object
		rt = dv.new RoutingTable(routerID, hopCost[0],Integer.parseInt(hopCost[1].trim()));
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
		
		dvUpdate.put(routerID, 0);
		
		for (int i = 2; i < attributes.length; i++){
			String [] dvKeys = attributes[i].split(";");
			int cost = Integer.parseInt(dvKeys[1].trim());
			dvUpdate.put(dvKeys[0], cost);
		}
		System.out.println(dvUpdate.keySet());
		}
	
	public static void routingTable(){
		
	}

	public static void main(String[] args) {
		
		List<String> updateList = new ArrayList<String>();
		updateList = readRoutingTable("InUpdates.txt");
		dvUpdateTable(updateList.get(0));
		
		//System.out.println(updateList);
		/*for (int i=0; i < updateList.size(); i++){
			dvUpdateTable(updateList.get(i));
		}*/
		
		
		/*for (String key: dvrouter.keySet()) {
			RoutingTable obj = dvrouter.get(key);
			System.out.println("Key:" + key + " Values:" + obj.nextHop + " " + obj.totalCost);
		}*/
	}
}
