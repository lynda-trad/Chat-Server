package chatModele;

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
		
	public void sendMessage(String user, String msg) 
	{
		if (state == ClientState.ST_INIT) 
			return;
		ChatModel.sendChatMessage(name, msg);
		logger.publicChat(name, msg);
	}
		
	public void chatMessage(String from, String msg) 
	{
		if (from != name) 
		{
			cho.sendMessage(from, msg);
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
	public void sendPrivateMessage(String from, String to, String msg) 
	{
		if (state == ClientState.ST_INIT) 
			return;
		
		ChatModel.sendPrivateChatMessage(from, to, msg);
		logger.privateChat(from, to, msg);
	}
	
	@Override
	public void sendQuit() 
	{
		shutdownRequested();
	}
	
	// Room
	
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
			cho.sendRoomOK(room);
		}
	}
	
	public void sendRoomMessage(String room, String from , String message)
	{
		if (state == ClientState.ST_INIT )
		{
			cho.sendError( "Not initialized...");
		}
		if (ChatModel.roomHasUser(room, name ))
		{
			ChatModel.roomSendChatMessage(room, name ,message);
		}
		else
		cho.sendError( "Not in room...");
	}

	@Override
	public void roomUserListChanged(String room) 
	{
		cho.sendRoomUserList(room, RoomModel.getRoomUserNames());
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
		cho.sendRoomList(ChatModel.getRoomNames());
	}
	
}