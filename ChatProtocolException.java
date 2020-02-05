package server;

public class ChatProtocolException extends Exception 
{
	public ChatProtocolException(String string) throws Exception 
	{
		throw new Exception(string);
	}

}
