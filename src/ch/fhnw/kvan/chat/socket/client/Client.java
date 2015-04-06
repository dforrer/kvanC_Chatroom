package ch.fhnw.kvan.chat.socket.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import ch.fhnw.kvan.chat.general.ChatRoom;
import ch.fhnw.kvan.chat.general.ChatRoomDriver;
import ch.fhnw.kvan.chat.gui.ClientGUI;
import ch.fhnw.kvan.chat.interfaces.IChatDriver;
import ch.fhnw.kvan.chat.interfaces.IChatRoom;
import ch.fhnw.kvan.chat.utils.*;

public class Client implements IChatDriver {

	private static Client client;
	
	private Socket s;
	protected In in;
	protected Out out;
	private ChatRoom chatRoom = null;

	public Client() {

	}

	public static void main(String args[]) {
		System.out.println("Client-Class has been called with arguments: ");
		System.out.println("Name: " + args[0]);
		System.out.println("Host: " + args[1]);
		System.out.println("Port: " + args[2]);

		int port = Integer.parseInt(args[2]);
		String clientName = args[0];

		client = new Client();
		try {
			client.connect(args[1], port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Send name
		client.out.println("name=" + clientName);

		// Start GUI
		ClientGUI gui = new ClientGUI(client.getChatRoom(), clientName);
	}

	@Override
	public void connect(String host, int port) throws IOException {
		// Setup socket-connection
		s = null;
		try {
			s = new Socket(host, port, null, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		in = new In(s);
		out = new Out(s);
		chatRoom = ChatRoom.getInstance();
	}

	@Override
	public void disconnect() throws IOException {
		s.close();
		chatRoom = null;
	}

	@Override
	public IChatRoom getChatRoom() {
		return chatRoom;
	}
}
