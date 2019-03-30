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
			Thread connectionThread = new Thread(new ConnectionService(socketserveur, joueurs));
			connectionThread.start();
			
		} catch (IOException e) {e.printStackTrace();}
	}
}

class GameService implements Runnable{

	private ServerSocket socketserveur ;
	private PrintWriter msgToClient;
	private BufferedReader msgFromClient;
	
	private boolean competEnCours = true;
	private String state = "init";
	
	private Joueur joueur;
	private Mot mot;
	private String motRecu = null;
	
	public GameService(ServerSocket socketserveur, Socket socketjoueur, Joueur joueur){
		this.socketserveur = socketserveur;
		this.joueur = joueur;
		this.mot = new Mot();
		try{
		this.msgToClient = new PrintWriter(socketjoueur.getOutputStream());
		this.msgFromClient = new BufferedReader (new InputStreamReader (socketjoueur.getInputStream()));
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	public void run(){
		try {
			while(competEnCours){
				switch(state) {
				case "init":
					//mot.generer();
					mot.selectionner();
					msgToClient.println("init!");
					msgToClient.flush();
					msgToClient.println(Mot.TAILLE);
					msgToClient.flush();
					state = "game";
					break;
				case "game":
					msgToClient.println("answer?");
					msgToClient.flush();
					motRecu = null;
					motRecu = msgFromClient.readLine();
					state = "result";
					
					break;
				case "result":
					String resultat = comparer(mot,motRecu);
					if(resultat.equals("win")) {
						state = "win";
					}
					else {
						System.out.println("Le mot à trouver pour "+joueur.getPseudo()+" est "+mot);
						msgToClient.println("result!");
						msgToClient.flush();
						msgToClient.println(resultat);
						msgToClient.flush();
						state = "game";
					}
					break;
				case "win":
					msgToClient.println("win!");
					msgToClient.flush();
					joueur.ajouterPoints(10);
					msgToClient.println(joueur.getScore());
					msgToClient.flush();
					state = "init";
					break;
				default:
					System.out.println("Switch failure");
					break;
				}
				
			
			}
		}   
		//catch (IOException e) {e.printStackTrace();}            
		catch (Exception e) {e.printStackTrace();}
	}
	
	public String comparer(Mot mot, String motRecu) {
		
		if(mot.toString().equals(motRecu)) {
			return "win";
		}
		
		int lettresPresentes = 0;
		int lettresBienPlacees = 0;
		
		for(int i=0; i < Mot.TAILLE; i++){
			int occurences = 1;

			for(int k=0; k<i; k++) {				
				if(motRecu.charAt(i) == motRecu.charAt(k)){
					occurences++;
				}
			}
			
			for(int j=0; j < Mot.TAILLE; j++){
				if(motRecu.charAt(i) == mot.toString().charAt(j)){
					if(i==j) {
						lettresBienPlacees++;
					}
					occurences--;
					if(occurences == 0) {
						lettresPresentes++;
					}
				}
			}
		}
		return lettresPresentes+"|"+lettresBienPlacees;
		
	}

}

class ConnectionService implements Runnable {
	
	private PrintWriter msgToClient;
	private BufferedReader msgFromClient;
	private List<Joueur> joueurs;
	private ServerSocket socketserveur;
	private Socket s ;
	private int nbClients;
	
	private boolean serverOn;
	
	private List<Thread> gameThreads;
	
	public ConnectionService(ServerSocket socketserveur, List<Joueur> joueurs) {
		this.socketserveur = socketserveur;
		this.joueurs = joueurs;
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
				
				//ajout du joueur dans la liste
				Thread gameThread = new Thread(new GameService(socketserveur, s, new Joueur(0, nomRecu, 0, 0, ipClient)));
				gameThread.start();
				gameThreads.add(gameThread);
								
				
				nbClients++;
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