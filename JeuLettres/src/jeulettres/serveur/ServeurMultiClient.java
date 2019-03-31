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
	
	//Attributes
	static final int MAXJOUEURS = 10;
	static List<Joueur> joueurs;

	//MAIN FUNCTION
	public static void main(String[] args) {
		// Variable initialisation
		ServerSocket socketserveur;
		joueurs = Collections.synchronizedList(new ArrayList<Joueur>());
		
		try {
			//Start the server connexion on a specific port
			socketserveur = new ServerSocket(60000);
			ConnectionService connectionService = new ConnectionService(socketserveur);
			Thread connectionThread = new Thread(connectionService);
			connectionThread.start();
			
			//Initialize a scanner listenning 
			Scanner scanner = new Scanner(System.in);
			String line = "";
			
			//Game loop
			while(!line.equals("stop")) {
				//Get the entry of the server administator
				line = scanner.nextLine();
				
				// If the administrator enter this word, it reset all the scores of all the gamers
				if(line.equals("reset")) {
					connectionService.resetScores();
					System.out.println("Les scores ont été réinitialisés");
				}
			}	
		} catch (IOException e) {e.printStackTrace();}
	}
}
