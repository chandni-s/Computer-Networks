package VRouter;

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

	}

	public static void main(String[] args) {
		incomingPackets("InPackets.txt");
		// readInterfaces();
		// lookupInterfaces("191.168.0.0");
		// readFowardingTable();
		// convertStringToBinary();

	}

	public static List<IP4Packet> incomingPackets(String fileName) {

		List<IP4Packet> ip4Packets = new ArrayList<IP4Packet>();

		// read the file and store each pack in a list
		FileReader fr = null;
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
		fragment(ip, 400);

		return ip;

	}

	public static String checksum(IP4Packet ip4packet) {

		// int to Hex
		// Hex to binary
		addInBinary("101", "100"); // 5+4=9:1001

		return null;
	}

	private static String addInBinary(String string, String string2) {

		int i1 = Integer.parseInt(string, 2);
		int i2 = Integer.parseInt(string2, 2);

		String checksum = Integer.toBinaryString(i1 + i2);
		System.out.println("1: " + i1 + " 2: " + i2 + " checksum: " + checksum);

		return checksum;

	}

	public static List<IP4Packet> fragment(IP4Packet ip4packet, int MTU) {

		List<IP4Packet> fragments = new ArrayList<IP4Packet>();

		int k = 0;

		// If MTU is at least as great as the size of ip4packet, 
		// return ip4packet as is since no fragmentation is needed
		if (ip4packet.totalLen < MTU) {
			System.out.println("Size of packet is < MTU, no fragmentation needed: " + ip4packet.totalLen);
			fragments.add(ip4packet);
			return fragments;
		}

		// if DF is set to 1, drop the packet an return [] list
		char[] bits = String.valueOf(ip4packet.flags).toCharArray();
		if (bits[1] == '1') {
			System.out.println("DF Field set!");
			dropPacket(ip4packet.sourceAddr, ip4packet.destAddr, ip4packet.id,
					"Fragmentation needed and DF set \n");
			return fragments;
		}

		IP4Packet newPack = null;
		VRouter vr = new VRouter();

		double numOfFragments = Math.ceil((float) (ip4packet.totalLen - 20) / (MTU - 20));
		int packetNumber = (int) numOfFragments;
		int sizeOfEachPacket = 0;
		int count = 1;
		
		int offset = (MTU - 20) / 8;

		while (sizeOfEachPacket < ip4packet.totalLen) {

			System.out.println("count " + count + "   packetNumber: "
					+ packetNumber);

			// all packet except the last
			if (count < packetNumber) {

				// first packet: offset = 0
				
				newPack = vr.new IP4Packet(ip4packet.version, ip4packet.ihl,
						ip4packet.tos, MTU - 20, ip4packet.id, ip4packet.flags,
						0, ip4packet.ttl - 1, ip4packet.protocol,
						ip4packet.checksum, ip4packet.sourceAddr,
						ip4packet.destAddr);
				
				// offset for 1st packet = 0
				if (count == 1) {
					newPack.fragOffset = 0;
				}
				else {
					newPack.fragOffset = offset*(count-1);
				}

			} else {

				// Handle the last packet case - change the DF flag and packet

				int sizeOfLastPacket = ip4packet.totalLen
						- ((MTU - 20) * (packetNumber - 1));

				newPack = vr.new IP4Packet(ip4packet.version, ip4packet.ihl,
						ip4packet.tos, sizeOfLastPacket, ip4packet.id, "000",
						offset*(count-1), ip4packet.ttl - 1, ip4packet.protocol,
						ip4packet.checksum, ip4packet.sourceAddr,
						ip4packet.destAddr);

			}
			count += 1;
			sizeOfEachPacket += (MTU - 20);
			fragments.add(newPack);
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

		File msgFile = null;
		FileWriter msgout = null;
		BufferedWriter bufMsg = null;

		try {

			msgFile = new File("message.txt");
			msgout = new FileWriter(msgFile);
			bufMsg = new BufferedWriter(msgout);

			bufMsg.write("\nPacket " + ID + " from " + sourceAddr + " to "
					+ destAddr + ": " + message);
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
	 * public static boolean forward(IP4Packet ip4packet, IPaddress interface) {
	 * return false; }
	 */

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
				interfaces.put(content[0] + " " + content[1], mtus);

			}
			System.out.println(interfaces.keySet());

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

	public static boolean lookupInterfaces(String ipAddress) {
		if (interfaces.containsKey(ipAddress)) {
			System.out.println("FOUND IT!");
			return true;
		}
		return false;

	}

	// mask with ipAddress pass and get the ipAddress in fwd table [0]
	// longest prefix is: 192.0.1.3; 255.255.255.0 and them =
	// hashmap: and [0] & [1] , [2] & [3], [1] separately - split by ;

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

				forwardingTable.put(content[0] + " " + content[1], content[2]
						+ " " + content[3]);
			}
			System.out.println(forwardingTable);
			//
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

	public static void convertStringToBinary() {
		String[] sample = interfaces.keySet().toString().replaceAll("\\[", "")
				.replaceAll("\\]", "").split(" ");
		int[] results = new int[sample.length];
		for (int i = 0; i < sample.length; i++) {
			// System.out.println(sample[i].replaceAll("\\,", ""));

			// results[i] = Integer.parseInt(sample[0].trim());
			String numberAsString = sample[0].trim();
			System.out.println(numberAsString);
		}

	}

	public static String lookupDest(String ipAddress) {
		return null;
	}

}
