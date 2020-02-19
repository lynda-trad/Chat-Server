package chatModele;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TestClient 
{	
	public static void main(String[] args) 
	{
		try 
		{
			Socket s = new Socket("localhost", 1234);
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			PrintWriter pw = new PrintWriter(s.getOutputStream(), false);
			boolean stop = false;
			Scanner sc = new Scanner(System.in);
			while(!stop) 
			{
				String cmd = sc.nextLine();
				switch(cmd) 
				{
				case "":
					pw.flush();
					System.out.println("envoyé");
					String res = br.readLine();
					if(res != null) 
						System.out.println(res);
					break;
				case "q":
					stop = true;
					System.out.println("quitté");
					break;
				default:
					pw.println(cmd);
					break;
				}
			}
			sc.close();
			s.close();
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}

}