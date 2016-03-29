package DVRouter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * @Name Chandni Sehgal StudentNo: 998973375
 * @Name Srihitha Maryada StudentNo: Infinity :)
 *
 */
public class DVRouter {
	
	
	// Destination <NextHop:Cost>
	private static HashMap<String, RoutingTable> dvrouter = new HashMap<String, RoutingTable>();
	
	protected class RoutingTable
	{
		String nextHop;
		int totalCost;
		
		public RoutingTable(String nextHop, int totalCost) {
			this.nextHop = nextHop;
			this.totalCost = totalCost;
		}
	}
	
	public static void readRoutingTable(String filename) {

		File file = null;
		FileReader readFile = null;
		BufferedReader bufRead = null;
		String[] content = null;

		try {
			file = new File(filename);
			readFile = new FileReader(file);
			bufRead = new BufferedReader(readFile);

			String line;
			
			while ((line = bufRead.readLine()) != null) {
				
				updateRoutingTable(line);
//				if (line.contains(";")) {
//					content = line.split(";");
//					updateRoutingTable(content);
//				}
//				// get destination i.e A
//				else {
//					System.out.println(line.split(" ")[0]);
//				}
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
	
	public static RoutingTable updateRoutingTable(String content) {
		
		String[] hopCost = null;
		RoutingTable rt = null;
		DVRouter dv = new DVRouter();
		String routerID = null;
		
			if (content.contains(";")) {
				hopCost = content.split(";");
				System.out.println("Hostcost: " + hopCost[0] + hopCost[1]);
				//rt = dv.new RoutingTable(hopCost[0].trim(), Integer.parseInt(hopCost[1].trim()));
			}
			else {
				System.out.println("blahhhh "+content.split(" ")[0]);
				routerID = content.split(" ")[0];
				
			}
		
		// put keys: Destination, Values: <NextHop-totalCost> 
		//dvrouter.put(routerID, dv.new RoutingTable(rt.nextHop, rt.totalCost));
		
		return rt;
	}

	
	public static void main(String[] args) {
		
		readRoutingTable("InUpdates.txt");
	}
}
