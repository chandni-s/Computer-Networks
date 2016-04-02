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

/**
 * @Name Chandni Sehgal StudentNo: 998973375
 * @Name Srihitha Maryada StudentNo: Infinity :)
 *
 */
public class DVRouter {

	// Destination <NextHop:Cost>
	private static HashMap<String, RoutingTable> dvrouter = new HashMap<String, RoutingTable>();

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

	public static void readRoutingTable(String filename) {

		File file = null;
		FileReader readFile = null;
		BufferedReader bufRead = null;
		// ArrayList<String> content = new ArrayList<String>();
		String[] hopCost = null;

		int sequenceNum = 0;
		String routerID = null;

		try {
			file = new File(filename);
			readFile = new FileReader(file);
			bufRead = new BufferedReader(readFile);

			String line;

			int count = 0;
			while ((line = bufRead.readLine()) != null) {

				// Get the sequence number
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

						// Create object
						RoutingTable rt = updateRoutingTable(routerID, hopCost);
						// put routerID and Routing object into hashMap
						
					}
					if (line.length() == 0) {
						count = 0;
					}
				}
			}

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

	public static void main(String[] args) {

		readRoutingTable("InUpdates.txt");
		for (int i=0; i<dvrouter.size(); i++) {

			System.out.println("Values: " + dvrouter.get(i));
		}
	}
}
