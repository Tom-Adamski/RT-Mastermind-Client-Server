package jeulettres.serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class GameService implements Runnable{

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
	
	public GameService(ServerSocket socketserveur, Socket socketjoueur, Joueur joueur){
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

	public void run(){
		try {
			while(competEnCours && clientConnecte){
				switch(state) {
				case "init":
					//mot.generer();
					mot.selectionner();
					msgToClient.println("init!");
					msgToClient.flush();
					msgToClient.println(Mot.TAILLE);
					msgToClient.flush();
					chrono.start();
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
					long seconds = chrono.getElapsedTime();
					if(seconds < 60)
						joueur.ajouterPoints(10);
					else if(seconds < 180) 
						joueur.ajouterPoints(5);
					else if(seconds < 300)
						joueur.ajouterPoints(2);
					else
						joueur.ajouterPoints(1);
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
		catch (IOException e) {e.printStackTrace();}            
		finally {
			clientConnecte = false;
		}
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

	public String getPseudo() {
		return joueur.getPseudo();
	}
	
	public String getPassword() {
		return joueur.getPassword();
	}
	
	public void reconnect(Socket socketjoueur) {
		try{
			System.out.println("Reconnecté : " + getPseudo());
			this.msgToClient = new PrintWriter(socketjoueur.getOutputStream());
			this.msgFromClient = new BufferedReader (new InputStreamReader (socketjoueur.getInputStream()));
			clientConnecte = true;
			this.run();
		}
			catch(IOException e){
				e.printStackTrace();
			}
	}
}