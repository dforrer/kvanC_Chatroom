package ch.fhnw.kvan.chat.socket.client;

import java.io.IOException;
import java.net.Socket;

import ch.fhnw.kvan.chat.general.ChatRoom;
import ch.fhnw.kvan.chat.general.Chats;
import ch.fhnw.kvan.chat.general.Participants;
import ch.fhnw.kvan.chat.gui.ClientGUI;
import ch.fhnw.kvan.chat.interfaces.IChatDriver;
import ch.fhnw.kvan.chat.interfaces.IChatRoom;
import ch.fhnw.kvan.chat.utils.*;

public class Client implements IChatDriver, IChatRoom {

	private static Client client;

	private Socket s;
	protected In in;
	protected Out out;
	private boolean running;
	private ClientGUI gui;

	// Same as ChatRoom-Class
	private final Participants participantInfo = new Participants();
	private final Chats chatInfo = new Chats();

	public Client() {
		running = true;
		// Start GUI
	}

	public static void main(String args[]) {
		System.out.println("Client-Class has been called with arguments: ");
		System.out.println("Name: " + args[0]);
		System.out.println("Host: " + args[1]);
		System.out.println("Port: " + args[2]);

		int port = Integer.parseInt(args[2]);
		String clientName = args[0];

		client = new Client();
		try {
			client.connect(args[1], port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Send name
		client.startListening();
		client.out.println("name=" + clientName);
		client.gui = new ClientGUI(client, clientName);

	}

	public void startListening() {
		new Thread() {
			public void run() {

				String input = in.readLine();

				while (running && input != null) {
					System.out.println("input from server: "+input);
					processServerMessage(input);
					input = in.readLine();
				}
			}
		}.start();
	}

	public void processServerMessage(String input) {
		System.out.println("serverMessage:"+input);
		String key = input.split("=")[0];
		String value = input.split("=")[1];

		switch (key) {
		case "message":
			// FORMAT: "message=Hello World;topic=myTopic"
			String topic = input.split("=")[2];
			String message = input.split("=")[1].split(";")[0];
			chatInfo.addMessage(topic, message); // Add it to the model
			break;
		case "add_topic":
			// FORMAT: "add_topic=myTopic"
			String topic2 = input.split("=")[1];
			chatInfo.addTopic(topic2);
			gui.addTopic(topic2);
			break;
		case "remove_topic":
			break;
		case "topics":
			String[] topics = value.split(";");
			gui.updateTopics(topics);
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

	// --------------------------------
	// IChatDriver-Interface-Functions
	// --------------------------------

	@Override
	public void connect(String host, int port) throws IOException {
		// Setup socket-connection
		s = null;
		try {
			s = new Socket(host, port, null, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		in = new In(s);
		out = new Out(s);
	}

	@Override
	public void disconnect() throws IOException {
		s.close();
	}

	@Override
	public IChatRoom getChatRoom() {
		return this;
	}

	// --------------------------------
	// IChatRoom-Interface-Functions
	// --------------------------------

	@Override
	public boolean addParticipant(String name) throws IOException {
		System.out.println("addParticipant SHOULD NEVER BE CALLED");
		return true;
	}

	@Override
	public boolean removeParticipant(String name) throws IOException {
		if (!name.trim().equalsIgnoreCase("")) {
			System.out.println("Participant removed");
			out.println("remove_name=" + name);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean addTopic(String topic) throws IOException {
		if (!topic.trim().equalsIgnoreCase("")) {
			System.out.println("Topic added: "+topic);
			out.println("add_topic=" + topic);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean removeTopic(String topic) throws IOException {
		if (!topic.trim().equalsIgnoreCase("")) {
			System.out.println("Topic removed");
			out.println("remove_topic=" + topic);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean addMessage(String topic, String message) throws IOException {
		if (!topic.trim().equalsIgnoreCase("")
				&& !message.trim().equalsIgnoreCase("")) {
			System.out.println("Message added");
			out.println("message=" + message + ";topic=" + topic);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getMessages(String topic) throws IOException {
		if (!topic.trim().equalsIgnoreCase("")) {
			return chatInfo.getMessages(topic);
		} else {
			return ("messages=");
		}
	}

	@Override
	public String refresh(String topic) throws IOException {
		if (!topic.trim().equalsIgnoreCase("")) {
			return chatInfo.getMessages(topic);
		} else {
			return ("messages=");
		}
	}

}
