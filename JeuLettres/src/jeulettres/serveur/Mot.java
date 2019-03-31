package jeulettres.serveur;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class Mot {
	
	//Attributes
	public static final int TAILLE = 5;
	private String sequence;
	private ArrayList dictionnaire;
	private boolean pret;
	
	//Constructor initialisation
	public Mot() {
		//Generate a word
		generer();
		// Select a word in a dictionary file
		selectionner();
	}
	

	// Generate a word of 5 letters
	public void generer(){
		// Initialization of the attributes
		this.sequence = "";
		this.dictionnaire = new ArrayList<String>();

		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		//Creation of the word
		for(int i=0; i<TAILLE; i++){
			int l = (int)Math.floor(Math.random() * 26);
			sequence += alphabet.charAt(l);
		}
		
	}
	
	// Get a word in a dictionnary txt file
	public void selectionner(){ 
		//Set the choosen word
		this.sequence = "";
		
		try {
			// Path of the folder which contains the dictionnary file
			File currentDirectory = new File(new File(".").getAbsolutePath());
			String path = currentDirectory.getCanonicalPath();

			// Get the content of the files
			BufferedReader lecteur = new BufferedReader (new FileReader(path+ "/dictionnaire.txt"));    
			String ligne;
			int compteur_mots = 0;
			
			while ((ligne = lecteur.readLine()) != null) 
			{
				// Get all the word in a array of String, that already containt words
				for(String w : ligne.split(" "))
				{
					this.dictionnaire.add(w);
				}
	        	}
			// Counter
			compteur_mots = this.dictionnaire.size();
			int Min = 0;
			// Get a random number to select a random word with this as the index in the array of words
			int nbrRandom = Min + (int)(Math.random() * ((compteur_mots - Min))); // Between 0 and the Counter value
			// Set the selected word
			this.sequence = this.dictionnaire.get(nbrRandom).toString().toUpperCase();

	     		// Close the file reader buffer
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
