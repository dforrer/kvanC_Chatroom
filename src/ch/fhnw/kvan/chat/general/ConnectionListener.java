package ch.fhnw.kvan.chat.general;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/*
 * Manages all the active socket-connections
 */

public class ConnectionListener extends Thread{

	ArrayList<ConnectionHandler> connections;
	ChatRoom cr;
	
	public ConnectionListener (ChatRoom _cr){
		connections = new ArrayList<ConnectionHandler>(5);
		cr = _cr;
	}
	
	@Override
	public void run() {
		while (true) {
			
		}
	}

	public void addHandler(ConnectionHandler cl) {
		connections.add(cl);
	}
	
}
