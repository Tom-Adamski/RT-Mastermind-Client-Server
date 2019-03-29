package jeulettres.serveur;


public class Joueur {
	
	private int codeLicencie;
	private String pseudo;
	private int fidelite;
	private int score;
	private String ip;
	
	
		
	public Joueur(int codeLicencie, String pseudo, int fidelite, int score, String ip) {
		super();
		this.codeLicencie = codeLicencie;
		this.pseudo = pseudo;
		this.fidelite = fidelite;
		this.score = score;
		this.ip = ip;
	}
	
	
	
	
	public int getCodeLicencie() {
		return codeLicencie;
	}
	public void setCodeLicencie(int codeLicencie) {
		this.codeLicencie = codeLicencie;
	}
	public String getPseudo() {
		return pseudo;
	}
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	public int getFidelite() {
		return fidelite;
	}
	public void setFidelite(int fidelite) {
		this.fidelite = fidelite;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public void ajouterPoints(int points) {
		this.score += points;
	}

	
	
	

}