package server;

import java.util.Set;
import java.util.TreeMap;

public class ChatModel 
{
	private static final TreeMap <String, ChatModelEvents > clientList = new TreeMap<>();
	
	public static synchronized boolean registerUser (String name, HandleClient client)
	{
		if (!existUserName (name) && name.equals (""))
		{
			clientList.put(name,client);
			notifyNewName();
			return true;
		}
		return false;
	}

	public static synchronized void unregisterUser(String name)
	{
		if (existUserName (name))
		{
			clientList.remove(name);
			notifyNewName();
		}
	}
	
	public static synchronized boolean renameUser(String oldname , String newname , HandleClient client)
	{
		if (existUserName (oldname))
		{
			if(existUserName(newname))
			{
				unregisterUser(oldname);
				clientList.put(newname,client);			
				notifyNewName();
				return true;
			}
		}
		return false;
	}

	public static synchronized boolean existUserName (String name)
	{
		return clientList.containsKey(name);
	}

	public static synchronized Set<String>getUserNames()
	{
		return clientList.keySet();
	}
	
	public static void sendChatMessage (String from, String msg)
	{
		clientList.values().forEach (c->c.chatMessageSent(from,msg));
	}
	
	public static void sendPrivateChatMessage (String from, String to, String msg )
	{
		clientList.get(to).privateChatMessageSent(from, to, msg);
	}
	
	private static void notifyNewName()
	{
		clientList.values().forEach (c->c.userListChanged());
	}
	
	/*//prof ne lutilise plus
	public static void clearAll()
	{
		
	}
	*/
}