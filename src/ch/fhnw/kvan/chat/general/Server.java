package ch.fhnw.kvan.chat.general;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ChatRoomDriver crd = new ChatRoomDriver();
		crd.connect("", 0); // To get a ChatRoom-Instance
		ChatRoom cr = (ChatRoom) crd.getChatRoom();
		
		// Start listening on the server
		ConnectionListener cl = new ConnectionListener(cr);
		cl.start();
		ServerSocket server = null;
		try {
			server = new ServerSocket(1234);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while (true) {
			Socket s = null;
			try {
				s = server.accept();
				ConnectionHandler h = new ConnectionHandler(s, cl);
				h.start();				
				cl.addHandler(h);
			} catch (IOException e) {
				System.err.println(e);
			} finally {
				try {
					s.close();
				} catch (IOException e) {
					System.err.println(e);
				}
			}
		}
	}
}
