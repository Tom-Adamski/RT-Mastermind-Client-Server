package jeulettres.serveur;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class ServeurMultiClient{
	
	static final int MAXJOUEURS = 3;
	
	static List<Joueur> joueurs;

	public static void main(String[] args) {
		ServerSocket socketserveur;
		joueurs = Collections.synchronizedList(new ArrayList<Joueur>());
		
		try {
			socketserveur = new ServerSocket(60000);
			Thread connectionThread = new Thread(new ConnectionService(socketserveur));
			connectionThread.start();
			
		} catch (IOException e) {e.printStackTrace();}
	}
}


