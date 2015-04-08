package ch.fhnw.kvan.chat.socket.server;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
		}
	}

	@Override
	public synchronized void newMessageFromClient(ConnectionHandler ch,
			String input) {
		// Process new message: 1. Alter chatRoom 2. Distribute to all clients
		System.out.println("New Client-Message is: " + input);
		String key = input.split("=")[0];

		switch (key) {
		case "name":
			// FORMAT: "name=client1"
			String addedName = input.split("=")[1];
			try {
				// Add client to model
				cr.addParticipant(addedName);

				// Set the clientName
				ch.setClientName(addedName);

				// Send topics and participants to the new client
				ch.getOut().println(cr.getTopics());
				ch.getOut().println(cr.getParticipants());

				// Send messages for topics after initial connection
				// FORMAT: "messages=Meldung1;Meldung2;topic=myTopic"
				if (!cr.getTopics().equals("topics=")) {
					String[] topics = cr.getTopics().split("=")[1].split(";"); // FORMAT:
																				// "topics=Topic1;Topic2;"
					for (int i = 0; i < topics.length; i++) {
						if (!cr.getMessages(topics[i]).equals("messages=")) {
							ch.getOut().println(
									cr.getMessages(topics[i]) + "topic="
											+ topics[i]);
						}
					}
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// Send new Participant to everyone except the new Participant
			// himself
			for (int i = 0; i < connections.size(); i++) {
				if (!ch.equals(connections.get(i))) {
					connections.get(i).getOut()
							.println("add_participant=" + addedName);
				}
			}
			break;
		case "remove_name":
			// FORMAT: "remove_name=client1"
			String removed_name = input.split("=")[1];

			// Only remove "name" if the client has registered with the same
			// name
			if (removed_name.equals(ch.getClientName())) {
				try {
					cr.removeParticipant(removed_name);
					ch.getSocket().close();
					connections.remove(ch);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				distributeMessage("remove_participant=" + removed_name);
			}
			break;
		case "message":
			// FORMAT: "message=Hello World;topic=myTopic"
			String topic = input.split("=")[2];
			String message = input.split("=")[1].split(";")[0];
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			message = dateFormat.format(new Date()) + " " + ch.getClientName() + " : " + message;
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
			boolean topicRemoved = false;
			try {
				topicRemoved = cr.removeTopic(removedTopic); // add topic to chatroom-model
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (topicRemoved) {
				// Distribute topic among all the clients
				distributeMessage("remove_topic=" + removedTopic);
			}
			break;
		default:
			throw new IllegalArgumentException("Invalid key: " + key);
		}
	}
}
