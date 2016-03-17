package VRouter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VRouter {

	private class IP4Packet {
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

		public IP4Packet(int version, int ihl, int tos, int totalLen, int id, String flags, int fragOffset, int ttl,
				int protocol, String checksum, String sourceAddr, String destAddr) {
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

		public void setVersion(int version) {
			this.version = version;
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
		List<IP4Packet> ip4Packets = incomingPackets("InPackets.txt");
		VRouter vr = new VRouter();
		IP4Packet ip = vr.new IP4Packet(4, 5, 0, 60, 7238, "100", 0, 64, 6,"0", "4.40.48", "25.72");
		checksum(ip4Packets.get(0));

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
		ip = vr.new IP4Packet(Integer.parseInt(chars[0]), Integer.parseInt(chars[1]), Integer.parseInt(chars[2]),
				Integer.parseInt(chars[3]), Integer.parseInt(chars[4]), chars[5], Integer.parseInt(chars[6]),
				Integer.parseInt(chars[7]), Integer.parseInt(chars[8]), chars[9], chars[10], chars[11]);

		System.out.println(ip.destAddr);

		return ip;

	}

	public static String checksum(IP4Packet ip4packet) {

		// Set checksum field to 0
		ip4packet.checksum = "0";
		ArrayList<String> hexList = convertHex(ip4packet);
		System.out.println(hexList);

		ArrayList<String> binList = new ArrayList<String>();
		for (int i = 0; i < hexList.size(); i++) {
			int b = Integer.parseInt(hexList.get(i), 16);
			String bin = convertHexHelper(Integer.toBinaryString(b), 16);
			binList.add(bin);
		}

		System.out.println(binList);
		String checksumValue = getSum(binList);
		System.out.println(checksumValue);
		//Need to find one's complement
		
		return checksumValue;
		//4500 0514 42A2 2140 8001 50B2(Header Checksum) C0A8 0003 C0A8 0001

	}

	public static String getSum(ArrayList<String> binList) {
		BigInteger sum = new BigInteger("0");
		for (int i = 0; i < binList.size(); i++) {
			sum = sum.add(new BigInteger(binList.get(i), 2));

			// Splits the sum by removing the carry once the sum goes past 16
			// bits
			if (sum.toString(2).length() > 16) {
				String s1 = sum.toString(2).substring(sum.toString(2).length() - 16, sum.toString(2).length());
				String s2 = sum.toString(2).substring(0, sum.toString(2).length() - 16);

				BigInteger num1 = new BigInteger(s1, 2);
				BigInteger num2 = new BigInteger(s2, 2);
				sum = num1.add(num2);
			}

		}
		String value = sum.toString(2);
		return value;

		// System.out.println(sum.toString(16));
		// 110101000010000
		
	}

	public static ArrayList<String> convertHex(IP4Packet ip4packet) {

		ArrayList<String> hexList = new ArrayList<String>();
		String hex1 = null;
		hex1 = convertHexHelper(Integer.toHexString(ip4packet.version), 1);
		hex1 += convertHexHelper(Integer.toHexString(ip4packet.ihl), 1);
		hex1 += convertHexHelper(Integer.toHexString(ip4packet.tos), 2);

		String hex2 = convertHexHelper(Integer.toHexString(ip4packet.totalLen), 4);

		String hex3 = convertHexHelper(Integer.toHexString(ip4packet.id), 4);

		String hex4 = convertHexHelper(Integer.toHexString((Integer.parseInt(ip4packet.flags, 2))), 1);
		hex4 += convertHexHelper(Integer.toHexString(ip4packet.fragOffset), 3);

		String hex5 = convertHexHelper(Integer.toHexString(ip4packet.ttl), 2);
		hex5 += convertHexHelper(Integer.toHexString(ip4packet.protocol), 2);

		String hex6 = convertHexHelper(ip4packet.checksum, 4);

		String s = ip4packet.sourceAddr.replaceAll("\\.", "");
		System.out.println(s);
		int i = Integer.parseInt(s);
		
		String s1 = convertHexHelper(Integer.toHexString(i), 8);
		String hex7 = s1.substring(0, 4);
		String hex8 = s1.substring(4, 8);

		String s2 = ip4packet.destAddr.replaceAll("\\.", "");
		int i1 = Integer.parseInt(s2);
		String s3 = convertHexHelper(Integer.toHexString(i1), 8);
		String hex9 = s3.substring(0, 4);
		String hex10 = s3.substring(4, 8);

		// System.out.println(hex1 + "\n" + hex2 + "\n" + hex3 + "\n" + hex4 +
		// "\n" + hex5 + "\n" + hex6 + "\n" + hex7 + "\n" + hex8);

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

	public static String convertHexHelper(String s, int l) {
		while (s.length() < l) {
			s = "0" + s;
		}
		return s;
	}

	private static String addInBinary(String string, String string2) {

		int i1 = Integer.parseInt(string, 2);
		int i2 = Integer.parseInt(string2, 2);

		String checksum = Integer.toBinaryString(i1 + i2);
		// System.out.println("1: " + i1 + " 2: " + i2 + " checksum: " +
		// checksum);

		System.out.println(checksum);
		return checksum;

		/*
		 * 4500 -> 0100010100000000 003c -> 0000000000111100 453C ->
		 * 0100010100111100 /// First result
		 * 
		 * 453C -> 0100010100111100 // First result plus next 16-bit word. 1c46
		 * -> 0001110001000110 6182 -> 0110000110000010 // Second result.
		 * 
		 * 6182 -> 0110000110000010 // Second result plus next 16-bit word. 4000
		 * -> 0100000000000000 A182 -> 1010000110000010 // Third result.
		 * 
		 * A182 -> 1010000110000010 // Third result plus next 16-bit word. 4006
		 * -> 0100000000000110 E188 -> 1110000110001000 // Fourth result. E188
		 * -> 1110000110001000 // Fourth result plus next 16-bit word. AC10 ->
		 * 1010110000010000 18D98 -> 11000110110011000 // One odd bit (carry),
		 * add that odd bit to the result as we need to keep the checksum in 16
		 * bits.
		 * 
		 * 18D98 -> 11000110110011000 8D99 -> 1000110110011001 // Fifth result
		 * 
		 * 8D99 -> 1000110110011001 // Fifth result plus next 16-bit word. 0A63
		 * -> 0000101001100011 97FC -> 1001011111111100 // Sixth result
		 * 
		 * 97FC -> 1001011111111100 // Sixth result plus next 16-bit word. AC10
		 * -> 1010110000010000 1440C -> 10100010000001100 // Again a carry, so
		 * we add it (as done before)
		 * 
		 * 1440C -> 10100010000001100 440D -> 0100010000001101 // This is
		 * seventh result
		 * 
		 * 440D -> 0100010000001101 //Seventh result plus next 16-bit word 0A0C
		 * -> 0000101000001100 4E19 -> 0100111000011001 // Final result.
		 */

	}

	public static List<IP4Packet> fragment(IP4Packet ip4packet, int MTU) {

		return null;
	}

	/*
	 * public static boolean dropPacket(IPaddress sourceAddress, IPaddress
	 * destAddress, int ID, String message) { return false; }
	 * 
	 * public static boolean forward(IP4Packet ip4packet, IPaddress interface) {
	 * return false; }
	 * 
	 * public static boolean lookupInterfaces(IPaddress ipAddress) { return
	 * false; }
	 * 
	 * public static IPaddress lookupDest(IPaddress ipAddress) { return null; }
	 */
}
