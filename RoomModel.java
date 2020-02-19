package chatModele;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class RoomModel 
{
	String master;
	String roomName;
	
	private static final Map <String, RoomEvents > roomObserverList = new TreeMap<>();
	
	public RoomModel (String roomName , String master, RoomEvents handler) 
	{
		this.master = master;
		this.roomName = roomName;
		registerUser(master, handler);
	}
	
	public static synchronized Set<String> getRoomUserNames() 
	{
		return roomObserverList.keySet();
	}
	
	private void notifyUserListChanged()
	{
		roomObserverList.values().forEach(c-> c.roomUserListChanged(roomName));
	}
	
	public synchronized void registerUser (String user, RoomEvents handler) 
	{
		roomObserverList.put(user, handler);
		notifyUserListChanged();
	}
	
	public synchronized void unregisterUser (String user) 
	{
		if (user.equals (master))
			master = null;
		roomObserverList.remove(user);
		notifyUserListChanged();
	}
	
	public synchronized void chatMessage(String from , String message) 
	{
		roomObserverList.values().forEach(c->c.roomChatMessageSent(roomName, from, message));
	}
	
	public synchronized boolean canDelete (String name) //////// not sure
	{
		return roomObserverList.containsKey(name);
	}
	
	public synchronized Collection<String> userList()
	{
		return RoomModel.getRoomUserNames();
	}
	
	synchronized int userCount() 
	{
		return roomObserverList.size();
	}
	
	synchronized boolean hasUser(String user) 
	{
		return roomObserverList.containsKey(user);
	}
	
	synchronized boolean userRenamed(String name , String newName, RoomEvents handler) 
	{
		if (hasUser(name))
		{
			if(!hasUser(newName))
			{
				unregisterUser(name);
				registerUser(newName,handler);
				notifyUserListChanged();
				return true;
			}
		}
		return false;
	}	
}