/**
 *
 * Minimal examples for the project in JAVA
 *
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * @author verel
 * @date 2015/11/07
 */
public class Main {
    // Distance between photos
    public static double [][] photoDist;
    public static ArrayList<String> solutions;
    // Inverse of the distance between positions in the album
    public static double [][] albumInvDist;

    /**
     *
     * Example of json file parsing
     *
     * see: https://code.google.com/p/json-simple/
     * for more example to decode json under java
     *
     */
    public static void readPhotoExample(String fileName) {
	try {
	    FileReader reader = new FileReader(fileName);

	    JSONParser parser = new JSONParser();
	    
	    // parser the json file
	    Object obj = parser.parse(reader);
	    //System.out.println(obj);

	    // extract the array of image information
	    JSONArray array = (JSONArray) obj;
	    System.out.println("The first element:\n" + array.get(0));

	    JSONObject obj2 = (JSONObject) array.get(0);
	    System.out.println("the id of the first element is: " + obj2.get("id"));    

	    JSONArray arraytag = (JSONArray) ((JSONObject)obj2.get("tags")).get("classes");
	    System.out.println("Tag list of the first element:");
	    for(int i = 0; i < arraytag.size(); i++)
		System.out.print(" " + arraytag.get(i));
	    System.out.println();

	} catch(ParseException pe) {	    
	    System.out.println("position: " + pe.getPosition());
	    System.out.println(pe);
	} catch (FileNotFoundException ex) {
	    ex.printStackTrace();
	} catch(IOException ex) {
	    ex.printStackTrace();
	}
    }

    /**
     *  Compute the matrice of distance between solutions 
     *                  and of inverse distance between positions 
     */
    public static void computeDistances(String photoFileName, String albumFileName) {
	computePhotoDistances(photoFileName);
	computeAlbumDistances(albumFileName);
    }

    public static void computeAlbumDistances(String fileName) {
	try {
	    FileReader reader = new FileReader(fileName);

	    JSONParser parser = new JSONParser();
	    Object obj = parser.parse(reader);

	    JSONObject album = (JSONObject) obj;

	    // number of pages
	    long nPage = (long) album.get("page");

	    // number of photo in each page
	    JSONArray pageSize = (JSONArray) album.get("pagesize");

	    // number on the first page
	    int size = (int) (long) pageSize.get(0);
	    // total number of photo in the album
	    int nbPhoto = 0;
	    for(int i = 0; i < pageSize.size(); i++) 
		nbPhoto += (int) (long) pageSize.get(i);

	    albumInvDist = new double[nbPhoto][nbPhoto];

	    // compute the distance
	    for(int i = 0; i < nbPhoto; i++) 
		for(int j = 0; j < nbPhoto; j++) 
		    albumInvDist[i][j] = inverseDistance(size, i, j);
	    
	    /*
	    for(int i = 0; i < albumDist.length; i++) {
		for(int j = 0; j < albumDist.length; j++) {
		    System.out.print(" " + albumDist[i][j]);
		}
		System.out.println();
	    }
	    */

	} catch(ParseException pe) {	    
	    System.out.println("position: " + pe.getPosition());
	    System.out.println(pe);
	} catch (FileNotFoundException ex) {
	    ex.printStackTrace();
	} catch(IOException ex) {
	    ex.printStackTrace();
	}
    }

    public static double inverseDistance(int size, int i, int j) {
	// number of pages
	int pagei = i / size;
	int pagej = j / size;

	if (pagei != pagej)
	    // not on the same page: distance is infinite. Another choice is possible of course!
	    return 0;
	else {
	    // positions in the page
	    int posi = i % size;
	    int posj = j % size;

	    // coordinate on the page
	    int xi = posi % 2;
	    int yi = posi / 2;
	    int xj = posj % 2;
	    int yj = posj / 2;

	    // Manhatthan distance
	    return ((double) 1) / (double) (Math.abs(xi - xj) + Math.abs(yi - yj));
	}
    }

