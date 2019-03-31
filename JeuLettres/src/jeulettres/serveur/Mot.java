package jeulettres.serveur;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class Mot {
	
	public static final int TAILLE = 5;
	private String sequence;
	private ArrayList dictionnaire;
	private boolean pret;
	
	public Mot() {
		generer();
		selectionner();
	}
	

	public void generer(){
		this.sequence = "";
		this.dictionnaire = new ArrayList<String>();

		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
		for(int i=0; i<TAILLE; i++){
			int l = (int)Math.floor(Math.random() * 26);
			sequence += alphabet.charAt(l);
		}
		
	}
	
	public void selectionner(){ // récuperation d'un mot dans un fichier txt
		this.sequence = "";
		
		try {
			// Chemin du dossier contenant le fichier
			File currentDirectory = new File(new File(".").getAbsolutePath());
			String path = currentDirectory.getCanonicalPath();

			
			BufferedReader lecteur = new BufferedReader (new FileReader(path+ "/dictionnaire.txt"));    
			String ligne;
			int compteur_mots = 0;
			
			while ((ligne = lecteur.readLine()) != null) 
			{
	        	// Récupération de tout les mots dans le tableau de mots
	        	// En les ajoutant à ceux existant
				// this.dictionnaire = ObjectArrays.concat(this.dictionnaire, ligne.split(" "), String.class);
	        	for(String w : ligne.split(" "))
	        	{
	        		this.dictionnaire.add(w);
	        	}
	        }

			compteur_mots = this.dictionnaire.size();
			int Min = 0;
			int nbrRandom = Min + (int)(Math.random() * ((compteur_mots - Min))); // entre 0 et longueur de liste
		
			this.sequence = this.dictionnaire.get(nbrRandom).toString().toUpperCase();

	     	// Fermeture de la ressource de fichier
			lecteur.close();
	 	} 
	 	catch(IOException e) 
	 	{
             System.out.println("Exception : " + e);
        }
	}
	
	
	
	@Override
	public String toString() {
		return this.sequence;
	}
	
}