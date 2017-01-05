package model;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Classe permettant d'écrire un fichier 
 * @author Valentin Mullet
 *
 */
public class FileWriter {

		private String fileName; //Chemin du fichier généré
		
		public FileWriter(String pfileName) {
			fileName = pfileName;
		}
		
		/**
		 * Méthode permettant de créer et d'écrire des données dans le fichier 
		 * @param data Données à écrire dans le fichier
		 */
		public void Write(String data) {
			
			try {
				PrintWriter pw = new PrintWriter(new File(fileName));
				pw.write(data);
		        pw.close();
			} 
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	           
		}
	
}