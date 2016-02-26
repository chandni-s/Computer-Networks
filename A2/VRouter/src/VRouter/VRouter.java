package VRouter;

import java.util.List;

public class VRouter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static List<IP4Packet> incomingPackets(String fileName) {
		return null ;
	}
	
	public static String checksum(IP4Packet ip4packet) {
		return null;
	}
	
	public static List<IP4Packet> fragment(IP4Packet ip4packet, int MTU) {
		
		return null;
	}
	
	public static boolean dropPacket(IPaddress sourceAddress, IPaddress destAddress, int ID, String message) {
		return false;
	}

	public static boolean forward(IP4Packet ip4packet, IPaddress interface) {
		return false;
	}
	
	public static boolean lookupInterfaces(IPaddress ipAddress) {
		return false;
	}
	
	public static IPaddress lookupDest(IPaddress ipAddress) {
		return null;
	}
}
