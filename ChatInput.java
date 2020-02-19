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
						
					// Rooms
					
					case "CREATE ROOM":
						strName = is.readLine();
						handler.sendCreateRoom(strName);
					break;

					case "ROOM OK":
						handler.sendRoomOK();
					break;

					case "ROOM BAD":
						handler.sendRoomBad();
					break;

					case "DELETE ROOM":
						strName = is.readLine();						
						handler.deleteRoom(strName);
					break;
					
					case "ARLIST":
						handler.sendAskRoomList();
					break;
					
					case "RLIST":
						userList = new ArrayList<>();
						String r;
						while (!(r = is.readLine()).equals(".")) 
						{
							userList.add(r);
						}
						handler.sendRoomList(userList);
					break;
					
					case "ARULIST":
						strName = is.readLine();
						handler.sendAskRoomUserList(strName);
					break;
					
					case "RULIST":
						userList = new ArrayList<>();
						String ru;
						while (!(ru = is.readLine()).equals(".")) 
						{
							userList.add(ru);
						}
						handler.sendRoomUserList(userList);
					break;
					
					case "ENTER ROOM":
						strName = is.readLine();
						handler.enterRoom(strName);
					break;

					case "LEAVE ROOM":
						strName = is.readLine();
						handler.leaveRoom(strName);
					break;
					
					case "ROOM MESSAGE":
						String room = is.readLine();
						strName = is.readLine();
						strMsg = is.readLine();
						handler.sendRoomMessage(room, strName, strMsg);
					break;
					
					// Files
					
					case "PROPOSE FILE":
						strName = is.readLine();
						strMsg = is.readLine();
						handler.proposeFile(strName, strMsg);
					break;
					
					case "ACCEPT FILE":
						strName = is.readLine();
						strMsg = is.readLine();
						handler.acceptFile(strName, strMsg);						
					break;
					
					case "REFUSE FILE":
						strName = is.readLine();
						strMsg = is.readLine();
						handler.refuseFile(strName, strMsg);
					break;

					// how ?
					
					/*
					case "SEND FILE":
						strName = is.readLine();
						strMsg = is.readLine();
						handler.sendFile(strName, strMsg);
					break;
					*/
					
					default:
						throw new ChatProtocolException("Invalid input");
				}
			}
		}
	}
}