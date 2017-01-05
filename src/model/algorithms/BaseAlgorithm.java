package model.algorithms;

import java.util.ArrayList;
import java.util.Random;

import model.AlbumCriteria;
import model.AlbumPhoto;

public abstract class BaseAlgorithm {

	protected AlbumPhoto _albumPhoto;
	protected AlbumCriteria _albumCriteria; // Evaluation criteria for the album photo
	protected int _bestIteration;
	protected ArrayList<String> _solutions;
	
	public BaseAlgorithm(AlbumPhoto albumPhoto,AlbumCriteria criteria) {
		
		_albumPhoto = albumPhoto;
		_albumCriteria = criteria;
		_solutions = new ArrayList<String>();
		
	}
	
	public BaseAlgorithm(AlbumPhoto albumPhoto) {
		
		_albumPhoto = albumPhoto;
		_solutions = new ArrayList<String>();
		
	}
	
	
	public AlbumPhoto getAlbumPhoto() {
		return _albumPhoto;
	}

	public void setAlbumPhoto(AlbumPhoto _albumPhoto) {
		this._albumPhoto = _albumPhoto;
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

	
	public AlbumCriteria getAlbumCriteria() {
		return _albumCriteria;
	}


	public void setAlbumCriteria(AlbumCriteria _albumCriteria) {
		this._albumCriteria = _albumCriteria;
	}


	/**
	 * Every algorithm can be launched so this method allows all of them to be launched but not necessary the same way (different implementations)
	 * @param permuter
	 */
	public abstract void Launch(Random permuter);
	
	
	
	public void computeCriteria() {
		
		switch(_albumCriteria) {
		
		case HASH:
			_albumPhoto.computePhotoHashes();
			break;
			
		case COLORS:
			_albumPhoto.computePhotosColors();
			break;
			
		case GREY_AVG:
			_albumPhoto.computePhotosGreyAVG();
			break;
			
		case COMMON_TAGS:
			_albumPhoto.computePhotosTags();
			break;
			
		case UNCOMMON_TAGS:
			_albumPhoto.computePhotosTags();
			break;
			
		case NB_UNCOMMON_TAGS:
			_albumPhoto.computePhotosTags();
			break;
			
		case NONE:
			break;
		
		}
		_albumPhoto.computeAlbumDistances();
		
	}
	
	
	public int[] generateBasicSolution(int size) {
    	int[] basic = new int[size];
    	for(int i = 0; i < size; i++) {
    	   basic[i] = i;
        }
    	return basic;
    }
	
	public int[] generateRandomSolution(int size) {
		
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		for (int i = 0 ; i< size;i++) {
			indexList.add(i);
		}
		Random r = new Random();
		int random[] = new int[size];
		for (int j = 0 ; j< size ; j++) {
			int randomIndex = r.nextInt(indexList.size());
			random[j] = indexList.get(randomIndex);
			indexList.remove(randomIndex);
		}
		return random;
		
	}
	
	
	/**
	 * Permute only 1 time (1 mutation)
	 * @param r The random object to get random position to permute (parameter to avoid seed problem)
	 * @param basicSolution The basic solution to permute
	 * @return The permuted solution in another variable (no direct alteration of the original solution)
	 */
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
	
	/**
	 * Permute a specific number of times (specific number of mutations)
	 * @param r The random object to get random position to permute (parameter to avoid seed problem)
	 * @param basicSolution The basic solution to permute
	 * @param nbMutation The number of mutations to the basic solution
	 * @return The permuted solution in another variable (no direct alteration of the original solution)
	 */
	protected int[] randomPermute(Random r,int[] basicSolution,int nbMutation) {
		
    	int[] permute = new int[basicSolution.length];
    	
    	for (int i = 0 ; i< permute.length;i++) {
    		permute[i] = basicSolution[i];
    	}
    	
    	for (int j = 0 ; j< nbMutation ; j++) {
    		
    	int  firstRandom = r.nextInt(permute.length);
    	
    	int secondRandom = r.nextInt(permute.length);
    	while(firstRandom == secondRandom)
    		secondRandom = r.nextInt(permute.length);
    	
    	
    	int temp = permute[firstRandom];
    	permute[firstRandom] = permute[secondRandom];
    	permute[secondRandom] = temp;
    	
    	}
    	 
    	return permute;
    	
}
	
	protected ArrayList<String> convertToArrayList(int[] solution) {
		
		ArrayList<String> converted = new ArrayList<String>();
		for (int i = 0 ; i< solution.length;i++) {
			converted.add(solution[i]+"");
		}
		return converted;
		
	}
	
	protected int[] convertToIntArray(ArrayList<String> solution) {
		
		int[] converted = new int[solution.size()];
		for (int i = 0 ; i< solution.size();i++) {
			converted[i] = Integer.parseInt(solution.get(i));
		}
		
		return converted;
		
	}
	
	
	

	
	protected ArrayList<String> cloneSolution() {
		
		ArrayList<String> clone = new ArrayList<String>();
		for (int i =0 ; i < _solutions.size() ; i++) {
			clone.add(i,_solutions.get(i));
		}
		return clone;
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
