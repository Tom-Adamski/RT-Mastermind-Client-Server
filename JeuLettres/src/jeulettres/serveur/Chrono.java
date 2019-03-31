package jeulettres.serveur;

public class Chrono {
	
	// Attributes
	private long tempsDebut;
	
	// Initialisation of the attribute
	public Chrono() {
		this.tempsDebut = 0;
	}
	
	// Launch the chrono
	public void start() {
		tempsDebut = System.currentTimeMillis();
	}
	
	// Get the chrono time when called
	public long getElapsedTime() {
		return (System.currentTimeMillis() - tempsDebut)/1000;
	}
	
}
