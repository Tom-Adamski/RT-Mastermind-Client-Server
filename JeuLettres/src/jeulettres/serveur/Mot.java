package jeulettres.serveur;

public class Mot {
	
	public static final int TAILLE = 5;
	private String sequence;
	private boolean pret;
	
	
	public Mot() {
		generer();
	}
	

	public void generer(){
		this.sequence = "";
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		
		for(int i=0; i<TAILLE; i++){
			int l = (int)Math.floor(Math.random() * 26);
			sequence += alphabet.charAt(l);
		}
		
	}
	
	
	
	@Override
	public String toString() {
		return this.sequence;
	}
	
}