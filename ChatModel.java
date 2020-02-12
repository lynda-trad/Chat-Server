package chatModele;

import java.util.Set;
import java.util.TreeMap;

public class ChatModel 
{
	private static final TreeMap<String, ChatModelEvents> clientList = new TreeMap<>();
	private static final TreeMap <String , RoomModel> roomList = new TreeMap<>();
	
	// Chat
	
	public static synchronized boolean registerUser(String name, HandleClient client)
	{
		if (!existUserName(name) && !name.equals("")) 
		{
			clientList.put(name, client);
			notifyNewName();
			return true;
		}
		return false;
	}
			
	public static synchronized boolean existUserName(String name)
	{
		return clientList.containsKey(name);
	}
	
	public static synchronized boolean renameUser(String oldname, String newname, HandleClient client) 
	{
		if (existUserName(oldname))
		{
			if(!existUserName(newname))
			{
				unregisterUser(oldname);
				clientList.put(newname,client);
				notifyNewName();
				return true;
			}
		}
		return false;
	}
	
	public static synchronized Set<String> getUserNames() 
	{
		return clientList.keySet();
	}
			
	public static synchronized void unregisterUser(String name) 
	{
		if (existUserName(name)) 
		{
			clientList.remove(name);
			notifyNewName();
		}
	}
			
	private static void notifyNewName() 
	{
		clientList.values().forEach(ChatModelEvents::userListChanged);
	}
	
	public static void sendChatMessage(String from, String msg) 
	{
		clientList.values().forEach(c->c.chatMessageSent(from, msg));
	}
	
	public static void sendPrivateChatMessage(String from, String to, String msg)
	{
		clientList.get(to).privateChatMessageSent(from, to, msg);
	}

	public static void clearAll() 
	{
		clientList.clear();
	}

	// Rooms

	public static synchronized Set<String> getRoomNames() 
	{
		return roomList.keySet();
	}
	
	public static synchronized boolean addRoom(String room, String master)
	{
		if (!existRoom(room) && existUserName(master))
		{
			roomList.put(room, new RoomModel(room, master, clientList.get(master)));
			notifyChangeRooms();
			return true;
		}
		return false;
	}
	
	public static synchronized void deleteRoom(String room, String user)
	{
		if (existRoom(room) && roomList.get(room).canDelete(user))
		{
			roomList.remove(room); 
			notifyChangeRooms();
		}
	}
	
	private static synchronized void notifyChangeRooms()
	{
		clientList.values().forEach(ChatModelEvents::roomListChanged);
	}
	
	public static synchronized void enterRoom(String room, String user)
	{
		if (!existUserName(user) || !existRoom (room))
		{
			return;
		}
		roomList.get(room).registerUser(user, clientList.get (user));
	}
	
	public static synchronized void leaveRoom (String room, String user)
	{
		if (!existUserName (user) || !existRoom (room))
		{
			return;
		}
		
		roomList.get(room).unregisterUser(user);
		if (roomList.get(room).userCount() == 0)
			roomList.remove(room);
	}
	
	public static boolean existRoom(String room) 
	{
		return roomList.containsKey(room);
	}

	public static boolean roomHasUser(String room, String name) 
	{
		return roomList.get(room).hasUser(name);
	}

	public static void roomSendChatMessage(String room, String name, String message) 
	{
		roomList.get(room).chatMessage(name, message);
	}

}