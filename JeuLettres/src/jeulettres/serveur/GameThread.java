package jeulettres.serveur;

import java.net.ServerSocket;
import java.net.Socket;

public class GameThread extends Thread {
	
	private GameService gameService;
	
	public GameThread(Runnable target) {
		super(target);
		if(target instanceof GameService)
			this.gameService = (GameService)target;
		}
	
	public void reconnect(Socket socketjoueur) {
		gameService.reconnect(socketjoueur);
	}
	
	public boolean checkPseudo(String pseudo) {
		if(gameService.getPseudo().equals(pseudo)) {
			return true;
		}
		return false;
	}
	
	public boolean checkPassword(String password) {
		if(gameService.getPassword().equals(password)) {
			return true;
		}
		return false;
	}

}
