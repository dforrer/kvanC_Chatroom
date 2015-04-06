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
import ch.fhnw.kvan.chat.utils.*;

public class Client {
	
	public static void main(String args[]) {
		System.out.println("Client-Class has been called with arguments: ");
		System.out.println("Name: " + args[0]);
		System.out.println("Host: " + args[1]);
		System.out.println("Port: " + args[2]);

		int port = Integer.parseInt(args[2]);
		String clientName = args[0];

		// Setup socket-connection
		Socket s = null;
		try {
			s = new Socket(args[1], port, null, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}

		In in = new In(s);
		Out out = new Out(s);
		
		// Setup the ChatRoom
		ChatRoomDriver crd = new ChatRoomDriver();
		crd.connect("", 0); // To get a ChatRoom-Instance
		ChatRoom cr = (ChatRoom) crd.getChatRoom();
				
		// Start GUI
		ClientGUI gui = new ClientGUI(cr, clientName);
		
		// Send name
		out.println("name="+clientName);
		/*
		Scanner scan = new Scanner(System.in);
		String input = scan.nextLine();
		while (input != null && !input.equals("")) {
			System.out.println("Client: " + input);
			out.println(input);
			input = scan.nextLine();
		}
		scan.close();
	*/
	}		
}
