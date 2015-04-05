package ch.fhnw.kvan.chat.general;

public interface Intercom
{
	public void newMessageFromClient(ConnectionHandler c, String m);
}