package chatModele;

public class TextChatLogger implements IChatLogger {

	@Override
	public void clientConnected(String ip) {
		System.out.println("client connected: " + ip);
	}

	@Override
	public void clientDisconnected(String ip, String name) {
		System.out.println("client disconnected: " +ip+ " ("+name+")" );
	}

	@Override
	public void clientGotName(String ip, String name) {
		System.out.println("client " +ip+ " got name: " +name);
	}

	@Override
	public void clientGotCommand(String name, int command) {
		System.out.println("client got " +name+ " command: "+ command);
	}

	@Override
	public void publicChat(String from, String msg) {
		System.out.println("public chat from " +from+ ": " +msg);
	}

	@Override
	public void privateChat(String from, String to, String msg) {
		System.out.println("private chat from " +from+ " to " +to+ ": " +msg);
	}

	@Override
	public void systemMessage(String msg) {
		System.out.println("system message: " +msg);
	}

}
