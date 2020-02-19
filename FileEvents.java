package chatModele;

import java.io.File;

public interface FileEvents 
{
	public void fileSent(String from, String filename, File f);
}
