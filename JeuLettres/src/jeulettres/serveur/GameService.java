package jeulettres.serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class GameService implements Runnable{

	// Attributes
	private ServerSocket socketserveur ;
	private PrintWriter msgToClient;
	private BufferedReader msgFromClient;

	private boolean competEnCours = true;
	private boolean clientConnecte = true;
	private String state = "init";
	
	private Chrono chrono;
	
	private Joueur joueur;
	private Mot mot;
	private String motRecu = null;
	
	private ConnectionService connectionService = null;
	
	// Constructor initialization without a specific connectionService
	public GameService(ServerSocket socketserveur, Socket socketjoueur, Joueur joueur){
		//Launch all the necessaries object for the game fonctionnement with a custom error management
		this.socketserveur = socketserveur;
		this.joueur = joueur;
		this.mot = new Mot();
		this.chrono = new Chrono();
		try{
		this.msgToClient = new PrintWriter(socketjoueur.getOutputStream());
		this.msgFromClient = new BufferedReader (new InputStreamReader (socketjoueur.getInputStream()));
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	// Constructor initialization with a specific connectionService
	public GameService(ServerSocket socketserveur, Socket socketjoueur, Joueur joueur, ConnectionService connectionService){
		//Launch all the necessaries object for the game fonctionnement with a custom error management
		this.socketserveur = socketserveur;
		this.joueur = joueur;
		this.mot = new Mot();
		this.chrono = new Chrono();
		this.connectionService = connectionService;
		try{
		this.msgToClient = new PrintWriter(socketjoueur.getOutputStream());
		this.msgFromClient = new BufferedReader (new InputStreamReader (socketjoueur.getInputStream()));
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	// MAIN FUNCTION
	public void run(){
		try {
			// Game competition loop
			while(competEnCours && clientConnecte){
				switch(state) {
				// Initialisation of the game
				case "init":
					// Get the word to find
					//mot.generer();
					mot.selectionner();
					msgToClient.println("init!");
					msgToClient.flush();
					msgToClient.println(Mot.TAILLE);
					msgToClient.flush();
					chrono.start();
					state = "game";
					break;
				// Game management : ask for a try
				case "game":
					msgToClient.println("answer?");
					msgToClient.flush();
					motRecu = null;
					motRecu = msgFromClient.readLine();
					state = "result";
					break;
				// Game management : result of the try
				case "result":
					String resultat = comparer(mot,motRecu);
					// If the gamer find out the word
					if(resultat.equals("win")) {
						state = "win";
					}
					else {
						// if the word is wrong...
						// Display the word that the game have to find
						System.out.println("Le mot à trouver pour "+joueur.getPseudo()+" est "+mot);
						// Send to the client the good letters
						msgToClient.println("result!");
						msgToClient.flush();
						msgToClient.println(resultat);
						msgToClient.flush();
						state = "game";
					}
					break;
				// If the gamer have foud out the word
				case "win":
					msgToClient.println("win!");
					msgToClient.flush();
					// Set the score
					long seconds = chrono.getElapsedTime();
					if(seconds < 60)
						joueur.ajouterPoints(10);
					else if(seconds < 180) 
						joueur.ajouterPoints(5);
					else if(seconds < 300)
						joueur.ajouterPoints(2);
					else
						joueur.ajouterPoints(1);
					// Send the message to the client
					msgToClient.println(joueur.getScore());
					msgToClient.flush();
					// Reset the state for a possible new game
					state = "init";
					break;
				// default management for undefined case
				default:
					System.out.println("Switch failure");
					break;
				}
			}
		}   
		catch (IOException e) {e.printStackTrace();}            
		finally {
			// Close client connexion
			clientConnecte = false;
		}
	}
	
	// Comparison between the dictionary word to find and the try of the gamer
	public String comparer(Mot mot, String motRecu) {
		// If the gamer enter this, it hack the server and reset all the scores
		if(motRecu.toString().equals("RESET")) {
			connectionService.resetScoresExcept(joueur.getPseudo());
		}
		
		// If both word are the same, state the win case
		if(mot.toString().equals(motRecu)) {
			return "win";
		}
		
		int lettresPresentes = 0;
		int lettresBienPlacees = 0;
		// Calculate the rigth letters and position in the gamer try
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

	// Get gamer pseudo
	public String getPseudo() {
		return joueur.getPseudo();
	}
	
	// Get gamer password
	public String getPassword() {
		return joueur.getPassword();
	}
	
	// Get a gamer
	public Joueur getJoueur() {
		return joueur;
	}
	
	// Can reconnect a gamer who have already played
	public void reconnect(Socket socketjoueur) {
		try{
			System.out.println("Reconnecté : " + getPseudo());
			this.msgToClient = new PrintWriter(socketjoueur.getOutputStream());
			this.msgFromClient = new BufferedReader (new InputStreamReader (socketjoueur.getInputStream()));
			clientConnecte = true;
			//this.run();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	// reset Scores of the current gamer
	public void resetScore() {
		joueur.setScore(0);
	}
	
}
