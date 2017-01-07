package model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Class corresponding to an albumPhoto with its attributes like json data file and array to store specific data values
 * @author Valentin
 *
 */
public class AlbumPhoto {

	// Path of the Json file storing album informations
	private String _albumFileName; 
	
	// Path of the Json file storing photos informations
	private String _photoFileName; 
	
	// Number of photos in the album
	private int _nbPhotos; 
	
	// Hash Distances between photos and album
    private double [][] photosDistHash;
    private double [][] albumInvDistHash;
    
    //PhashDist between photos
    private double [][] photosDistPhash;
    
    //DHashDist between photos
    private double [][] photosDistDhash;
    
    
    // Tags values distances between photos
    private double[][] photosDistComTags;
    private double[][] photosDistUncomTags;
    private double[][] photosDistUncomNbTags;
    
    //Colors distances between photos
    private double[][] photosDistColors;
    
    //Grey AVG distances between photos
    private double[][] photosDistGreyAVG;
    
    
	
	public AlbumPhoto(String albumfilename,String photofilename,int nbPhotos) {
		
		_albumFileName = albumfilename;
		_photoFileName = photofilename;
		_nbPhotos = nbPhotos;
		
	}
	
	public String getAlbumFileName() {
		return _albumFileName;
	}

	public void setAlbumFileName(String _albumFileName) {
		this._albumFileName = _albumFileName;
	}

	public String getPhotoFileName() {
		return _photoFileName;
	}

	public void setPhotoFileName(String _photoFileName) {
		this._photoFileName = _photoFileName;
	}

	public int getNbPhotos() {
		return _nbPhotos;
	}

	public void setNbPhotos(int _nbPhotos) {
		this._nbPhotos = _nbPhotos;
	}

	public String get_albumFileName() {
		return _albumFileName;
	}

	public String get_photoFileName() {
		return _photoFileName;
	}

	public int get_nbPhotos() {
		return _nbPhotos;
	}

	public double[][] getPhotosDistHash() {
		return photosDistHash;
	}

	public double[][] getAlbumInvDistHash() {
		return albumInvDistHash;
	}

	public double[][] getPhotosDistComTags() {
		return photosDistComTags;
	}

	public double[][] getPhotosDistUncomTags() {
		return photosDistUncomTags;
	}

	public double[][] getPhotosDistUncomNbTags() {
		return photosDistUncomNbTags;
	}

	public double[][] getPhotosDistColors() {
		return photosDistColors;
	}

	public double[][] getPhotosDistGreyAVG() {
		return photosDistGreyAVG;
	}

	public double[][] getPhotosDistPhash() {
		return photosDistPhash;
	}

	public double[][] getPhotosDistDhash() {
		return photosDistDhash;
	}

