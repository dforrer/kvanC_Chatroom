package ch.fhnw.kvan.chat.socket.server;


public interface Intercom
{
	public void newMessageFromClient(ConnectionHandler c, String m);
}