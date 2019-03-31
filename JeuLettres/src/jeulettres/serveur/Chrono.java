package jeulettres.serveur;

public class Chrono {
	
	private long tempsDebut;
	
	public Chrono() {
		this.tempsDebut = 0;
	}
	
	public void start() {
		tempsDebut = System.currentTimeMillis();
	}
	
	public long getElapsedTime() {
		return (System.currentTimeMillis() - tempsDebut)/1000;
	}
	
}
