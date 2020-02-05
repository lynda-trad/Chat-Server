package server;

public class TextChatLogger implements IChatLogger
{

	@Override
	public void clientConnected(String ip) 
	{
		System.out.println("Connected : " + ip);	
	}

	@Override
	public void clientDisconnected(String ip, String name) 
	{
		System.out.println("Disconnected : " + ip + " " + name );	
	}

	@Override
	public void clientGotName(String ip, String name) 
	{
		System.out.println(ip + " " + name);	
	}

	@Override
	public void clientGotCommand(String name, int command) 
	{
		System.out.println(name + " " + command);
	}

	@Override
	public void publicChat(String from, String msg) 
	{
		System.out.println("From : " + from);
		System.out.println(msg);
	}

	@Override
	public void privateChat(String from, String to, String msg) 
	{
		System.out.println("From : " + from);
		System.out.println("To : " + to);
		System.out.println(msg);
	}

	@Override
	public void systemMessage(String msg) 
	{
		System.out.println("System message : " + msg);	
	}
	
}
