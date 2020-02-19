package chatModele;

public class TextChatLogger implements IChatLogger 
{
	@Override
	public void clientConnected(String ip) 
	{
		System.out.println("client connected: " + ip);
	}

	@Override
	public void clientDisconnected(String ip, String name) 
	{
		System.out.println("client disconnected: " + ip + " (" + name + ")" );
	}

	@Override
	public void clientGotName(String ip, String name) 
	{
		System.out.println("client " + ip + " got name: " + name);
	}

	@Override
	public void clientGotCommand(String name, int command) 
	{
		System.out.println("client got " + name + " command: " + command);
	}

	@Override
	public void publicChat(String from, String message) 
	{
		System.out.println("public chat from " + from + ": " + message);
	}

	@Override
	public void privateChat(String from, String to, String message) 
	{
		System.out.println("private chat from " + from + " to " + to + ": " + message);
	}

	@Override
	public void systemMessage(String message) 
	{
		System.out.println("system message: " + message);
	}

	// Rooms

	@Override
	public void createRoom(String room) 
	{
		System.out.println("new room: " + room);
	}

	@Override
	public void roomSendChatMessage(String room, String from, String message) 
	{
		System.out.println("room message from " + from + " to room " + room + ": " + message);	
	}

	@Override
	public void deleteRoom(String room) 
	{
		System.out.println("room " + room + " was deleted.");
	}	
}
