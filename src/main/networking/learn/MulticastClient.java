package main.networking.learn;

import java.io.*;
import java.net.*;

public class MulticastClient {

	public static void main(String[] args) throws IOException {
		MulticastSocket socket = new MulticastSocket(7);
		InetAddress address = InetAddress.getByName("224.0.0.2");
		 NetworkInterface netIf = NetworkInterface.getByName("bge0");
		InetSocketAddress group = new InetSocketAddress(address, 7);
		socket.joinGroup(group, netIf);

		DatagramPacket packet;

		// get a few quotes
		for (int i = 0; i < 5; i++) {

			byte[] buf = new byte[256];
			packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);

			String received = new String(packet.getData(), 0, packet.getLength());
			System.out.println("Quote of the Moment: " + received);
		}

		socket.leaveGroup(group, netIf);
		socket.close();
	}

}