package VRouter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VRouter {

	private static HashMap<String, Integer> interfaces = new HashMap<String, Integer>();
	private static HashMap<String, String> forwardingTable = new HashMap<String, String>();

	protected class IP4Packet {
		int version;
		int ihl;
		int tos;
		int totalLen;
		int id;
		String flags;
		int fragOffset;
		int ttl;
		int protocol;
		String checksum;
		String sourceAddr;
		String destAddr;

		public IP4Packet(int version, int ihl, int tos, int totalLen, int id,
				String flags, int fragOffset, int ttl, int protocol,
				String checksum, String sourceAddr, String destAddr) {

			this.version = version;
			this.ihl = ihl;
			this.tos = tos;
			this.totalLen = totalLen;
			this.id = id;
			this.flags = flags;
			this.fragOffset = fragOffset;
			this.ttl = ttl;
			this.protocol = protocol;
			this.checksum = checksum;
			this.sourceAddr = sourceAddr;
			this.destAddr = destAddr;
		}

		public void setIhl(int ihl) {
			this.ihl = ihl;
		}

		public void setTos(int tos) {
			this.tos = tos;
		}

		public void setTotalLen(int totalLen) {
			this.totalLen = totalLen;
		}

		public void setId(int id) {
			this.id = id;
		}

		public void setFlags(String flags) {
			this.flags = flags;
		}

		public void setFragOffset(int fragOffset) {
			this.fragOffset = fragOffset;
		}

		public void setTtl(int ttl) {
			this.ttl = ttl;
		}

		public void setProtocol(int protocol) {
			this.protocol = protocol;
		}

		public void setChecksum(String checksum) {
			this.checksum = checksum;
		}

		public void setSourceAddr(String sourceAddr) {
			this.sourceAddr = sourceAddr;
		}

		public void setDestAddr(String destAddr) {
			this.destAddr = destAddr;
		}

		public String getChecksumBin() {
			String checksumVal = this.checksum.replace("-", "");
			return checksumVal;
		}

	}

	public static List<IP4Packet> incomingPackets(String fileName) {

		List<IP4Packet> ip4Packets = new ArrayList<IP4Packet>();

		// read the file and store each pack in a list
		BufferedReader br = null;

		// String file = "./" + fileName;

		String line = null, content = null;
		List<String> packetItems = new ArrayList<String>();
		try {
			br = new BufferedReader(new FileReader(fileName));

			while ((line = br.readLine()) != null) {
				if (line.isEmpty()) {
					if (!content.isEmpty()) {
						packetItems.add(content);
					}
					content = null;

				} else {
					if (content == null) {
						content = line;
					} else {
						content += " " + line;
					}
				}
			}
			packetItems.add(content); // add the last packet to list array

			IP4Packet ip4 = null;
			for (int i = 0; i < packetItems.size(); i++) {
				// create instance of IP4Packet and put them into an ordered
				// list for processing
				ip4 = createPacket(packetItems.get(i));
				ip4Packets.add(ip4);

			}

			br.close();

		} catch (IOException e) {
			e.printStackTrace();

		}
		return ip4Packets;
	}

	private static IP4Packet createPacket(String string) {

		/*
		 * Helper function for incomingPackets. Returns an IP4Packet with all
		 * the attributes set.
		 */

		IP4Packet ip;

		String[] chars = null;
		chars = string.replaceAll(";", "").split(" ");

		VRouter vr = new VRouter();

		ip = vr.new IP4Packet(Integer.parseInt(chars[0]),
				Integer.parseInt(chars[1]), Integer.parseInt(chars[2]),
				Integer.parseInt(chars[3]), Integer.parseInt(chars[4]),
				chars[5], Integer.parseInt(chars[6]),
				Integer.parseInt(chars[7]), Integer.parseInt(chars[8]),
				chars[9], chars[10], chars[11]);

		// Inet4Address srcAddr = null, destAddr = null;
		//
		// try {
		// srcAddr = (Inet4Address) Inet4Address.getByName(ip.sourceAddr);
		// destAddr = (Inet4Address) Inet4Address.getByName(ip.destAddr);
		// } catch (UnknownHostException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//

		// dropPacket(ip.sourceAddr, ip.destAddr, ip.id,
		// "Testing out the drop packet");

		// readInterfaces(ip.destAddr);
		// checksum(ip);
		// readInterfaces();

		return ip;

	}

	public static String checksum(IP4Packet ip4packet) {

		/*
		 * Returns the checksum calculated for the given ip4packet.
		 */
		String checksumOriginal = ip4packet.checksum;
		// Set checksum field to 0
		ip4packet.checksum = "0";

		// Call helper method convertHex that parses the ip4packet and
		// returns a list where each element is a 4byte hexadecimal
		ArrayList<String> hexList = convertHex(ip4packet);

		// Convert each 4byte hexadecimal value into 16 bit binary values
		ArrayList<String> binList = new ArrayList<String>();
		for (int i = 0; i < hexList.size(); i++) {
			int b = Integer.parseInt(hexList.get(i), 16);
			String bin = addPadding(Integer.toBinaryString(b), 16, "0");
			binList.add(bin);
		}

		// Call helper method getSum that finds the sum of all the 16 bit binary
		// values with the carry
		String checksumValue = getSum(binList);

		// Find one's complement of the sum
		String c1 = checksumValue.replace("0", ">");
		String c2 = c1.replace("1", "0");
		String checksumFinal = c2.replace(">", "1");

		// System.out.println(checksumFinal);

		ip4packet.checksum = checksumOriginal;

		return checksumFinal;

	}

	public static ArrayList<String> convertHex(IP4Packet ip4packet) {

		/*
		 * Helper function for checksum. Parses all attributes of the given
		 * ip4packet and converts them into hexadecimal values based on
		 * specifications given in IP4Header.pdf Returns an ArrayList of strings
		 * where each element is a 4 byte hexadecimal value.
		 */

		ArrayList<String> hexList = new ArrayList<String>();

		// Convert each ip4packet field to hex and use helper function
		// addPadding to add appropriate padding for each field.
		String hex1 = addPadding(Integer.toHexString(ip4packet.version), 1, "0");
		hex1 += addPadding(Integer.toHexString(ip4packet.ihl), 1, "0");
		hex1 += addPadding(Integer.toHexString(ip4packet.tos), 2, "0");

		String hex2 = addPadding(Integer.toHexString(ip4packet.totalLen), 4,
				"0");

		String hex3 = addPadding(Integer.toHexString(ip4packet.id), 4, "0");

		String b1 = addPadding(ip4packet.flags, 3, "0");
		String b2 = addPadding(Integer.toBinaryString(ip4packet.fragOffset),
				13, "0");
		String bFinal = b1 + b2;
		String hex4 = Integer.toHexString(Integer.parseInt(bFinal, 2));

		String hex5 = addPadding(Integer.toHexString(ip4packet.ttl), 2, "0");
		hex5 += addPadding(Integer.toHexString(ip4packet.protocol), 2, "0");

		String hex6 = addPadding(ip4packet.checksum, 4, "0");

		// Use helper function ipToHex to convert ip address into hex.
		String s = ipToHex(ip4packet.sourceAddr);

		String hex7 = s.substring(0, 4);
		String hex8 = s.substring(4, 8);

		String s1 = ipToHex(ip4packet.destAddr);
		String hex9 = s1.substring(0, 4);
		String hex10 = s1.substring(4, 8);

		hexList.add(hex1);
		hexList.add(hex2);
		hexList.add(hex3);
		hexList.add(hex4);
		hexList.add(hex5);
		hexList.add(hex6);
		hexList.add(hex7);
		hexList.add(hex8);
		hexList.add(hex9);
		hexList.add(hex10);

		return hexList;

	}

	public static String addPadding(String s, int len, String pad) {
		/*
		 * Helper function for convertHex. Returns a string with the appropriate
		 * padding added to the start of the given input with the given length.
		 */
		while (s.length() < len) {
			s = pad + s;
		}
		return s;
	}

	public static String ipToHex(String ipAddress) {

		/*
		 * Helper function for converHex. Returns hexadecimal string which is a
		 * conversion of the given ipAddress (source/destination) of an
		 * ip4packet.
		 */

		// Split the given ipAddress into string array.
		String[] ipAddressInArray = ipAddress.split("\\.");

		// Multiply each element in ipAddressInArray by 256^n where n=3,..0 and
		// calculate the sum
		long result = 0;
		for (int i = 0; i < ipAddressInArray.length; i++) {

			int power = 3 - i;
			int ip = Integer.parseInt(ipAddressInArray[i]);
			result += ip * Math.pow(256, power);

		}

		// Convert the calculated sum to hexadecimal
		String final_hex = BigInteger.valueOf(result).toString(16);

		return final_hex;
	}

	public static String getSum(ArrayList<String> binList) {

		/*
		 * Helper function for checksum. Returns a binary string which is the
		 * sum(with carry) of the given binList.
		 */

		BigInteger sum = new BigInteger("0");
		for (int i = 0; i < binList.size(); i++) {
			sum = sum.add(new BigInteger(binList.get(i), 2));

			// Splits the sum by removing the carry once the sum goes past 16
			// bits
			if (sum.toString(2).length() > 16) {
				// System.out.println("Initial Sum: " + sum.toString(16));
				String s1 = sum.toString(2)
						.substring(sum.toString(2).length() - 16,
								sum.toString(2).length());
				String s2 = sum.toString(2).substring(0,
						sum.toString(2).length() - 16);

				BigInteger num1 = new BigInteger(s1, 2);
				BigInteger num2 = new BigInteger(s2, 2);
				sum = num1.add(num2);
			}

		}
		String value = addPadding(sum.toString(2), 16, "0");

		// System.out.println("To hex:" + value);
		return value;

	}

	public static List<IP4Packet> fragment(IP4Packet ip4packet, int MTU) {

		List<IP4Packet> fragments = new ArrayList<IP4Packet>();

		// If MTU is at least as great as the size of ip4packet,
		// return ip4packet as is since no fragmentation is needed
		if (ip4packet.totalLen < MTU) {
			System.out
					.println("Size of packet is < MTU, no fragmentation needed: "
							+ ip4packet.totalLen);
			fragments.add(ip4packet);
			return fragments;
		}

		// if DF is set to 1, drop the packet an return [] list
		char[] bits = String.valueOf(ip4packet.flags).toCharArray();
		if (bits[1] == '1') {
			System.out.println("DF Field set!");
			dropPacket(ip4packet.sourceAddr, ip4packet.destAddr, ip4packet.id,
					"Fragmentation needed and DF set");
			return fragments;
		}

		IP4Packet newIPacket = null;
		VRouter vr = new VRouter();

		// Get number of fragments needed for the packet
		double numOfFragments = Math.ceil((float) (ip4packet.totalLen - 20)
				/ (MTU - 20));

		// convert number of fragment to interger
		int packetNumber = (int) numOfFragments;

		// keep track of size and counter for each packet
		int sizeOfEachPacket = 0;
		int count = 1;

		// get Offset value
		int offset = (MTU - 20) / 8;

		while (sizeOfEachPacket < ip4packet.totalLen) {

			System.out.println("count " + count + "   packetNumber: "
					+ packetNumber);

			// generate a new IP4Packet with new size
			if (count < packetNumber) {

				newIPacket = vr.new IP4Packet(ip4packet.version, ip4packet.ihl,
						ip4packet.tos, MTU - 20, ip4packet.id, ip4packet.flags,
						0, ip4packet.ttl, ip4packet.protocol,
						ip4packet.checksum, ip4packet.sourceAddr,
						ip4packet.destAddr);

				// if its first packet, set the offset to 0
				if (count == 1) {
					newIPacket.fragOffset = 0;
				}

				// else set the offet based on the counter of the packet ( -1:
				// except the last one)
				else {
					newIPacket.fragOffset = offset * (count - 1);
				}

			}

			// if its last fragmented packet
			else {

				// Calculate the size of last fragmented packet
				int sizeOfLastPacket = ip4packet.totalLen
						- ((MTU - 20) * (packetNumber - 1));

				// set the flag of last fragmented packet to 000
				newIPacket = vr.new IP4Packet(ip4packet.version, ip4packet.ihl,
						ip4packet.tos, sizeOfLastPacket, ip4packet.id, "000",
						offset * (count - 1), ip4packet.ttl - 1,
						ip4packet.protocol, ip4packet.checksum,
						ip4packet.sourceAddr, ip4packet.destAddr);

			}

			// update the counter for all fragments and add each fragment to the
			// list
			count += 1;
			sizeOfEachPacket += (MTU - 20);
			fragments.add(newIPacket);
		}

		// Testing purposes
		for (int i = 0; i < fragments.size(); i++) {
			System.out.println(fragments.get(i).flags + "---"
					+ fragments.get(i).totalLen + "......."
					+ fragments.get(i).fragOffset);
		}

		return fragments;

	}

	public static boolean dropPacket(String sourceAddr, String destAddr,
			int ID, String message) {

		String errMsg = "Packet " + ID + " from " + sourceAddr + " to "
				+ destAddr + ": " + message;

		if (writeToFile("Messages.txt", errMsg)) {
			return true;
		}
		return false;
	}

	public static boolean writeToMessageFile(IP4Packet ip) {

		String msg = "Packet from " + ip.sourceAddr
				+ "destined for this router successfully received: " + ip.id;
		if (writeToFile("Messages.txt", msg)) {
			return true;
		}
		return false;

	}

	public static boolean writeToFile(String fileName, String input) {

		File msgFile = null;
		FileWriter msgout = null;
		BufferedWriter bufMsg = null;

		try {

			msgFile = new File(fileName);

			msgout = new FileWriter(msgFile, true); // remove append
			bufMsg = new BufferedWriter(msgout);

			bufMsg.write("");
			bufMsg.write(input + "\n");
			bufMsg.flush();

			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;

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

	/*
	 * 
	 * store all interfaces in a hash map after reading from file split ; - and
	 * first two values and store the last one - generates the prefix: ipAdd &
	 * mask
	 * 
	 * below function should just return true or false
	 */
	public static void readInterfaces() {

		File file = null;
		FileReader readFile = null;
		BufferedReader bufRead = null;

		try {
			file = new File("./Interfaces.txt");
			readFile = new FileReader(file);
			bufRead = new BufferedReader(readFile);

			String line;
			String[] content;
			while ((line = bufRead.readLine()) != null) {
				content = line.split(";"); // escape ;

				int mtus = Integer.parseInt(content[2]);

				//String maskedIp = ipAddressMask(content[0], content[1]);
				// System.out.println(maskedIp);
				interfaces.put(content[0] + ";" + content[1], mtus);

			}

			// printing HashMap Keys:Values
			for (String name : interfaces.keySet()) {

				String key = name;
				Integer value = interfaces.get(name);
				System.out.println("Interfaces Keys= "+key + ": Interfaces value= " + value);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (file != null) {
				try {
					bufRead.close();
					readFile.close();

				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		}
	}

	public static String ipAddressMask(String ipAddress, String mask) {

		String[] ipOctets = ipAddress.split("\\.");
		// System.out.println(ipOctets);
		// System.out.println(mask);
		int maskLen = Integer.parseInt(mask);
		String result = "";
		for (int i = 0; i < ipOctets.length; i++) {
			result += convertToBinary(ipOctets[i]);
		}
		// System.out.println(result);
		return result.substring(0, maskLen);

	}

	public static String convertToBinary(String input) {

		int b = Integer.parseInt(input);
		String bin = addPadding(Integer.toBinaryString(b), 8, "0");
		return bin;
	}

	public static boolean lookupInterfaces(String ipAddress) {
		if (interfaces.containsKey(ipAddress)) {
			System.out.println("FOUND IT!");
			return true;
		}
		return false;

	}

	public static void readFowardingTable() {

		File file = null;
		FileReader readFile = null;
		BufferedReader bufRead = null;

		try {
			file = new File("./ForwardingTable.txt");
			readFile = new FileReader(file);
			bufRead = new BufferedReader(readFile);

			String line;
			String[] content;
			while ((line = bufRead.readLine()) != null) {
				content = line.split(";");
				String mask = maskToInt(content[1]);
				String key = ipAddressMask(content[0], mask);

				forwardingTable.put(key, content[2] + " " + content[3]);

			}
			for (String name : forwardingTable.keySet()) {

				String key = name;
				String value = forwardingTable.get(name);
				//System.out.println(key + " : " + value);
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

	public static String maskToInt(String subnet) {
		String[] subnetOctets = subnet.split("\\.");
		int count = 0;
		for (int i = 0; i < subnetOctets.length; i++) {
			if (subnetOctets[i].equals("255")) {
				count += 1;
			}
		}

		// handling Class-A
		if (count == 1) {
			return "8";
		}

		// handling Class-B
		else if (count == 2) {
			return "16";
		}

		// handling Class-C
		else {
			return "24";
		}
	}

	public static String lookupDest(String ipAddress) {

		String matchedInterfaceIP = null;
		// convert destinationIPAddress to binary only up to longest prefix
		// length
		String destAddrInBinary = ipAddressMask(ipAddress, "32");
		System.out.println(destAddrInBinary);
		int longestMatch = 0;

		for (String key : forwardingTable.keySet()) {

			// Longest prefix length: if IPAdd in FwdTable is class-A: length=8,
			// B=16, C=24
			int keyLen = key.length();
			// match the longest prefixed length destinationIP with keys in FWD
			// HashMap
			if (key.equals(destAddrInBinary.substring(0, keyLen)) && longestMatch < keyLen) {
				// System.out.println("Found a match! " + ipAddress + " " +
				// destAddrInBinary);
				// System.out.println(key + " === " + forwardingTable.get(key));
				matchedInterfaceIP = forwardingTable.get(key);
				System.out.println(key);
			}
		}
		
		return matchedInterfaceIP;
	}
	
	/*
	 * Forward the packet to its destination and write it to OutPackets.txt
	 */
	
	public static boolean forward(IP4Packet ip4packet, String interfaceIP) {
		
		String checksum = checksum(ip4packet); 
		char[] checksumBits = String.valueOf(checksum).toCharArray();

		// need to format the checksum 
		
		ip4packet.checksum = checksum;
		
		String writeToOutPacket = ip4packet.version + "; " + ip4packet.ihl + "; " 
				+ ip4packet.tos + "; " + ip4packet.totalLen + "\n" 
				+ ip4packet.id + "; " + ip4packet.flags + "; " + ip4packet.fragOffset + "\n" 
				+ ip4packet.ttl + "; " + ip4packet.protocol + "; " + ip4packet.checksum + "\n"
				+ ip4packet.sourceAddr + "\n" + ip4packet.destAddr + "\n" + interfaceIP + "\n";
		
		return writeToFile("OutPackets.txt", writeToOutPacket);
		
	}

	public static void main(String[] args) {

		List<IP4Packet> ip4Packets = incomingPackets("InPackets.txt");
		readInterfaces();
		readFowardingTable();

		for (int i = 0; i < ip4Packets.size(); i++) {
			IP4Packet ip = ip4Packets.get(i);
			String checksum = checksum(ip);

			if (!ip.getChecksumBin().equals(checksum)) {
				dropPacket(ip.sourceAddr, ip.destAddr, ip.id, "Checksum Error");
			} else {
				if (lookupInterfaces(ip.destAddr)) {
					writeToMessageFile(ip);
				}

				String interfaceIP = lookupDest(ip.destAddr);
				if (interfaceIP != null) {

					// decrement TTL
					ip.ttl = ip.ttl - 1;
					if (ip.ttl <= 0) {

						dropPacket(ip.sourceAddr, ip.destAddr, ip.id,
								"TTL exceeded");
					}

					// need to convert values of Forward HashMap 
					// to same type as keys in Interfaces HashMap
					//String[] matchInterfaceIP = interfaceIP.split(" ");
					
					// convert the IPAddress and IntMask to binary so we can match 
					// in interfaces HashMap to get MTU
					//String ipForFragmenting = ipAddressMask(matchInterfaceIP[0], maskToInt(matchInterfaceIP[1]));
//					System.out.println("From LookUpDestination: "+interfaceIP + " -> " + ipForFragmenting 
//							+ " \nFrom Interfaces HashMap " + matchInterfaceIP[0] + " = " + interfaces.get(ipForFragmenting));
//
//					fragment(ip, interfaces.get(ipForFragmenting));

					forward(ip,  interfaceIP); 
				}

				else {
					dropPacket(ip.sourceAddr, ip.destAddr, ip.id,
							"Destination not found");
				}

			}
		}
	}
}
