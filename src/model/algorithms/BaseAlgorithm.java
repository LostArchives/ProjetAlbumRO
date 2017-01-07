package model.algorithms;

import java.util.ArrayList;
import java.util.Random;

import model.AlbumCriteria;
import model.AlbumPhoto;

public abstract class BaseAlgorithm {

	protected AlbumPhoto _albumPhoto; // AlbumPhoto used by the algorithm
	protected AlbumCriteria _albumCriteria; // Evaluation criteria for the album photo
	protected int _bestIteration; //Best ieration to retrieve the bestSolution in the arrayList
	protected ArrayList<String> _solutions; //All best solutions from every run launched by the singleton  AlgorithManager
	
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
	 * Every algorithm can be launched
	 * so this method allows all of them to be launched
	 * As an abstract method every children classes has this method but with its own code
	 * @param permuter Random object external to avoid seed problems
	 */
	public abstract void Launch(Random permuter);
	
	
	/**
	 * Method to affect values into the double array of a criteria 
	 * ( ex : computePhotoHash loads values into photoDistHashes etc...)
	 * (The double array of a criteria can be accessed directly with the getCriteriaArray method just below
	 */
	public void computeCriteria() {
		
		switch(_albumCriteria) {
		
		case HASH:
			_albumPhoto.computePhotoHashes();
			break;
			
		case PHASH:
			_albumPhoto.computePhotoPhashes();
			break;
			
		case DHASH:
			_albumPhoto.computePhotoDhashes();
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
	
	/**
	 * Method to get directly the double array containing the values related to the computation of the criteria
	 * @return the double array containing the values related to the computation of the album criteria attribute
	 */
	public double[][] getCriteriaArray() {
		
		double[][] arr = null;
		
		switch(_albumCriteria) {
		
		case HASH:
			return _albumPhoto.getPhotosDistHash();
			
			
		case PHASH:
			return _albumPhoto.getPhotosDistPhash();
			
		case DHASH:
			return _albumPhoto.getPhotosDistDhash();
			
		case COLORS:
			return _albumPhoto.getPhotosDistColors();
			
		case GREY_AVG:
			return _albumPhoto.getPhotosDistGreyAVG();
			
		case COMMON_TAGS:
			return _albumPhoto.getPhotosDistComTags();
			
		case UNCOMMON_TAGS:
			return _albumPhoto.getPhotosDistUncomTags();
			
		case NB_UNCOMMON_TAGS:
			return _albumPhoto.getPhotosDistUncomNbTags();
			
		case NONE:
			break;
		
		}
		
		return arr;
	}
	
	
	/**
	 * Method to generate a basic solution (0 to size)
	 * @param size of the solution to generate
	 * @return The solution generated
	 */
	public int[] generateBasicSolution(int size) {
    	int[] basic = new int[size];
    	for(int i = 0; i < size; i++) {
    	   basic[i] = i;
        }
    	return basic;
    }
	
	/**
	 *  Method to generate a random solution ( values from 0 to size randomly placed)
	 * @param size of the solution to generate
	 * @return The solution generated
	 */
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
	
	/**
	 * Convert method of an int[] array into an arrayList<String> (could be useful :) )
	 * @param solution int array to convert
	 * @return converted array into an arrayList
	 */
	protected ArrayList<String> convertToArrayList(int[] solution) {
		
		ArrayList<String> converted = new ArrayList<String>();
		for (int i = 0 ; i< solution.length;i++) {
			converted.add(solution[i]+"");
		}
		return converted;
		
	}
	
	/**
	 * Convert method of an arrayList<String> into an int[] array (could be useful :) )
	 * @param solution ArrayList to convert
	 * @return converted arrayList into an int array
	 */
	protected int[] convertToIntArray(ArrayList<String> solution) {
		
		int[] converted = new int[solution.size()];
		for (int i = 0 ; i< solution.size();i++) {
			converted[i] = Integer.parseInt(solution.get(i));
		}
		
		return converted;
		
	}
	
	
	
	/**
	 * Method to clone a solution to avoid references problems when affecting arrays variables to another
	 * @return a clone of the value passed as a parameter
	 */
	protected ArrayList<String> cloneSolution() {
		
		ArrayList<String> clone = new ArrayList<String>();
		for (int i =0 ; i < _solutions.size() ; i++) {
			clone.add(i,_solutions.get(i));
		}
		return clone;
	}
    
	/**
	 * Copy the array 'from' content to the array called 'to'
	 * @param from
	 * @param to
	 */
    protected void copyToArray(int[] from,int[] to) {
    	for (int i = 0 ; i< from.length;i++) {
    		to[i] = from[i];
    	}
    }
    
    /**
     * Create a string to display properly the solution order evaluated  in the console
     * @param arr Array to display
     * @return
     */
    protected String display(int[] arr) {
    	String s = "";
    	for (int i = 0 ;i< arr.length ; i++) {
    		s+=arr[i]+" ";
    		
    	}
    	s+="\n--------";
    	return s;
    }
    
    
	
}
