package model;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Classe permettant d'�crire un fichier 
 * @author Valentin Mullet
 *
 */
public class FileWriter {

		private String fileName; //Chemin du fichier g�n�r�
		
		public FileWriter(String pfileName) {
			fileName = pfileName;
		}
		
		/**
		 * M�thode permettant de cr�er et d'�crire des donn�es dans le fichier 
		 * @param data Donn�es � �crire dans le fichier
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