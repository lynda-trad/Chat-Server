package chatModele;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class HandleClient implements Runnable, ChatProtocol, ChatModelEvents 
{	
	private final Socket s;
	private ChatOutput cho;
	private ChatInput chi;
	private String name = "";
	private IChatLogger logger = null;
	
	private enum ClientState 
	{
		ST_INIT, ST_NORMAL
	};
	
	private ClientState state = ClientState.ST_INIT;
	private boolean stop = false;
	
	public HandleClient(Socket s, IChatLogger logger) throws IOException 
	{
		this.s = s;
		this.logger = logger;
	}
	
	public void run() 
	{
		try (Socket s1 = s) 
		{
			cho = new ChatOutput(s1.getOutputStream());
			chi = new ChatInput(s1.getInputStream(), this);
			chi.doRun();
		} 
		catch (IOException ex) 
		{
			if (!stop) 
			{
				finish();
			}
		}
	}

	public void sendName(String name) 
	{
		String newName = name;
		if (ChatModel.existUserName(newName)) 
		{
			cho.sendNameBad();
		} 
		else 
		{
			if (state == ClientState.ST_INIT) 
			{
				ChatModel.registerUser(newName, this);
				state = ClientState.ST_NORMAL;
			}
			else 
			{
				ChatModel.renameUser(this.name, newName, this);
			}
			
			this.name = newName;
			cho.sendNameOK();
			logger.clientGotName(s.toString(), name);
		}
	}

	public void askUList() 
	{
		if (state == ClientState.ST_INIT) return;
		cho.sendUserList(ChatModel.getUserNames());
	}
		
	public void sendMessage(String user, String message) 
	{
		if (state == ClientState.ST_INIT) 
			return;
		ChatModel.sendChatMessage(name, message);
		logger.publicChat(name, message);
	}
		
	public void chatMessage(String from, String message) 
	{
		if (from != name) 
		{
			cho.sendMessage(from, message);
		}
	}

	public synchronized void finish()
	{
		if (!stop) 
		{
			stop = true;
			try 
			{
				s.close();
			} 
			catch (IOException ex) 
			{ 
				ex.printStackTrace(); 
			}
			if (name != null)
				ChatModel.unregisterUser(name);
			
			logger.clientDisconnected(s.toString(), name);
		}
	}

	@Override
	public void userListChanged() 
	{
		cho.sendUserList(ChatModel.getUserNames());
	}

	@Override
	public void chatMessageSent(String from, String message) 
	{
		if (from != name) 
		{
			cho.sendMessage(from, message);
		}
	}
	
	@Override
	public void privateChatMessageSent(String from, String to, String message) 
	{
		if (from != name) 
		{
			cho.sendPrivateMessage(from, to, message);
		}
	}
	
	@Override
	public void shutdownRequested() 
	{
		cho.sendQuit();
	}
	
	@Override
	public void sendAskUserList() 
	{
		cho.sendUserList(ChatModel.getUserNames());
	}
	
	@Override
	public void sendPrivateMessage(String from, String to, String message) 
	{
		if (state == ClientState.ST_INIT) 
			return;
		
		ChatModel.sendPrivateChatMessage(from, to, message);
		logger.privateChat(from, to, message);
	}
	
	@Override
	public void sendQuit() 
	{
		shutdownRequested();
	}
	
	// Rooms
	
	public void sendCreateRoom(String room)
	{
		if (state == ClientState.ST_INIT )
		{
			return;
		}
		if(ChatModel.existRoom(room))
			cho.sendRoomBad(room);
		else
		{
			ChatModel.addRoom(room, name);
			logger.createRoom(room);
			cho.sendRoomOK(room);
		}
	}
	
	public void sendRoomMessage(String room, String from , String message)
	{
		if (state == ClientState.ST_INIT )
		{
			cho.sendError( "Not initialized...");
		}
		if (ChatModel.roomHasUser(room, from))
		{
			ChatModel.roomSendChatMessage(room, from, message);
			logger.roomSendChatMessage(room, from, message);
		}
		else
			cho.sendError( "Not in room...");
	}

	@Override
	public void roomChatMessageSent(String room, String from, String message) 
	{
		if (from != name) 
		{
			cho.sendRoomMessage(room, from, message);
		}
	}

	@Override
	public void roomListChanged() 
	{
		cho.sendRoomList(ChatModel.getRooms());
	}
	
	public void deleteRoom(String room)
	{
		cho.deleteRoom(room);
		logger.deleteRoom(room);
	}
	
	public void sendAskRoomList(String room)
	{
		cho.sendRoomList(ChatModel.getRooms());
	}

	public void sendAskRoomUserList(String room)
	{
		cho.sendRoomUserList(room, RoomModel.getRoomUserNames());
	}

	@Override
	public void roomUserListChanged(String room) 
	{
		cho.sendRoomUserList(room, RoomModel.getRoomUserNames());
	}
	
	// Files
	
	public void proposeFile(String user, String filename)
	{
		// TO-DO ?
		cho.proposeFile(user, filename);
	}
	
	public void acceptFile(String user, String filename)
	{
		// TO-DO ?
		cho.acceptFile(user, filename);
	}
	
	public void refuseFile(String user, String filename)
	{
		// TO-DO ?
		cho.refuseFile(user, filename);
	}
	
	public void sendFile(String user, String filename, File f)
	{
		ChatModel.sendFile(name, user, filename, f);
		f.delete();
		cho.sendFile(user, filename, f);
	}

	public void fileSent(String from, String filename, File f)
	{
		cho.sendFile(from, filename, f);
	}

}