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
	
	//MAIN FUNCTION
	public static void main(String[] args) {
		// Variable initialisation
		Socket socket;
		BufferedReader in;
		PrintWriter out;
		boolean enJeu = true;
		int tailleMot = 5;
		
		try {
			// Openning a connexion on the local server on a specific port
			socket = new Socket(InetAddress.getLocalHost(),60000);  

			//Welcoming message
			in = new BufferedReader (new InputStreamReader (socket.getInputStream()));
			String message_distant = in.readLine();
			
			// Warning message when the server can't manage more gamers
			if(message_distant.equals("full")) {
				System.out.println("Serveur complet.");
				return;
			}
			// Returning server message
			System.out.println("serveur :"+ message_distant);        

			//Get and send the gamer's pseudo
			out = new PrintWriter(socket.getOutputStream());
			Scanner scanner = new Scanner(System.in);
			String pseudo = scanner.nextLine();
			out.println(pseudo);
			out.flush();

			//Welcoming message
			in = new BufferedReader (new InputStreamReader (socket.getInputStream()));
			message_distant = in.readLine();
			System.out.println("Serveur :"+ message_distant);
			
			//Get and send of the password
			message_distant = in.readLine();
			System.out.println("serveur :"+ message_distant);
			String password = scanner.nextLine();
			out.println(password);
			out.flush();
			
			
			//Game loop management
			while(enJeu) {
				// Get the state of the game
				message_distant = in.readLine();
				
				switch(message_distant) {
				// Initialisation of the game
				case "init!":
					String taille_mot = in.readLine();
					//TODO Taille du mot variable
					break;
				// Ask the try of the gamer
				case "answer?":
					String answer = "";
					while(answer.length() != tailleMot) {
						System.out.println("Entrez un mot de "+tailleMot+" lettres : ");
						answer = scanner.nextLine();
					}
					
					out.println(answer.toUpperCase());
					out.flush();
					break;
				// Show if the gamer try is good or not
				case "result!":
					String resultat = in.readLine();
					String[] valeurs = resultat.split("\\|");
					System.out.println("Vous avez "+valeurs[0]+" lettre(s) présente(s) dont "+valeurs[1]+" bien placée(s)");
					break;
				// If the gamer found out the world
				case "win!":
					System.out.println("Bravo !");
					String score = in.readLine();
					System.out.println("Nouveau score : " + score);
					break;
				// End of the game
				case "end!":
					enJeu = false;
					break;
				//default treatment for undefined case
				default:
					System.out.println("Switch failure");
					break;
				}
			}
			// Close the listening of the scanner
			scanner.close();
			
			//Close of the connexion
			socket.close();

		}catch (UnknownHostException e) {      
			// Host errors catchs
			e.printStackTrace();
		}catch (IOException e) {
			// System error catchs
			e.printStackTrace();
		}
	}
}
