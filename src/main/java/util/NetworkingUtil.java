package main.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

public class NetworkingUtil {
	public static void printAllIPAdressesOnMachine() throws UnknownHostException, SocketException {
		System.out.println("Host addr: " + InetAddress.getLocalHost().getHostAddress()); // often returns "127.0.0.1"
		Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
		while (n.hasMoreElements()) {
			NetworkInterface e = n.nextElement();
			System.out.println("Interface: " + e.getName());
			Enumeration<InetAddress> a = e.getInetAddresses();
			for (; a.hasMoreElements();) {
				InetAddress addr = a.nextElement();
				System.out.println("  " + addr.getHostAddress());
			}
		}
	}

	public static void printAllInterfaces() throws SocketException {
		Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();

		for (NetworkInterface netIf : Collections.list(nets)) {
			System.out.printf("Display name: %s\n", netIf.getDisplayName());
			System.out.printf("Name: %s\n", netIf.getName());
			displaySubInterfaces(netIf);
			System.out.printf("\n");
		}
	}

	public static void displaySubInterfaces(NetworkInterface netIf) throws SocketException {
		Enumeration<NetworkInterface> subIfs = netIf.getSubInterfaces();

		for (NetworkInterface subIf : Collections.list(subIfs)) {
			System.out.printf("\tSub Interface Display name: %s\n", subIf.getDisplayName());
			System.out.printf("\tSub Interface Name: %s\n", subIf.getName());
		}
	}

	public static void main(String[] args) {
		try {
			Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
			while (networks.hasMoreElements()) {
				NetworkingUtil.displayInterfaceInformation(networks.nextElement());
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
		System.out.printf("Display name: %s\n", netint.getDisplayName());
		System.out.printf("Name: %s\n", netint.getName());
		Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();

		for (InetAddress inetAddress : Collections.list(inetAddresses)) {
			System.out.printf("InetAddress: %s\n", inetAddress);
		}

		System.out.printf("Up? %s\n", netint.isUp());
		System.out.printf("Loopback? %s\n", netint.isLoopback());
		System.out.printf("PointToPoint? %s\n", netint.isPointToPoint());
		System.out.printf("Supports multicast? %s\n", netint.supportsMulticast());
		System.out.printf("Virtual? %s\n", netint.isVirtual());
		System.out.printf("Hardware address: %s\n", Arrays.toString(netint.getHardwareAddress()));
		System.out.printf("MTU: %s\n", netint.getMTU());
		System.out.printf("\n");
	}
}