    public static void computePhotoDistances(String fileName) {
	try {
	    FileReader reader = new FileReader(fileName);

	    JSONParser parser = new JSONParser();

	    Object obj = parser.parse(reader);

	    JSONArray array = (JSONArray) obj;

	    photoDist = new double[array.size()][array.size()];

	    // distance based on the distance between average hash
	    for(int i = 0; i < array.size(); i++) {
		JSONObject image = (JSONObject) array.get(i);
		JSONArray d = (JSONArray) image.get("ahashdist");		
		for(int j = 0; j < d.size(); j++) {
		    photoDist[i][j] = (double) d.get(j);
		}
	    }

	    /*
	    for(int i = 0; i < photoDist.length; i++) {
		for(int j = 0; j < photoDist.length; j++) {
		    System.out.print(" " + photoDist[i][j]);
		}
		System.out.println();
	    }
	    */


	} catch(ParseException pe) {	    
	    System.out.println("position: " + pe.getPosition());
	    System.out.println(pe);
	} catch (FileNotFoundException ex) {
	    ex.printStackTrace();
	} catch(IOException ex) {
	    ex.printStackTrace();
	}
    }

    /**
     * Un exemple de fonction objectif (à minimiser):
     *   distance entre les photos pondérées par l'inverse des distances spatiales sur l'album
     *   Modélisaiton comme un problème d'assignement quadratique (QAP)
     *
     *   Dans cette fonction objectif, 
     *      pas de prise en compte d'un effet de page (harmonie/cohérence de la page)
     *      par le choix de distance, pas d'intéraction entre les photos sur des différentes pages
     */
    static double eval(int [] solution) {
	double sum = 0;

	for(int i = 0; i < albumInvDist.length; i++) {
	    for(int j = i + 1; j < albumInvDist.length; j++) {
		sum += photoDist[ solution[i] ][ solution[j] ] * albumInvDist[i][j] ;
	    }
	}

	return sum;
    }

    /**
     * @param args
     */
    public static void main(String[] args)  { 
	// Path to the photo information file in json format
	String photoFileName = "data/info-photo.json";
	// Path to the album information file in json format
	String albumFileName = "data/info-album.json";

	// uncomment to test it
	// readPhotoExample(photoFileName);

	computeDistances(photoFileName, albumFileName);

	// one basic solution : order of the index

	int numberOfPhoto = 55;
	int [] solution = generateBasicSolution(numberOfPhoto);

	// compute the fitness
	//System.out.println(eval(solution));
	double smallest = 100;
	Random r = new Random();
	int bestIteration = 0;
	solutions = new ArrayList<String>();
	for (int i = 0 ; i<100000;i++) 
	{
		double min = HillClimberFirst(r);
		if (min<smallest) {
			smallest = min;
			bestIteration = i;
		}
			
	}
	System.out.println("La plus petite valeur est " + smallest + " obtenue avec \n"+solutions.get(bestIteration));
	
	
}
    
    
    public static double HillClimberFirst(Random permuter) {
    	double theMinEvaluation = 0;
    	double actualEvaluation = 0;
    	int[] solution = generateBasicSolution(55);
    	int[] permute = new int[solution.length];
    	boolean stop = false;
    	int cnt = 0;
    	
    	theMinEvaluation = eval(solution);
    	do {
    		
    		for (int i = 0 ; i< 55 ;i++) {
    			permute = randomPermute(permuter,solution);
    			actualEvaluation = eval(permute);
    			
    			if (actualEvaluation < theMinEvaluation) {
    				cnt+=i;
    				break;
    			}
    				
    		}
    		
    		if (actualEvaluation < theMinEvaluation) {
    			theMinEvaluation = actualEvaluation;
    			copyToArray(permute,solution);
    		}
    		else
    		{
    			stop = true;
    		}
    		
    	} while(!stop);
    	
    	System.out.println("Avec l'ordre "+display(permute)+" on obtient "+theMinEvaluation + " en "+ (cnt+1)+ " it�rations");
    	solutions.add(display(permute));
    	return theMinEvaluation;
    }
    
    private static int[] randomPermute(Random r,int[] basicSolution) {
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
    
    private static String display(int[] arr) {
    	String s = "";
    	for (int i = 0 ;i< arr.length ; i++) {
    		s+=arr[i]+" ";
    		
    	}
    	s+="\n--------";
    	return s;
    }
    
    private static int[] generateBasicSolution(int size) {
    	int[] basic = new int[size];
    	for(int i = 0; i < size; i++) {
    	   basic[i] = i;
        }
    	return basic;
    }
    
    private static void copyToArray(int[] from,int[] to) {
    	for (int i = 0 ; i< from.length;i++) {
    		to[i] = from[i];
    	}
    }
    
    


}
