package VRouter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VRouter {

	private class IP4Packet {
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
				Integer.parseInt(chars[5]), Integer.parseInt(chars[6]),
				Integer.parseInt(chars[7]), Integer.parseInt(chars[8]),
				chars[9], chars[10], chars[11]);

		System.out.println(ip.destAddr);

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
		 
		 
		//dropPacket(ip.sourceAddr, ip.destAddr, ip.id,
		//		 "Testing out the drop packet");
			 
		//lookupInterfaces(ip.destAddr);
		 

		return ip;

	}

	public static String checksum(IP4Packet ip4packet) {
		return null;
	}

	public static List<IP4Packet> fragment(IP4Packet ip4packet, int MTU) {

		return null;
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

			// while (message != null ) {
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
	 * @ipAddress takes in a IP4Packet address and looks for it in Interfaces.txt 
	 * @return: it address is found, it returns true, else false
	 */
	public static boolean lookupInterfaces(String ipAddress) {

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
				content = line.split(";");
				
				if (content[0].contains(ipAddress)){
					System.out.println("\nLOOKING FOR: " + ipAddress);
					System.out.println("FOUND IT " + content[0] +"\n");
				}
			}
			
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
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

	/*
	 * public static IPaddress lookupDest(IPaddress ipAddress) { return null; }
	 */
}
