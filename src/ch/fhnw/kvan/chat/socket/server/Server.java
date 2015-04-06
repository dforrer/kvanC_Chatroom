package ch.fhnw.kvan.chat.socket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import ch.fhnw.kvan.chat.general.ChatRoom;
import ch.fhnw.kvan.chat.general.ChatRoomDriver;

public class Server {

	public static void main(String[] args) {
		
		ChatRoomDriver crd = new ChatRoomDriver();
		crd.connect("", 0); // To get a ChatRoom-Instance
		ChatRoom cr = (ChatRoom) crd.getChatRoom();
		
		// Start listening on the server
		ConnectionListener cl = new ConnectionListener(cr);
		ServerSocket server = null;
		try {
			server = new ServerSocket(1234);
			System.out.println("Server started and listening on port: 1234");
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
			}
		}
	}
}
