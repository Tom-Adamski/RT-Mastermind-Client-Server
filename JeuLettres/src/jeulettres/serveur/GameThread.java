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
	
	public boolean update(String name, Socket s) {
		if(gameService.getPseudo().equals(name)) {
			gameService.reconnect(s);
			return true;
		}
		return false;
	}

}
