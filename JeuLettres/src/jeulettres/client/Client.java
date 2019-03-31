package jeulettres.client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.net.InetAddress;
import java.net.Socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;


public class Client {
	
	//Testing commit from another computer
		
	public static void main(String[] args) {

		Socket socket;
		BufferedReader in;
		PrintWriter out;
		boolean enJeu = true;
		int tailleMot = 5;
		
		try {
			//Demande d'ouverture d'une connexion sur le serveur local
			socket = new Socket(InetAddress.getLocalHost(),60000);  

			//Message d'accueil 
			in = new BufferedReader (new InputStreamReader (socket.getInputStream()));
			String message_distant = in.readLine();
			
			if(message_distant.equals("full")) {
				System.out.println("Serveur complet.");
				return;
			}
			
			System.out.println("serveur :"+ message_distant);        

			//Envoi du pseudo
			out = new PrintWriter(socket.getOutputStream());
			Scanner scanner = new Scanner(System.in);
			String pseudo = scanner.nextLine();
			out.println(pseudo);
			out.flush();

			//Message de bienvenue
			in = new BufferedReader (new InputStreamReader (socket.getInputStream()));
			message_distant = in.readLine();
			System.out.println("Serveur :"+ message_distant);
			
			// Envoi du mot de passe
			message_distant = in.readLine();
			System.out.println("serveur :"+ message_distant);
			String password = scanner.nextLine();
			out.println(password);
			out.flush();
			
			
			//Boucle de jeu
			while(enJeu) {
				message_distant = in.readLine();
				
				switch(message_distant) {
				case "init!":
					String taille_mot = in.readLine();
					//TODO Taille du mot variable
					break;
				case "answer?":
					String answer = "";
					while(answer.length() != tailleMot) {
						System.out.println("Entrez un mot de "+tailleMot+" lettres : ");
						answer = scanner.nextLine();
					}
					
					out.println(answer.toLowerCase());
					out.flush();
					break;
				case "result!":
					String resultat = in.readLine();
					String[] valeurs = resultat.split("\\|");
					System.out.println("Vous avez "+valeurs[0]+" lettre(s) présente(s) dont "+valeurs[1]+" bien placée(s)");
					break;
				case "win!":
					System.out.println("Bravo !");
					String score = in.readLine();
					System.out.println("Nouveau score : " + score);
					break;
				case "end!":
					enJeu = false;
					break;
				default:
					System.out.println("Switch failure");
					break;
				}
				
			}

			
			scanner.close();
			
			//fermeture de la connexion
			//socket.close();

		}catch (UnknownHostException e) {      
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}