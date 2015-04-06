package ch.fhnw.kvan.chat.socket.server;

import java.io.IOException;
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

	public void distributeMessage(String message) {
		for (int i = 0; i < connections.size(); i++) {
			connections.get(i).getOut().println(message);
			;
		}
	}

	@Override
	public void newMessageFromClient(ConnectionHandler ch, String input) {
		// Process new message: 1. Alter chatRoom 2. Distribute to all clients
		System.out.println("New Message is: " + input);
		String key = input.split("=")[0];

		switch (key) {
		case "name":
			// FORMAT: "name=client1"
			String addedName = input.split("=")[1];
			// TODO send messages for topics after initial connection
			try {
				// Add client to model
				cr.addParticipant(addedName);
				
				// Send topics and participants to the new client
				ch.getOut().println(cr.getTopics());
				ch.getOut().println(cr.getParticipants());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			distributeMessage("add_participant="+addedName);
			break;
		case "remove_name":
			// FORMAT: "remove_name=client1"
			String removed_name = input.split("=")[1];
			try {
				cr.removeParticipant(removed_name);
				ch.getSocket().close();
				connections.remove(ch);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			distributeMessage("remove_participant="+removed_name);

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
			distributeMessage("message=" + message + ";topic=" + topic);
			break;
		case "add_topic":
			// FORMAT: "add_topic=myTopic"
			String addedTopic = input.split("=")[1];
			System.out.println("topic2:" + addedTopic);
			try {
				cr.addTopic(addedTopic); // add topic to chatroom-model
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Distribute topic among all the clients
			distributeMessage("add_topic=" + addedTopic);
			break;
		case "remove_topic":
			// FORMAT: "remove_topic=myTopic"
			String removedTopic = input.split("=")[1];
			System.out.println("removedTopic:" + removedTopic);
			try {
				cr.removeTopic(removedTopic); // add topic to chatroom-model
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Distribute topic among all the clients
			distributeMessage("remove_topic=" + removedTopic);
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
