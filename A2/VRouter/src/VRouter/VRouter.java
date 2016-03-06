package VRouter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VRouter {

	public class IP4Packet {
		int version;
		int ihl;
		int tos;
		int totalLen;
		int id;
		int flags;
		int fragOffset;
		int ttl;
		int protocol;
		String checksum;
		String sourceAddr;
		String destAddr;

		public IP4Packet(int version, int ihl, int tos, int totalLen, int id,
				int flags, int fragOffset, int ttl, int protocol,
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

		public void setFlags(int flags) {
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

	}

	public static List<IP4Packet> incomingPackets(String fileName) {

		// read the file and store each pack in a list
		FileReader fr = null;
		BufferedReader br = null;

		//String file = "./" + fileName;
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
			
			
			for (int i = 0; i < packetItems.size(); i++) {
				//System.out.println(packetItems.get(i) + "\n");
				createPacket(packetItems.get(i));
			}
			
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static IP4Packet createPacket(String string) {
		
		IP4Packet ip;
		
		String[] chars = null;
		chars = string.replaceAll(";", "").split(" ");
		
		
		ip = new VRouter().new IP4Packet(Integer.parseInt(chars[0]), Integer.parseInt(chars[1]), 
				Integer.parseInt(chars[2]), Integer.parseInt(chars[3]), Integer.parseInt(chars[4]),
				Integer.parseInt(chars[5]), Integer.parseInt(chars[6]), Integer.parseInt(chars[7]), 
				Integer.parseInt(chars[8]), chars[9], chars[10], chars[11]);
		
		System.out.println(ip.destAddr);
		
		return ip;
		
	}

	public static String checksum(IP4Packet ip4packet) {
		return null;
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
