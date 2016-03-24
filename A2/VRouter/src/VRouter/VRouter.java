package VRouter;

/*
 * Name: Chandni Sehgal, Student-No: 998973375
 * Name: Srihitha Maryada Student-No: 999829164
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/*
 * Assumptions: 
 * Fragmentation - if fields are 000, it means we "may" fragment but it also
 * means that its last packet. However, its packetSize > MTU - the packet gets
 * fragmented and Flgas remains same as original EXCEPT the flag for last 
 * fragment gets explicity set to "0" indicating that this is last fragmented
 * packet.
 * 
 * A question was asked on Piazza for clarification - however, no response was
 * given
 */

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

			// add the last packet to list array
			packetItems.add(content);

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

	public static List<IP4Packet> fragment(IP4Packet ip4packet, int MTU) {

		/*
		 * Checks the outgoing link bandwidth for MTU (maximum transmission
		 * unit.) If link MTU is less than the packet size, fragments the packet
		 * by invoking fragment(ip4packet, MTU).
		 */

		List<IP4Packet> fragments = new ArrayList<IP4Packet>();

		// If MTU is at least as great as the size of ip4packet,
		// return ip4packet as is since no fragmentation is needed
		if (ip4packet.totalLen == MTU) {
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

		double numOfFragments = Math
				.ceil((float) (ip4packet.totalLen / (MTU - 20.00)));

		// convert number of fragment to integer
		int packetNumber = (int) numOfFragments;

		// keep track of size and counter for each packet
		int sizeOfEachPacket = 0;
		int count = 1;

		// get Offset value
		int offset = (MTU - 20) / 8;

		while (sizeOfEachPacket < ip4packet.totalLen) {

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

		/*
		 * Writes a String message to Messages.txt file
		 */

		String errMsg = "Packet " + ID + " from " + sourceAddr + " to "
				+ destAddr + ": " + message;

		return writeToFile("Messages.txt", errMsg);

	}

	public static boolean forward(IP4Packet ip4packet, String interfaceIP) {

		/*
		 * Forward the packet to its destination and write it to OutPackets.txt
		 */

		String checksum = checksum(ip4packet);

		// format the checksum to have 1111-0000-1111-0000
		// instead of 1111000011110000
		StringBuilder finalChecksum = new StringBuilder(checksum);
		for (int i = 0; i < checksum.length() - 1; i += 5) {
			finalChecksum.insert(i + 4, "-");
		}

		ip4packet.checksum = finalChecksum.toString();

		// create new packet
		String writeToOutPacket = ip4packet.version + "; " + ip4packet.ihl
				+ "; " + ip4packet.tos + "; " + ip4packet.totalLen + "\n"
				+ ip4packet.id + "; " + ip4packet.flags + "; "
				+ ip4packet.fragOffset + "\n" + ip4packet.ttl + "; "
				+ ip4packet.protocol + "; " + ip4packet.checksum + "\n"
				+ ip4packet.sourceAddr + "\n" + ip4packet.destAddr + "\n"
				+ interfaceIP + "\n";

		// write them to OutPackets
		return writeToFile("OutPackets.txt", writeToOutPacket);

	}

	public static boolean lookupInterfaces(String ipAddress) {

		/*
		 * Looks up the IPaddress passed in, returns true if it is one of
		 * VRouter’s interfaces, false otherwise.
		 */

		for (String key : interfaces.keySet()) {
			String[] ipAndMask = key.split(";");
			if (ipAddress.equals(ipAndMask[0])) {
				return true;
			}
		}
		return false;

	}

	public static String lookupDest(String ipAddress) {

		/*
		 * Looks up the IPaddress passed in the forwarding table by longest
		 * prefix match. If finds a match, then returns the matching interface.
		 * Otherwise, returns null.
		 */

		String matchedInterfaceIP = null;

		// convert destinationIPAddress to binary only up to longest prefix
		// length
		String destAddrInBinary = ipAddressMask(ipAddress, "32");

		int longestMatch = 0;

		// go through all keys in forward HashMap and performs
		// the longest prefix match on destinationIPAddress
		for (String key : forwardingTable.keySet()) {

			int keyLen = key.length();
			if (key.equals(destAddrInBinary.substring(0, keyLen))
					&& longestMatch < keyLen) {
				matchedInterfaceIP = forwardingTable.get(key);
				longestMatch = keyLen;
			}
		}

		return matchedInterfaceIP;
	}

	/*
	 * ALL HELPER METHODS
	 */

	public static void clearLogFile(String fileName) {

		/*
		 * Clears the text file if exists, creates a file if it doesn't exist.
		 * Used to clear Messages.txt and OutPackets.txt
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
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void readInterfaces() {

		/*
		 * Read Interfaces.txt and store the Interface-IP and masks as keys, and
		 * MTU as Value, in interfaces HashMap
		 */

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
				content = line.trim().split(";");

				int mtus = Integer.parseInt(content[2].trim());

				interfaces.put(content[0].trim() + ";" + content[1].trim(), mtus);

			}

			// Testing purposes: printing HashMap Keys:Values
			for (String name : interfaces.keySet()) {

				String key = name;
				Integer value = interfaces.get(name);
				System.out.println("Interfaces Keys= " + key
						+ ": Interfaces value= " + value);
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

	public static void readFowardingTable() {

		/*
		 * Read Forward.txt and store the IPAddress and masks as keys, and
		 * InterfaceIPAddress-Maks as Value, in forward HashMap
		 */

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
				content = line.trim().split(";");

				String key = ipAddressMask(content[0].trim(), content[1].trim());

				forwardingTable.put(key, content[2].trim() + ";" + content[3].trim());

			}

			// Testing purposes: print forwarding table
			for (String name : forwardingTable.keySet()) {

				String key = name;
				String value = forwardingTable.get(name);
				System.out.println(key + " : " + value);
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

		return ip;

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

	public static boolean writeToMessageFile(IP4Packet ip) {

		/*
		 * Helper function that writes successful messages to Message.txt
		 */

		String msg = "Packet from " + ip.sourceAddr + " destined for this "
				+ "router successfully received: " + ip.id;

		return writeToFile("Messages.txt", msg);

	}

	public static boolean writeToFile(String fileName, String input) {

		/*
		 * A helper function that creates a file and writes the message to it
		 */

		File msgFile = null;
		FileWriter msgout = null;
		BufferedWriter bufMsg = null;

		try {

			msgFile = new File(fileName);

			msgout = new FileWriter(msgFile, true);
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

	public static String ipAddressMask(String ipAddress, String mask) {

		/*
		 * A helper function that takes in an IP Address and mask associated
		 * with it (182.30.45.5 255.255.0.0) and returns
		 */

		String[] ipOctets = ipAddress.split("\\.");
		int maskLen = Integer.parseInt(mask);
		String result = "";

		for (int i = 0; i < ipOctets.length; i++) {
			result += convertToBinary(ipOctets[i]);
		}

		return result.substring(0, maskLen);

	}

	public static String convertToBinary(String input) {

		/*
		 * Helper function that converts a string to binary
		 */

		int b = Integer.parseInt(input);
		String bin = addPadding(Integer.toBinaryString(b), 8, "0");
		return bin;
	}

	public static boolean checkTTL(IP4Packet ip4packet) {

		/*
		 * Reduces the time to live for packet by 1 if packet is not found in
		 * lookupInterfaces
		 * 
		 * Checks the time to live for VRouter's packet if time < 0, the packet
		 * is dropped
		 */

		ip4packet.ttl = ip4packet.ttl - 1;
		if (ip4packet.ttl < 0) {
			dropPacket(ip4packet.sourceAddr, ip4packet.destAddr, ip4packet.id,
					"TTL exceeded");
			System.out.println("TTL exceeded");
			return false;
		}

		return true;

	}

	public static int getMTU(String interfaceIp) {

		/*
		 * Helper function for main() Get MTU values from interfaces HashMap
		 * based on Interface IP Address and Mask returned by lookupDest
		 */

		int mtu = 0;
		for (String key : interfaces.keySet()) {
			if (interfaceIp.equals(key)) {
				mtu = interfaces.get(key);
			}
		}

		return mtu;
	}

	public static void main(String[] args) {

		// Clears log files if it already exists
		clearLogFile("Messages.txt");
		clearLogFile("OutPackets.txt");

		// Parses InPackets.txt and adds packet to a list
		List<IP4Packet> ip4Packets = incomingPackets("InPackets.txt");

		readInterfaces();
		readFowardingTable();
		System.out.println("\n#of Packets :" + ip4Packets.size());

		for (int i = 0; i < ip4Packets.size(); i++) {
			IP4Packet ip = ip4Packets.get(i);
			System.out.println("\nPacket: " + (i + 1) + " Dest Addr: "
					+ ip.destAddr);
			String checksum = checksum(ip);

			if (!ip.getChecksumBin().equals(checksum)) {
				dropPacket(ip.sourceAddr, ip.destAddr, ip.id, "Checksum Error");
				System.out.println("Packet " + (i + 1) + " :Checksum fails");
			} else {
				if (lookupInterfaces(ip.destAddr)) {
					System.out.println("Packet " + (i + 1)
							+ " :LookupInterface succesful");
					writeToMessageFile(ip);
					continue;
				}

				checkTTL(ip);

				String interfaceIP = lookupDest(ip.destAddr);
				System.out.println("Longest Match interface: " + interfaceIP);
				if (interfaceIP != null) {

					// Get MTU of matching interface
					int interfaceMtu = getMTU(interfaceIP);
					System.out.println("Interface MTU: " + interfaceMtu);

					if (interfaceMtu < ip.totalLen && interfaceMtu != 0) {
						System.out.println("Needs fragmentation");
						List<IP4Packet> packetFragments = fragment(ip,
								interfaces.get(interfaceIP));
						for (IP4Packet p : packetFragments) {
							System.out.println("Fragments: " + p.id);
							forward(p, interfaceIP);
						}
						continue;
					}

					System.out.println("No fragment needed");
					forward(ip, interfaceIP);
					continue;

				}

				else {
					dropPacket(ip.sourceAddr, ip.destAddr, ip.id,
							"Destination not found");
				}
			}
		}
	}
}
