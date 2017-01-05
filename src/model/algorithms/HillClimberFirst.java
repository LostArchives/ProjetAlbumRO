package model.algorithms;

import java.util.ArrayList;
import java.util.Random;

import model.AlbumCriteria;
import model.AlbumPhoto;

public class HillClimberFirst extends BaseAlgorithm {
	
	private double actualMinEvaluation;
	private double bestEvaluation;
	private int[] tempSolution;
	
	
	public HillClimberFirst(AlbumPhoto albumPhoto,AlbumCriteria criteria) {
		super(albumPhoto,criteria);
		bestEvaluation = 10000000; // There won't be a value higher than this
		_solutions = new ArrayList<String>();
		// TODO Auto-generated constructor stub
	}


	public void Launch(Random permuter) {
		
		actualMinEvaluation = 0;
		
    	double actualEvaluation = 0;
    	
    	int[] solution = generateRandomSolution(_albumPhoto.getNbPhotos());
    	
    	int[] permute = new int[solution.length];
    	
    	boolean stop = false;
    	int cnt = 0;
    	
    	actualMinEvaluation = _albumPhoto.eval(_albumCriteria,solution);
    	
    	do {
    		
    		for (int i = 0 ; i< _albumPhoto.getNbPhotos(); i++) {
    			permute = randomPermute(permuter,solution);
    			actualEvaluation = _albumPhoto.eval(_albumCriteria,permute);
    			
    			if (actualEvaluation < actualMinEvaluation) {
    				cnt+=i;
    				break;
    			}
    				
    		}
    		
    		if (actualEvaluation < actualMinEvaluation) {
    			actualMinEvaluation = actualEvaluation;
    			copyToArray(permute,solution);
    			tempSolution = solution;
    		}
    		else
    		{
    			stop = true;
    		}
    		
    	} while(!stop);
    	
    	
    	System.out.println("Avec l'ordre "+display(permute)+" on obtient "+ actualMinEvaluation + " en "+ (cnt+1)+ " itérations ");
    	_solutions.add(display(permute));
    	
		
	}
	
    

	public void Launch(Random permuter,int[] psolution) {
		
		actualMinEvaluation = 0;
		
		_solutions = new ArrayList<String>();
		
    	double actualEvaluation = 0;
    	
    	int[] solution = psolution.clone();
    	
    	int[] permute = new int[solution.length];
    	
    	boolean stop = false;
    	int cnt = 0;
    	
    	actualMinEvaluation = _albumPhoto.eval(_albumCriteria,solution);
    	
    	do {
    		
    		for (int i = 0 ; i< _albumPhoto.getNbPhotos(); i++) {
    			permute = randomPermute(permuter,solution);
    			actualEvaluation = _albumPhoto.eval(_albumCriteria,permute);
    			
    			if (actualEvaluation < actualMinEvaluation) {
    				cnt+=i;
    				break;
    			}
    				
    		}
    		
    		if (actualEvaluation < actualMinEvaluation) {
    			actualMinEvaluation = actualEvaluation;
    			copyToArray(permute,solution);
    			tempSolution = solution;
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
	
	public double getBestEvaluation() {
		return bestEvaluation;
	}


	public void setBestEvaluation(double smallestEvaluation) {
		this.bestEvaluation = smallestEvaluation;
	}


	 public int[] gettempSolution() {
			return tempSolution;
		}


		public void settempSolution(int[] bestSolution) {
			this.tempSolution = bestSolution;
		}


}
