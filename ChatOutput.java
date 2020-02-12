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

	//Chat 

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

	// Room
	
	public synchronized void sendRoomOK(String room) 
	{
		os.println("ROOM OK");
		os.println(room);
	}
	
	public synchronized void sendRoomBad(String room) 
	{
		os.println("ROOM BAD");
		os.println(room);
	}

	public synchronized void deleteRoom(String string)
	{
		os.println("DELETE ROOM");
		os.println(string);
	}
	
	public synchronized void sendRoomList(Collection<String> rlist) //roomList de chatmodel
	{
		os.println("RLIST");
		rlist.forEach(os::println);
		os.println(".");
	}
	
	public synchronized void sendRoomMessage(String room, String user, String msg) 
	{
		os.println("MESSAGE");
		os.println(room);
		os.println(user);
		os.println(msg);
	}
	
	public synchronized void enterRoom(String room)
	{
		os.println("ENTER ROOM");
		os.println(room);
	}
	
	public synchronized void leaveRoom(String room)
	{
		os.println("LEAVE ROOM");
		os.println(room);
	}
	
	public synchronized void sendAskRoomUserList(String room)
	{
		os.println("ARULIST");
		os.println(room);
	}
	
	public synchronized void sendRoomUserList(String room, Collection<String> rulist)
	{
		os.println("RULIST");
		os.println(room);
		rulist.forEach(os::println); // ru list = roomObserverList de RoomModel
		os.println(".");
	}
	
	public synchronized void sendError(String string) 
	{
		os.println("ERR");
		os.println(string);
	}
}
