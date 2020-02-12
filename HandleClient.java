package chatModele;

import java.io.IOException;
import java.net.Socket;

public class HandleClient implements Runnable, ChatProtocol, ChatModelEvents {
	
	private final Socket s;
	private ChatOutput cho;
	private ChatInput chi;
	private String name = "";
	private IChatLogger logger = null;
	
	private enum ClientState {
		ST_INIT, ST_NORMAL
	};
	
	private ClientState state = ClientState.ST_INIT;
	private boolean stop = false;
	
	public HandleClient(Socket s, IChatLogger logger) throws IOException {
		this.s = s;
		this.logger = logger;
	}
	
	public void run() {
		try (Socket s1 = s) {
			cho = new ChatOutput(s1.getOutputStream());
			chi = new ChatInput(s1.getInputStream(), this);
			chi.doRun();
		} catch (IOException ex) {
		
			if (!stop) {
				finish();
			}
		}
	}

	public void sendName(String name) {
		String newName = name;
		if (ChatModel.existUserName(newName)) {
			cho.sendNameBad();
		} else {
			if (state == ClientState.ST_INIT) {
				ChatModel.registerUser(newName, this);
				state = ClientState.ST_NORMAL;
			} else {
				ChatModel.renameUser(name, newName, this);
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
		// TODO Auto-generated method stub
	}

	@Override
	public void chatMessageSent(String from, String message) 
	{
		// TODO Auto-generated method stub	
	}

	@Override
	public void privateChatMessageSent(String from, String to, String message) 
	{
		// TODO Auto-generated method stub	
	}

	@Override
	public void shutdownRequested() 
	{
		// TODO Auto-generated method stub	
	}
}
