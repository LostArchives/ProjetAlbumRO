package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import model.algorithms.HillClimberFirst;
import model.algorithms.IterativeLocalSearch;

/**
 * A singleton class to manage all baseAlgorithm children classes
 * @author Valentin
 *
 */
public class AlgorithmManager {

	
	private static AlgorithmManager _instance = null; // Singleton pattern
	
	/**
	 * Method to access the unique instance like in any singleton
	 * @return
	 */
	public static AlgorithmManager Instance() {
		
		if (_instance==null) {
			_instance = new AlgorithmManager();
		}
		
		return _instance;
		
	}
	
	/**
	 * Method to launch HillClimber x times
	 * @param hcf HillClimber object algorithm
	 * @param nbIteration nbRun for this algorithm
	 */
	public void LaunchHillClimber(HillClimberFirst hcf,int nbIteration) {
		
		Random permuter = new Random(); // Declared externally To avoid any problems with the seed
		
		
		hcf.computeCriteria(); // Compute the criteria (HASH or PHASH depending of the criteria attribute of the object)
		
		for (int i = 0 ; i<nbIteration;i++) 
		{
			hcf.Launch(permuter);
			
			if (hcf.getActualMinEvaluation()<hcf.getBestEvaluation()) {
				
				hcf.setBestEvaluation(hcf.getActualMinEvaluation());
				
				hcf.setBestIterations(i);
				
			}
				
		}
		
		/////////////////////////////////////////// CONSOLE OUTPUT //////////////////////////////////////////
		String solution = hcf.getSolutions().get(hcf.getBestIterations()).split("\n")[0];
		String solutionPath = "results/HCF_"+hcf.getAlbumCriteria().toString()+"_"+nbIteration+".sol";
		System.out.println("HCF : La plus petite valeur est " + hcf.getBestEvaluation() + " obtenue avec \n"+ solution);
		
		
		/////////////////////////////////////////// WRITE SOL FILE //////////////////////////////////////////
		FileWriter f = new FileWriter("results/HCF_"+hcf.getAlbumCriteria().toString()+"_"+nbIteration+".sol");
		f.Write(solution);
		
		/////////////////////////////////////////// BUILD THE ALBUM (execute python script) //////////////////////////////////////////
		BuildAlbum(solutionPath);
		
		
	}
	
	/**
	 * Method to launch ILS x times
	 * @param ils ILS Algorithm object
	 * @param nbIteration nbRun for this algorithm
	 */
	public void LaunchIterativeLocalSearch(IterativeLocalSearch ils,int nbIteration) {
		
		
		Random permuter = new Random(); // Declared externally To avoid any problems with the seed
		
		ils.getHcfIterative().computeCriteria(); // Compute the criteria (HASH or PHASH depending of the criteria attribute of the object)
		
		for (int i = 0 ; i < nbIteration ; i++) {
			
			ils.Launch(permuter);
			
				if (ils.getActualMinEvaluation()<ils.getBestEvaluation()) {
				
				ils.setBestEvaluation(ils.getActualMinEvaluation());
				
				ils.setBestIterations(i);
				
			}
			
		}
		
		/////////////////////////////////////////// CONSOLE OUTPUT //////////////////////////////////////////
		String solution = ils.getSolutions().get(ils.getBestIterations()).split("\n")[0];
		
		String solutionPath = "results/ILS_"+ils.getHcfIterative().getAlbumCriteria().toString()+"_"+ils.getNbMutation()+"_"+nbIteration+".sol";
		
		System.out.println("Ils : La plus petite valeur est " + ils.getBestEvaluation()  + " obtenue avec \n"+ solution );
		
		/////////////////////////////////////////// WRITE SOL FILE //////////////////////////////////////////
		FileWriter f = new FileWriter(solutionPath);
		f.Write(solution);
		
		/////////////////////////////////////////// BUILD THE ALBUM (execute python script) //////////////////////////////////////////
		BuildAlbum(solutionPath);
		
	}
	
	public void BuildAlbum(String solutionfile) {
		Runtime rt = Runtime.getRuntime();
		try {
			Process pr = rt.exec("python buildAlbum.py " +System.getProperty("user.dir")+"/"+ solutionfile);
			BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
			    System.out.println(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
}
