package server;

import java.util.Collection;

public interface ChatProtocol
{
	boolean getStop();
	
	default void sendName (String name){}
	
	default void sendNameOK(){}
	
	default void sendNameBad () {}
	
	default void sendMessage (String user, String msg) {}
	
	default void sendAskUserList () {}
	
	default void sendUserList (Collection<String> ulist ) {}
	
	default void sendPrivateMessage (String from, String to, String msg) {}
	
	default void sendQuit() {}
	
}