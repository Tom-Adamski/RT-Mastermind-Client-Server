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
	
	private PrintWriter msgToClient;
	private BufferedReader msgFromClient;
	private ServerSocket socketserveur;
	private Socket s ;
	private int nbClients;
	
	private boolean serverOn;
	
	private List<GameThread> gameThreads;
	
	public ConnectionService(ServerSocket socketserveur) {
		this.socketserveur = socketserveur;
		this.nbClients = 0;
		this.gameThreads = new ArrayList<>();
		this.serverOn = true;
	}

	@Override
	public void run() {
		// écoute d'un service entrant -association socket client et socket serveur.
		while(serverOn) {
		try {
			
			System.out.println("Attente de connexion");
			
			if(nbClients < ServeurMultiClient.MAXJOUEURS) {
				
				//attente de connection
				s = socketserveur.accept(); 
				String ipClient = s.getInetAddress().toString();
				System.out.println("Un client s'est connecté :" + ipClient);

				//récupération du pseudo
				msgToClient = new PrintWriter(s.getOutputStream());
				msgToClient.println("Bonjour "+ipClient+", quel est votre nom?");
				msgToClient.flush();
				msgFromClient = new BufferedReader (new InputStreamReader (s.getInputStream()));
				String nomRecu = null;
				nomRecu = msgFromClient.readLine();

				//envoi du message d'accueil
				msgToClient = new PrintWriter(s.getOutputStream());
				if(nomRecu!=null){
					msgToClient.println("Bonjour "+nomRecu+" !");
					msgToClient.flush();
				}
				
				System.out.println("Recu : " +nomRecu);
				
				//ajout du joueur dans la liste
				boolean playerFound = false;
				for(GameThread g : gameThreads) {
					if(g.checkPseudo(nomRecu)) {
						
						//récupération du mot de passe
						msgToClient = new PrintWriter(s.getOutputStream());
						msgToClient.println("Entrez votre mot de passe : ");
						msgToClient.flush();
						msgFromClient = new BufferedReader (new InputStreamReader (s.getInputStream()));
						String password = null;
						password = msgFromClient.readLine();
						
						//on reconnecte le joueur s'il utilise le bon mot de passe
						if(g.checkPassword(password)) {
							playerFound = true;
							Joueur joueur = g.getJoueur();
							GameThread gameThread = new GameThread(new GameService(socketserveur, s, joueur));
							gameThread.start();
							gameThreads.add(gameThread);
							
							gameThreads.remove(g);
							
						}
						
					}
					if(playerFound) break;
				}
				
				if(playerFound != true) {
					
					//demande du mot de passe
					msgToClient = new PrintWriter(s.getOutputStream());
					msgToClient.println("Choisissez votre mot de passe : ");
					msgToClient.flush();
					msgFromClient = new BufferedReader (new InputStreamReader (s.getInputStream()));
					String password = null;
					password = msgFromClient.readLine();
					
					
					GameThread gameThread = new GameThread(new GameService(socketserveur, s, new Joueur(0, nomRecu, password, 0, 0, ipClient)));
					gameThread.start();
					gameThreads.add(gameThread);
					nbClients++;
				}
				
				System.out.println(nomRecu + " est connecté.");
			}
			else {
				s = socketserveur.accept(); 
				String ipClient = s.getInetAddress().toString();
				System.out.println(ipClient + " ne peut pas se connecter, le serveur est plein !");
				msgToClient = new PrintWriter(s.getOutputStream());
				msgToClient.println("full");
				msgToClient.flush();
			}
		}
		catch (IOException e) {e.printStackTrace();}            
		catch (Exception e) {e.printStackTrace();}
		
	}
	}
	
}