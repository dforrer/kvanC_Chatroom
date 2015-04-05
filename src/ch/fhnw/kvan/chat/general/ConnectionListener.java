package ch.fhnw.kvan.chat.general;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


/*
 * Manages all the active socket-connections
 */

public class ConnectionListener implements Intercom{

	private ArrayList<ConnectionHandler> connections;
	private ChatRoom cr;
	
	public ConnectionListener (ChatRoom _cr){
		connections = new ArrayList<ConnectionHandler>(5);
		cr = _cr;
	}
	
	public void addHandler(ConnectionHandler cl) {
		connections.add(cl);
	}

	@Override
	public void newMessageFromClient(ConnectionHandler c, String m) {
		// Process new message
		System.out.println("New Message is: " + m);
	}
	
}
