package model.algorithms;

import java.util.ArrayList;
import java.util.Random;

import model.AlbumCriteria;
import model.AlbumPhoto;

public class IterativeLocalSearch extends BaseAlgorithm {

	private int _nbMutation;
	private int _nbIteration;
	private HillClimberFirst _hcfIterative;
	
	private double _actualMinEvaluation;
	private int[] _actualBestSolution;
	
	private ArrayList<String> _bestSolution;
	private double _bestEvaluation;
	
	public IterativeLocalSearch(AlbumPhoto albumPhoto,HillClimberFirst hcf,int nbMutation,int nbIteration) {
		super(albumPhoto);
		_hcfIterative = hcf;
		_nbMutation = nbMutation;
		_actualMinEvaluation = 10000000;
		_bestEvaluation = 10000000;
		// TODO Auto-generated constructor stub
	}

	
	
	public void Launch(Random permuter) {
		
		int nbPhotos = _hcfIterative.getAlbumPhoto().getNbPhotos();
		
		_hcfIterative.Launch(permuter,_hcfIterative.generateRandomSolution(nbPhotos));
		
		int[] solution = _hcfIterative.gettempSolution();
		
		_bestEvaluation = _hcfIterative.getBestEvaluation();
		
		int cntIteration = 0;
		
		do {
			
			solution = _hcfIterative.randomPermute(permuter,solution,_nbMutation);
			
			_hcfIterative.Launch(permuter,solution);
			
			double actualEvaluation = _hcfIterative.getActualMinEvaluation();
			
			if (actualEvaluation < _actualMinEvaluation) {
				_actualMinEvaluation = actualEvaluation;
				_actualBestSolution = solution.clone();
			}
			
			
			cntIteration++;
			
			
		} while (cntIteration < _nbIteration);
		
		_solutions.add(display(_actualBestSolution));
		System.out.println("Avec l'ordre "+display(solution)+" on obtient "+ _actualMinEvaluation + " en "+ (_nbIteration+1)+ " itérations");
		
	}



	public double getActualMinEvaluation() {
		return _actualMinEvaluation;
	}



	public void setActualMinEvaluation(double _actualMinEvaluation) {
		this._actualMinEvaluation = _actualMinEvaluation;
	}



	public int[] getActualBestSolution() {
		return _actualBestSolution;
	}



	public void setActualBestSolution(int[] _actualBestSolution) {
		this._actualBestSolution = _actualBestSolution;
	}



	public int getNbMutation() {
		return _nbMutation;
	}



	public void setNbMutation(int nbMutation) {
		this._nbMutation = nbMutation;
	}



	public int getNbIteration() {
		return _nbIteration;
	}



	public void setNbIteration(int nbIteration) {
		this._nbIteration = nbIteration;
	}



	public HillClimberFirst getHcfIterative() {
		return _hcfIterative;
	}



	public void setHcfIterative(HillClimberFirst hcfIterative) {
		this._hcfIterative = hcfIterative;
	}



	public ArrayList<String> getBestSolution() {
		return _bestSolution;
	}



	public void setBestSolution(ArrayList<String> bestSolution) {
		this._bestSolution = bestSolution;
	}



	public double getBestEvaluation() {
		return _bestEvaluation;
	}



	public void setBestEvaluation(double bestEvaluation) {
		this._bestEvaluation = bestEvaluation;
	}
	
}
