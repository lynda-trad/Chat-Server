package chatModele;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;

public class ChatOutput implements ChatProtocol
{	
	PrintWriter os;
	
	public ChatOutput(OutputStream out) throws IOException 
	{
		this.os = new PrintWriter(out, true);
	}

	public synchronized void sendName(String name)
	{
		os.println("NAME");
		os.println(name);
	}
	
	public synchronized void sendNameOK() 
	{
		os.println("NAME OK");
	}
	
	public synchronized void sendNameBad() 
	{
		os.println("NAME BAD");
	}
	
	public synchronized void sendMessage(String user, String msg) 
	{
		os.println("MESSAGE");
		os.println(user);
		os.println(msg);
	}
	
	public synchronized void sendAskUserList() 
	{
		os.println("AULIST");
	}
	
	public synchronized void sendUserList(Collection<String> ulist)
	{
		os.println("ULIST");
		ulist.forEach(os::println);
		os.println(".");
	}
	
	public synchronized void sendPrivateMessage(String from, String to, String msg) 
	{
		os.println("PRIVATE MESSAGE");
		os.println(from);
		os.println(to);
		os.println(msg);
	}
	
	public synchronized void sendQuit() 
	{
		os.println("QUIT");
	}
}
