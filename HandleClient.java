package server;

import java.io.IOException;
import java.net.Socket;
import java.util.Collection;

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
	
	public boolean getStop()
	{
		return stop;
	}
	
	public HandleClient (Socket s, IChatLogger logger) throws IOException
	{
		this.s = s;
		this.logger = logger;
	}
	
	public void run() 
	{
		try (Socket s1 = s) 
		{
			cho = new ChatOutput (s1.getOutputStream());
			chi = new ChatInput (s1.getInputStream(), this);
			try 
			{
				chi.doRun();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		} catch ( IOException ex ) 
		{
			logger.systemMessage(ex.getMessage());
			if (!stop) 
			{
				finish();
			}
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
	
	public void sendMessage (String user, String msg) 
	{
		ChatModel.sendChatMessage(user, msg);
		logger.publicChat(user, msg);
	}
	
	public void sendAskUserList () 
	{
		cho.sendUserList(ChatModel.getUserNames());
	}
	
	public void sendUserList (Collection<String> ulist) 
	{
		cho.sendUserList(ulist);
	}
	
	public void sendPrivateMessage (String from, String to, String msg) 
	{
		ChatModel.sendPrivateChatMessage(from, to, msg);
		logger.privateChat(from, to, msg);
	}
	
	@Override
	public void userListChanged() 
	{
		cho.sendUserList(ChatModel.getUserNames());
	}

	@Override
	public void chatMessageSent(String from, String message) 
	{
		cho.sendMessage(from, message);
	}

	@Override
	public void privateChatMessageSent(String from, String to, String message) 
	{
		cho.sendPrivateMessage(from, to, message);
	}

	@Override
	public void shutdownRequested() 
	{
		
	}
	
	public void sendQuit()
	{
		cho.sendQuit();
	}
	
	public void sendName(String name)
	{
		
	}
}