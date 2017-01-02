package model;

import java.util.Random;

import model.algorithms.HillClimberFirst;


public class AlgorithmManager {

	
	private static AlgorithmManager _instance = null;
	
	public static AlgorithmManager Instance() {
		
		if (_instance==null) {
			_instance = new AlgorithmManager();
		}
		
		return _instance;
		
	}
	
	public void LaunchHillClimber(HillClimberFirst hcf) {
		
		Random permuter = new Random(); // Declared externally To avoid any problems with the seed
		
		
		hcf.getAlbumPhoto().computeDistances();
		
		for (int i = 0 ; i<hcf.getNbIterations();i++) 
		{
			hcf.Launch(permuter);
			
			if (hcf.getActualMinEvaluation()<hcf.getSmallestEvaluation()) {
				
				hcf.setSmallestEvaluation(hcf.getActualMinEvaluation());
				
				hcf.setBestIterations(i);
				
			}
				
		}
		
		System.out.println("La plus petite valeur est " + hcf.getSmallestEvaluation() + " obtenue avec \n"+ hcf.getSolutions().get(hcf.getBestIterations()));
		
		
		
	}
	
	
	
	
}
