package ch.fhnw.kvan.chat.general;

import java.net.Socket;

import ch.fhnw.kvan.chat.general.Intercom;
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
		System.out.println("ConnectionHandler-run called");
		// Listen on <in>
		while (true) {
			if (in.hasNextLine()) {
				String input = in.readLine();
				System.out.println("readLine: " + input);
				// When new message arrives make callback via event
				listener.newMessageFromClient(this, input);
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//System.out.println("end of run");
	}

	// Getter
	public Out getOut() {
		return out;
	}
}
