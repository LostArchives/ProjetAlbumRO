package model.algorithms;

import java.util.ArrayList;
import java.util.Random;

import model.AlbumPhoto;

public abstract class BaseAlgorithm {

	protected AlbumPhoto _albumPhoto;
	protected int _nbIterations;
	protected int _bestIteration;
	protected ArrayList<String> _solutions;
	
	public BaseAlgorithm(AlbumPhoto albumPhoto,int nbIterations) {
		
		_albumPhoto = albumPhoto;
		_nbIterations = nbIterations;
		_solutions = new ArrayList<String>();
		
	}
	
	
	public AlbumPhoto getAlbumPhoto() {
		return _albumPhoto;
	}

	public void setAlbumPhoto(AlbumPhoto _albumPhoto) {
		this._albumPhoto = _albumPhoto;
	}

	public int getNbIterations() {
		return _nbIterations;
	}

	public void setNbIterations(int _nbIterations) {
		this._nbIterations = _nbIterations;
	}
	
	public int getBestIterations() {
		return _bestIteration;
	}

	public void setBestIterations(int _bestIteration) {
		this._bestIteration = _bestIteration;
	}

	public ArrayList<String> getSolutions() {
		return _solutions;
	}

	public void setSolutions(ArrayList<String> _solutions) {
		this._solutions = _solutions;
	}

	
	
	public abstract void Launch(Random permuter);
	
	
	protected int[] randomPermute(Random r,int[] basicSolution) {
		
	    	int[] permute = new int[basicSolution.length];
	    	
	    	for (int i = 0 ; i< permute.length;i++) {
	    		permute[i] = basicSolution[i];
	    	}
	    	
	    	int  firstRandom = r.nextInt(permute.length);
	    	
	    	int secondRandom = r.nextInt(permute.length);
	    	while(firstRandom == secondRandom)
	    		secondRandom = r.nextInt(permute.length);
	    	
	    	
	    	int temp = permute[firstRandom];
	    	permute[firstRandom] = permute[secondRandom];
	    	permute[secondRandom] = temp;
	    	
	    	return permute;
	    	
	}
	
	
	public int[] generateBasicSolution(int size) {
    	int[] basic = new int[size];
    	for(int i = 0; i < size; i++) {
    	   basic[i] = i;
        }
    	return basic;
    }
    
    protected void copyToArray(int[] from,int[] to) {
    	for (int i = 0 ; i< from.length;i++) {
    		to[i] = from[i];
    	}
    }
    
    protected String display(int[] arr) {
    	String s = "";
    	for (int i = 0 ;i< arr.length ; i++) {
    		s+=arr[i]+" ";
    		
    	}
    	s+="\n--------";
    	return s;
    }
    
    
	
}
