package jeulettres.serveur;


public class Joueur {
	
	//Attributes
	private int codeLicencie;
	private String pseudo;
	private String password;
	private int fidelite;
	private int score;
	private String ip;
	
	
	// Constructor initialisation of a gamer	
	public Joueur(int codeLicencie, String pseudo, String password, int fidelite, int score, String ip) {
		super();
		this.codeLicencie = codeLicencie;
		this.pseudo = pseudo;
		this.password = password;
		this.fidelite = fidelite;
		this.score = score;
		this.ip = ip;
	}
	
	// NOT USED
	public int getCodeLicencie() {
		return codeLicencie;
	}
	// NOT USED
	public void setCodeLicencie(int codeLicencie) {
		this.codeLicencie = codeLicencie;
	}
	// Get the current gamer pseudo
	public String getPseudo() {
		return pseudo;
	}
	// Set the current gamer pseudo
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	// Get the current gamer fidelite
	public int getFidelite() {
		return fidelite;
	}
	// Set the current gamer fidelite
	public void setFidelite(int fidelite) {
		this.fidelite = fidelite;
	}
	// Get the current gamer score
	public int getScore() {
		return score;
	}
	// Set the current gamer score
	public void setScore(int score) {
		this.score = score;
	}
	// Add point to the gamer score
	public void ajouterPoints(int points) {
		this.score += points;
	}
	// Get the current gamer password
	public String getPassword() {
		return password;
	}
	// Set the current gamer password
	public void setPassword(String password) {
		this.password = password;
	}
	// Get the current gamer IP address
	public String getIp() {
		return ip;
	}
	// Set the current gamer IP address
	public void setIp(String ip) {
		this.ip = ip;
	}
}
