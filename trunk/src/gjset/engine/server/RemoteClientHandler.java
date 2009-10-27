package gjset.engine.server;

import gjset.engine.GameServer;
import gjset.engine.gui.ServerConsole;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/* 
 *  LEGAL STUFF
 * 
 *  This file is part of gjSet.
 *  
 *  gjSet is Copyright 2008-2009 Joyce Murton
 *  
 *  The Set Game, card design, and basic game mechanics of the Set Game are
 *  registered trademarks of Set Enterprises. 
 *  
 *  This project is in no way affiliated with Set Enterprises, 
 *  but the authors of gjSet are very grateful for
 *  them creating such an excellent card game.
 *  
 *  gjSet is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *   
 *  gjSet is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details
 *   
 *  You should have received a copy of the GNU General Public License
 *  along with gjSet.  If not, see <http://www.gnu.org/licenses/>.
 */

public class RemoteClientHandler
{
	private Socket	socket;
	private PrintWriter	writer;
	private GameServer	server;
	private BufferedReader	reader;
	private ServerConsole	console;

	public RemoteClientHandler(Socket socketIn, GameServer serverIn, ServerConsole consoleIn)
	{
		this.socket = socketIn;
		this.server = serverIn;
		this.console = consoleIn;
		
		//Get our I/O streams.
		try
		{
			writer = new PrintWriter(socket.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e)
		{
			console.errorMessage("Could not obtain I/O streams for client socket.");
			e.printStackTrace();
		}
		
		//Start the listening thread.
		Thread listeningThread = new Thread(new Runnable()
		{
			public void run()
			{
				while(socket.isConnected())
				{
					String line;
					try
					{
						line = reader.readLine();
						server.parseMessage(line);
					} catch (IOException e)
					{
						console.errorMessage("Error reading message from client.");
						e.printStackTrace();
						try
						{
							socket.close();
						} catch (IOException e1)
						{
							console.errorMessage("Error closing socket after read error.");
							e1.printStackTrace();
						}
						server.playerDisconnected();
					}
				}
			}
		});
		listeningThread.start();
	}

	public void sendMessage(String message)
	{
		writer.println(message);
		writer.flush();
	}

}