	/**
    *
    * Example of json file parsing
    *
    * see: https://code.google.com/p/json-simple/
    * for more example to decode json under java
    *
    */
   public void readPhotoExample() {
	   
	try {
		
	    FileReader reader = new FileReader(_albumFileName);

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
   public void computeDistances() {
	computePhotoHashes();
	computeAlbumDistances();
   }

   public void computeAlbumDistances() {
	   
	   
	try {
	    FileReader reader = new FileReader(_albumFileName);

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

	    albumInvDistHash = new double[nbPhoto][nbPhoto];

	    // compute the distance
	    for(int i = 0; i < nbPhoto; i++) 
		for(int j = 0; j < nbPhoto; j++) 
		    albumInvDistHash[i][j] = inverseDistance(size, i, j);
	    
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

   public double inverseDistance(int size, int i, int j) {
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

   public void computePhotoHashes() {
	   
	   
	try {
	    FileReader reader = new FileReader(_photoFileName);

	    JSONParser parser = new JSONParser();

	    Object obj = parser.parse(reader);

	    JSONArray array = (JSONArray) obj;

	    photosDistHash = new double[array.size()][array.size()];

	    // distance based on the distance between average hash
	    for(int i = 0; i < array.size(); i++) {
		JSONObject image = (JSONObject) array.get(i);
		JSONArray d = (JSONArray) image.get("ahashdist");		
		for(int j = 0; j < d.size(); j++) {
		    photosDistHash[i][j] = (double) d.get(j);
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
    * Method to affect all values related to DHash distances between photos
    */
   public void computePhotoDhashes() {
	   
	   
		try {
		    FileReader reader = new FileReader(_photoFileName);

		    JSONParser parser = new JSONParser();

		    Object obj = parser.parse(reader);

		    JSONArray array = (JSONArray) obj;

		    photosDistDhash = new double[array.size()][array.size()];

		    // distance based on the distance between average hash
		    for(int i = 0; i < array.size(); i++) {
			JSONObject image = (JSONObject) array.get(i);
			JSONArray d = (JSONArray) image.get("dhashdist");		
			for(int j = 0; j < d.size(); j++) {
			    photosDistDhash[i][j] = (double) d.get(j);
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
    * Method to affect all values related to PHash Distance between photos
    */
   public void computePhotoPhashes() {
	   
	   
		try {
		    FileReader reader = new FileReader(_photoFileName);

		    JSONParser parser = new JSONParser();

		    Object obj = parser.parse(reader);

		    JSONArray array = (JSONArray) obj;

		    photosDistPhash = new double[array.size()][array.size()];

		    // distance based on the distance between average hash
		    for(int i = 0; i < array.size(); i++) {
			JSONObject image = (JSONObject) array.get(i);
			JSONArray d = (JSONArray) image.get("phashdist");		
			for(int j = 0; j < d.size(); j++) {
			    photosDistPhash[i][j] = (double) d.get(j);
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
    * Method to affect all values related to tags in 2D double arrays -> 1 for every tags categories (common,uncommon....)
    */
   public void computePhotosTags() {
	   
	   try {
		    FileReader reader = new FileReader(_photoFileName);

		    JSONParser parser = new JSONParser();

		    Object obj = parser.parse(reader);

		    JSONArray array = (JSONArray) obj;
		    
		   JSONArray temp = (JSONArray)((JSONObject)((JSONObject)array.get(0)).get("tags")).get("classes");
		   
		   int nbTags = temp.size();
		   
		   double[][] tagsValues = new double[array.size()][nbTags];
		   String[][] tagsNames = new String[array.size()][nbTags];

		    photosDistComTags = new double[array.size()][array.size()];
		    photosDistUncomTags = new double[array.size()][array.size()];
		    photosDistUncomNbTags = new double[array.size()][array.size()];

		    // distance based on the distance between average Tags
		    for(int i = 0; i < array.size(); i++) {
			JSONArray allTags = (JSONArray)((JSONObject)((JSONObject) array.get(i)).get("tags")).get("classes");
			JSONArray allProbs = (JSONArray)((JSONObject)((JSONObject) array.get(i)).get("tags")).get("probs");
			
			// Load tags values and probabilities
			for(int j = 0; j < nbTags; j++) {
				tagsValues[i][j] = Double.parseDouble(allProbs.get(j).toString());
			    tagsNames[i][j] = allTags.get(j).toString();
			    
			}
		    }

		    
		    // Comparison of tags with a penality system
		    for (int i = 0 ; i < array.size();i++) {
		    	
		    	
		    	for (int j = 0 ; j < array.size(); j++) {
		    		
		    		double unComSum = 0.0;
		    		int nbComTag = 0;
		    		double comSum = 0.0;
		    		
		    		for (int k = 0 ; k < nbTags ; k++) {
		    			
		    			for (int l = 0 ; l < nbTags;l++) {
		    				
		    				
		    				// if different tag names
		    				if (!tagsNames[i][l].equals(tagsNames[j][k])) {
		    					
		    					unComSum += Math.abs(tagsValues[i][l] - tagsValues[j][k]);
		    					
		    					comSum += 0.2; //Penality as tags are different
		    				}
		    				else
		    				{
		    					unComSum -=0.2; //tags are equal so decrease the penality
		    					comSum -= Math.abs(tagsValues[i][l] - tagsValues[j][k]);
		    					nbComTag += 1; //Tags equal so commonTags incremented
		    				}
		    			}
		    		}
		    		
		    		
		    		photosDistUncomTags[i][j] = unComSum;
		    		photosDistUncomNbTags[i][j] = nbTags - nbComTag;
		    		if (nbComTag > 0 ) {
		    			photosDistComTags[i][j] = comSum / nbComTag;
		    			
		    		}
		    		
		    		
		    	}
		    	
		    }
		    


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
    * Method to affect colors values from the json file to a 2D double array
    */
   public void computePhotosColors() {
	   
	   try {
		    FileReader reader = new FileReader(_photoFileName);

		    JSONParser parser = new JSONParser();

		    Object obj = parser.parse(reader);

		    JSONArray array = (JSONArray) obj;

		    photosDistColors = new double[array.size()][array.size()];
		    
		    int[][] photoColorFirst = new int[array.size()][3]; // RGB = 3 values between 0 and 255
		    int[][] photoColorSecond = new int[array.size()][3]; // RGB = 3 values between 0 and 255

		    // distance based on the distance between average Colors
		    for(int i = 0; i < array.size(); i++) {
		    
		    	JSONObject image = (JSONObject) array.get(i);
		    	photoColorFirst[i][0] = Integer.parseInt(((JSONObject)image.get("color1")).get("r").toString());
		    	photoColorFirst[i][1] = Integer.parseInt(((JSONObject)image.get("color1")).get("b").toString());
		    	photoColorFirst[i][2] = Integer.parseInt(((JSONObject)image.get("color1")).get("g").toString());
		    	
		    	photoColorSecond[i][0] = Integer.parseInt(((JSONObject)image.get("color2")).get("r").toString());
		    	photoColorSecond[i][0] = Integer.parseInt(((JSONObject)image.get("color2")).get("r").toString());
		    	photoColorSecond[i][0] = Integer.parseInt(((JSONObject)image.get("color2")).get("r").toString());
		    	
		    }

		    
		    // Sum of distances between diferent rgb values of the two colors for every photo
		    for(int i = 0; i < array.size(); i++) {
		    	
			for(int j = 0; j < array.size(); j++) {
				
			    double distance1 = Math.sqrt((photoColorFirst[i][0] - photoColorFirst[j][0])*(photoColorFirst[i][0] - photoColorFirst[j][0]))
			    					+ (photoColorFirst[i][1] - photoColorFirst[j][1]) * (photoColorFirst[i][1] - photoColorFirst[j][1])
			    					+ (photoColorFirst[i][2] - photoColorFirst[j][2]) * (photoColorFirst[i][2] - photoColorFirst[j][2]);
			    
			    double distance2 = Math.sqrt((photoColorSecond[i][0] - photoColorSecond[j][0])*(photoColorSecond[i][0] - photoColorSecond[j][0]))
    					+ (photoColorSecond[i][1] - photoColorSecond[j][1]) * (photoColorSecond[i][1] - photoColorSecond[j][1])
    					+ (photoColorSecond[i][2] - photoColorSecond[j][2]) * (photoColorSecond[i][2] - photoColorSecond[j][2]);
			    
			    photosDistColors[i][j] = distance1 + distance2;
			    
			}
			
		
			
		    }
		    


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
    * Method to affect GreyAVG from the Json file to a 2D double array
    */
   public void computePhotosGreyAVG() {
	   
	   try {
		    FileReader reader = new FileReader(_photoFileName);

		    JSONParser parser = new JSONParser();

		    Object obj = parser.parse(reader);

		    JSONArray array = (JSONArray) obj;

		    int[] photoGreyAVG = new int[array.size()];
		    
		    photosDistGreyAVG = new double[array.size()][array.size()];

		    // distance based on the distance between Grey AVG
		    for(int i = 0; i < array.size(); i++) {
		    	
			JSONObject image = (JSONObject) array.get(i);
			photoGreyAVG[i] = Integer.parseInt(image.get("greyavg").toString());
				
		    }

		    
		    for(int i = 0; i < array.size(); i++) {
			for(int j = 0; j < array.size(); j++) {
			    photosDistGreyAVG[i][j] = Math.abs(photoGreyAVG[i] - photoGreyAVG[j]); //Absolute for a better reliability (impossible to know which of i or j index contains the highest value)
			}
			
		    }
		    


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
   private double baseEval(double[][] computed,int[] solution) {
	   
	   double sum = 0;
	   
	   for(int i = 0; i < albumInvDistHash.length; i++) {
		    for(int j = i + 1; j < albumInvDistHash.length; j++) {
			sum += computed[ solution[i] ][ solution[j] ] * albumInvDistHash[i][j] ;
		    }
		}
	   return sum;
	   
   }

   
   /**
    * Function which evluates by taking the computed array matching with the criteria and a solution
    * @param criteria Criteria (HASH,etc...) used in the algorithm to produce an optimized album order
    * @param solution A solution produced by the algorithm (with permutation at every run)
    * @return The sum based on this criteria
    */
   public double eval(AlbumCriteria criteria,int [] solution) {
	   
	double sum = 0;

	switch(criteria) {
		
	case HASH:
		sum = baseEval(photosDistHash,solution);
		break;
		
	case PHASH:
		sum = baseEval(photosDistPhash,solution);
		break;
		
	case DHASH:
		sum = baseEval(photosDistDhash,solution);
		break;
		
	case COLORS:
		sum = baseEval(photosDistColors,solution);
		break;
		
	case GREY_AVG:
		sum = baseEval(photosDistGreyAVG,solution);
		break;
		
	case COMMON_TAGS:
		sum = baseEval(photosDistComTags,solution);
		break;
		
	case UNCOMMON_TAGS:
		sum = baseEval(photosDistUncomTags,solution);
		break;
		
	case NB_UNCOMMON_TAGS:
		sum = baseEval(photosDistUncomNbTags,solution);
		break;
		
	default:
		break;
		
	
	}
	

	return sum;
   }


   
	
}
