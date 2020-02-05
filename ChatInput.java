package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ChatInput 
{
	ChatProtocol handler;
	InputStream in;
	
	public ChatInput (InputStream in, ChatProtocol handler) throws IOException
	{
		this.in = in;
		this.handler = handler;
	}
	
	public void doRun() throws Exception
	{
		String strMsg , strName;
		ArrayList<String> userList;
		
		try ( BufferedReader is = new BufferedReader (new InputStreamReader (in)) )
		{
			while (handler.getStop()) //!stop 
			{
				String line = is.readLine();
				switch (line) 
				{
				
				case "NAME":
					strName	= is.readLine();
					handler.sendName(strName);
					break;
					
				case "MESSAGE":
					strName = is.readLine();
					strMsg = is.readLine();
					handler.sendMessage(strName , strMsg);
					break;
					
				case "ULIST":
					userList = new ArrayList<>();
					String x;
					while (!(x = is.readLine ()).equals("."))
					{
						userList.add(x);
					}
					handler.sendUserList(userList);
					break;
					
				case "PRIVATE MESSAGE":
					String from = is.readLine();
					String to = is.readLine();
					strMsg = is.readLine();
					handler.sendPrivateMessage(from, to, strMsg);
					break;

				case "QUIT":
					handler.sendQuit();
					break;

				case "NAME OK":
					handler.sendNameOK();
					break;
					
				case "NAME BAD":
					handler.sendNameBad();
					break;
					

				case "AULIST":
					handler.sendAskUserList();
					break;
					
				default : 
					System.out.println(line);
					throw new ChatProtocolException("Invalid Input");
				}
			}
		}
	}
}
