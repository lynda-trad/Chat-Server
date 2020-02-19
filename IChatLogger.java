package chatModele;

public interface IChatLogger 
{	
	public void clientConnected(String ip);
	public void clientDisconnected(String ip, String name);
	public void clientGotName(String ip, String name);
	public void clientGotCommand(String name, int command);
	public void publicChat(String from, String message);
	public void privateChat(String from, String to, String message);
	public void systemMessage(String message);
	
	// Rooms
	public void createRoom(String room);
	public void roomSendChatMessage(String room, String from, String message);
	public void deleteRoom(String room);
}