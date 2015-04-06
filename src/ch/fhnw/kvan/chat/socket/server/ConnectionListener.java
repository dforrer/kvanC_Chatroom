package ch.fhnw.kvan.chat.socket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import ch.fhnw.kvan.chat.general.ChatRoom;

/*
 * Manages all the active socket-connections
 */

public class ConnectionListener implements Intercom {

	private ArrayList<ConnectionHandler> connections;
	private ChatRoom cr;

	public ConnectionListener(ChatRoom _cr) {
		connections = new ArrayList<ConnectionHandler>(5);
		cr = _cr;
	}

	public void addHandler(ConnectionHandler ch) {
		connections.add(ch);
	}

	@Override
	public void newMessageFromClient(ConnectionHandler ch, String input) {
		// Process new message: 1. Alter chatRoom 2. Distribute to all clients
		System.out.println("New Message is: " + input);
		String key = input.split("=")[0];
		
		switch (key) {
		case "name":
			// TODO send client topics, participants, messages for topics
			
			break;
		case "message":
			// FORMAT: "message=Hello World;topic=myTopic"
			String topic = input.split("=")[2];
			String message = input.split("=")[1].split(";")[0];
			try {
				cr.addMessage(topic, message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Distribute message+topic among all the clients
			for (int i = 0; i < connections.size(); i++) {
				connections.get(i).getOut().println("message="+message+";topic="+topic);;
			}
			break;
		case "add_topic":
			// FORMAT: "add_topic=myTopic"
			String topic2 = input.split("=")[1];
			System.out.println("topic2:"+topic2);
			try {
				cr.addTopic(topic2); // add topic to chatroom-model
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Distribute topic among all the clients
			for (int i = 0; i < connections.size(); i++) {
				System.out.println("iterator: "+i);
				connections.get(i).getOut().println("add_topic="+topic2);;
			}
			break;
		case "remove_topic":
			break;
		case "add_participant":
			break;
		case "remove_participant":
			break;
		case "participants":
			break;
		case "messages":
			break;
		default:
			throw new IllegalArgumentException("Invalid key: " + key);
		}
	}

}
