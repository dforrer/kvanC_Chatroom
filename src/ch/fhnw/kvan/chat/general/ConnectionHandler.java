package ch.fhnw.kvan.chat.general;

import java.net.Socket;
import ch.fhnw.kvan.chat.utils.*;

/**
 *  
 * @author Daniel
 *
 */

public class ConnectionHandler extends Thread{

	Socket socket;
	ConnectionListener listener;
	In in;
	Out out;

	// Constructor
	public ConnectionHandler(Socket _socket, ConnectionListener _listener) {
		socket = _socket;
		listener = _listener;
		in = new In(socket);
		out = new Out(socket);
	}
	
	@Override
	public void run() {
		// Listen on 
	}
}
