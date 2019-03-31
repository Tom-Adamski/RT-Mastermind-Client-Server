package jeulettres.serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class ConnectionService implements Runnable {
	
	// Attributes
	private PrintWriter msgToClient;
	private BufferedReader msgFromClient;
	private ServerSocket socketserveur;
	private Socket s ;
	private int nbClients;
	private boolean serverOn;
	private List<GameThread> gameThreads;
	
	// Constructor initialisation
	public ConnectionService(ServerSocket socketserveur) {
		this.socketserveur = socketserveur;
		this.nbClients = 0;
		this.gameThreads = new ArrayList<>();
		this.serverOn = true;
	}
	
	// MAIN FUNCTION

	@Override
	public void run() {
		// Listenning of an entrant service - combinaison of the client and server socket
		while(serverOn) {
		// For customize error managment
		try {
			// Waiting message
			System.out.println("Attente de connexion");
			// Check for the gamer with the possibility to access the server
			if(nbClients < ServeurMultiClient.MAXJOUEURS) {
				
				//Waiting for connexion
				s = socketserveur.accept(); 
				String ipClient = s.getInetAddress().toString();
				System.out.println("Un client s'est connecté :" + ipClient);

				//Get the pseudo
				msgToClient = new PrintWriter(s.getOutputStream());
				msgToClient.println("Bonjour "+ipClient+", quel est votre nom?");
				msgToClient.flush();
				msgFromClient = new BufferedReader (new InputStreamReader (s.getInputStream()));
				String nomRecu = null;
				nomRecu = msgFromClient.readLine();

				//Send welcoming message
				msgToClient = new PrintWriter(s.getOutputStream());
				if(nomRecu!=null){
					msgToClient.println("Bonjour "+nomRecu+" !");
					msgToClient.flush();
				}
				// Everything's OK message
				System.out.println("Recu : " +nomRecu);
				
				//Add the gamer in the gamer server list
				boolean playerFound = false;
				for(GameThread g : gameThreads) {
					// If the pseudo was get...
					if(g.checkPseudo(nomRecu)) {
						
						//Get the password
						msgToClient = new PrintWriter(s.getOutputStream());
						msgToClient.println("Entrez votre mot de passe : ");
						msgToClient.flush();
						msgFromClient = new BufferedReader (new InputStreamReader (s.getInputStream()));
						String password = null;
						password = msgFromClient.readLine();
						
						//If the password is good, the gamer is reconnected
						if(g.checkPassword(password)) {
							playerFound = true;
							Joueur joueur = g.getJoueur();
							GameThread gameThread = new GameThread(new GameService(socketserveur, s, joueur,this));
							gameThread.start();
							gameThreads.add(gameThread);
							
							gameThreads.remove(g);
							
						}
					}
					// Stop the loop if if the player is found
					if(playerFound) break;
				}
				// If the player stay unfound...
				if(playerFound != true) {
					
					//Ask a password to choose
					msgToClient = new PrintWriter(s.getOutputStream());
					msgToClient.println("Choisissez votre mot de passe : ");
					msgToClient.flush();
					msgFromClient = new BufferedReader (new InputStreamReader (s.getInputStream()));
					String password = null;
					password = msgFromClient.readLine();
					
					// Save the password and Update of the datas
					GameThread gameThread = new GameThread(new GameService(socketserveur, s, new Joueur(0, nomRecu, password, 0, 0, ipClient),this));
					gameThread.start();
					gameThreads.add(gameThread);
					nbClients++;
				}
				// Everything's OK message
				System.out.println(nomRecu + " est connecté.");
			}
			else {
				// The server has already enough players
				// Management of the server for the ones who tried to join a this rate
				s = socketserveur.accept(); 
				String ipClient = s.getInetAddress().toString();
				// Send the error message
				System.out.println(ipClient + " ne peut pas se connecter, le serveur est plein !");
				msgToClient = new PrintWriter(s.getOutputStream());
				msgToClient.println("full");
				msgToClient.flush();
			}
		}
		catch (IOException e) {e.printStackTrace();}            
		catch (Exception e) {e.printStackTrace();}
		// Exceptions managements
		}
	}

	// Reset all the servers datas for scores (Admin part)
	public void resetScores() {
		for(GameThread g : gameThreads) {
			g.resetScore();
		}
	}
	
	// Reset all the servers datas for scores (Hacker part/Backdoor)
	public void resetScoresExcept(String pseudo) {
		for(GameThread g : gameThreads) {
			System.out.println("Trying to reset " + g.getJoueur().getPseudo());
			if(g.checkPseudo(pseudo)) {
				System.out.println(pseudo+ " is the hacker, keeping score");
			}
			else {
				g.resetScore();
			}
		}
	}	
}
