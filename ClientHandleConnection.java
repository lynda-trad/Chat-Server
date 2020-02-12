package chatModele;

import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.Random;

public class ClientHandleConnection extends Thread implements ChatProtocol 
{
	/*
	ChatInput chi;
	ChatOutput cho;
	Socket sock;
	
	public void sendNameOK() {
		f.acceptName();
	}
	
	public void sendUserList(Collection<String> ulist) {
		f.fillUserList(ulist);
	}
	
	public void sendMessage(String user, String msg) {
		f.putMessage(user + ": " + msg);
	}
	
	public void run() {
		try (Socket s1 = s) {
			chi = new ChatInput(s1.getInputStream(), this);
			chi.doRun();
		} catch (IOException ex) {
			finish();
		}
	}
	
	private void connect(){
		if(connected) 
			return;
		try {
			sock = new Socket(ConnectAddress,port);
			connected = true;
			connection = new ClientHandleConnection(sock, this);
			connection.start();
			Random r = new Random();
			if (nick.equals("test"))
				nick = nick + Integer.toString(r.nextInt());
			connection.sendName(nick);
			connection.askUserList();
		}
	}
	*/
}
