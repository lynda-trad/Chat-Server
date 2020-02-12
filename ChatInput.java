package chatModele;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ChatInput 
{	
	ChatProtocol handler;
	InputStream in;
	
	public ChatInput(InputStream in, ChatProtocol handler) throws IOException 
	{
		this.in = in;
		this.handler = handler;
	}
	
	public void doRun() throws IOException 
	{
		String strMsg, strName;
		ArrayList<String> userList;
		
		try (BufferedReader is = new BufferedReader(new InputStreamReader(in)))
		{
			boolean stop = false;
			while (!stop) 
			{
				String line = is.readLine();
				switch (line) 
				{
					case "NAME":
						strName = is.readLine();
						handler.sendName(strName);
					break;
						
					case "NAME OK":
						handler.sendNameOK();
					break;
						
					case "NAME BAD":
						handler.sendNameBad();
					break;
					
					case "MESSAGE":
						strName = is.readLine();
						strMsg = is.readLine();
						handler.sendMessage(strName, strMsg);
					break;
						
					case "PRIVATE MESSAGE":
						strName = is.readLine();
						String dst = is.readLine();
						strMsg = is.readLine();
						handler.sendPrivateMessage(strName, dst, strMsg);
					break;
						
					case "AULIST":
						handler.sendAskUserList();
					break;
					
					case "ULIST":
						userList = new ArrayList<>();
						String x;
						while (!(x = is.readLine()).equals(".")) 
						{
							userList.add(x);
						}
						handler.sendUserList(userList);
					break;
						
					case "QUIT":
						handler.sendQuit();
					break;
						
					default:
						throw new ChatProtocolException("Invalid input");
				}
			}
		}
	}
}