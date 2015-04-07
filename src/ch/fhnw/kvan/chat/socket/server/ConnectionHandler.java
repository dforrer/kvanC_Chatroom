package ch.fhnw.kvan.chat.socket.server;

import java.net.Socket;

import ch.fhnw.kvan.chat.utils.*;

/**
 * 
 * @author Daniel
 * 
 */

public class ConnectionHandler extends Thread {
	private Socket socket;
	private ConnectionListener listener;
	private In in;
	private Out out;

	// Constructor
	
	public ConnectionHandler(Socket _socket, ConnectionListener _listener) {
		System.out.println("new ConnectionHandler created");
		socket = _socket;
		listener = _listener;
		in = new In(socket);
		out = new Out(socket);
	}

	@Override
	public void run() {
		// Listen on <in>
		while (in.hasNextLine()) {
			String input = in.readLine();
			// When new message arrives make callback via event
			listener.newMessageFromClient(this, input);
		}
		// System.out.println("end of run");
	}

	// Getter
	public Out getOut() {
		return out;
	}
	
	public Socket getSocket() {
		return socket;
	}
}
