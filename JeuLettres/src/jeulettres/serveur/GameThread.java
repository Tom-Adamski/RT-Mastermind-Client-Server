package jeulettres.serveur;

import java.net.ServerSocket;
import java.net.Socket;

public class GameThread extends Thread {
	
	// Attribute
	private GameService gameService;
	
	// Constructor initialisation
	public GameThread(Runnable target) {
		super(target);
		if(target instanceof GameService)
			this.gameService = (GameService)target;
		}
	
	// Get a gamer in the gameService
	public Joueur getJoueur() {
		return gameService.getJoueur();
	}
	
	// Check the existence of the gamer pseudo in the gamerService
	public boolean checkPseudo(String pseudo) {
		if(gameService.getPseudo().equals(pseudo)) {
			return true;
		}
		return false;
	}
	
	// Check a gamer password for a specific gamer
	public boolean checkPassword(String password) {
		if(gameService.getPassword().equals(password)) {
			return true;
		}
		return false;
	}
	
	// Rest all gamer scores
	public void resetScore() {
		gameService.resetScore();
	}

}
