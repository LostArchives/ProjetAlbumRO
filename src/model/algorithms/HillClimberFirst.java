package model.algorithms;

import java.util.Random;

import model.AlbumPhoto;

public class HillClimberFirst extends BaseAlgorithm {
	
	private double actualMinEvaluation;
	private double smallestEvaluation;
	
	
	public HillClimberFirst(AlbumPhoto albumPhoto, int nbIterations) {
		super(albumPhoto, nbIterations);
		smallestEvaluation = 100; // There won't be a value higher than this
		// TODO Auto-generated constructor stub
	}


	public void Launch(Random permuter) {
		
		actualMinEvaluation = 0;
		
    	double actualEvaluation = 0;
    	
    	int[] solution = generateBasicSolution(_albumPhoto.getNbPhotos());
    	
    	int[] permute = new int[solution.length];
    	
    	boolean stop = false;
    	int cnt = 0;
    	
    	actualMinEvaluation = _albumPhoto.eval(solution);
    	
    	do {
    		
    		for (int i = 0 ; i< _albumPhoto.getNbPhotos(); i++) {
    			permute = randomPermute(permuter,solution);
    			actualEvaluation = _albumPhoto.eval(permute);
    			
    			if (actualEvaluation < actualMinEvaluation) {
    				cnt+=i;
    				break;
    			}
    				
    		}
    		
    		if (actualEvaluation < actualMinEvaluation) {
    			actualMinEvaluation = actualEvaluation;
    			copyToArray(permute,solution);
    		}
    		else
    		{
    			stop = true;
    		}
    		
    	} while(!stop);
    	
    	System.out.println("Avec l'ordre "+display(permute)+" on obtient "+ actualMinEvaluation + " en "+ (cnt+1)+ " itérations");
    	_solutions.add(display(permute));
    	
		
		
	}



	public double getActualMinEvaluation() {
		return actualMinEvaluation;
	}



	public void setActualMinEvaluation(double minEvaluation) {
		this.actualMinEvaluation = minEvaluation;
	}
	
	public double getSmallestEvaluation() {
		return smallestEvaluation;
	}


	public void setSmallestEvaluation(double smallestEvaluation) {
		this.smallestEvaluation = smallestEvaluation;
	}



}
