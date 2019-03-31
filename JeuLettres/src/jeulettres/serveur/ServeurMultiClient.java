package jeulettres.serveur;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class ServeurMultiClient{
	
	static final int MAXJOUEURS = 10;
	
	static List<Joueur> joueurs;

	public static void main(String[] args) {
		ServerSocket socketserveur;
		joueurs = Collections.synchronizedList(new ArrayList<Joueur>());
		
		try {
			socketserveur = new ServerSocket(60000);
			ConnectionService connectionService = new ConnectionService(socketserveur);
			Thread connectionThread = new Thread(connectionService);
			connectionThread.start();
			

			Scanner scanner = new Scanner(System.in);
			String line = "";
			
			while(!line.equals("stop")) {
				line = scanner.nextLine();
				
				if(line.equals("reset")) {
					connectionService.resetScores();
					System.out.println("Les scores ont été réinitialisés");
				}
				
			}
			
			
		} catch (IOException e) {e.printStackTrace();}
	}
}


