package chatModele;

import java.io.File;
import java.util.Collection;

public interface ChatProtocol 
{
	default void sendName(String name) {}
	default void sendNameOK() {}
	default void sendNameBad() {}
	default void sendMessage(String user, String message) {}
	default void sendAskUserList() {}
	default void sendUserList(Collection<String> ulist) {}
	default void sendPrivateMessage(String from, String to, String message) {}
	default void sendQuit() {}
	
	//Rooms
	
	default void sendCreateRoom(String room) {}
	default void sendRoomOK() {}
	default void sendRoomBad() {}
	default void deleteRoom(String room) {}
	
	default void sendAskRoomList() {}
	default void sendRoomList(Collection<String> ulist) {}
	
	default void sendAskRoomUserList(String room) {}
	default void sendRoomUserList(Collection<String> ulist) {}
	
	default void enterRoom(String room) {}
	default void leaveRoom(String room) {}

	default void sendRoomMessage(String room, String user, String message) {}
	
	//Files 
	
	default void proposeFile(String user, String filename) {}
	default void acceptFile(String user, String filename) {}
	default void refuseFile(String user, String filename) {}
	default void sendFile(String user, String filename, File f) {}
}