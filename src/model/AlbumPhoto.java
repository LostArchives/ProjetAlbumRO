package model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AlbumPhoto {

	private String _albumFileName; // Path of the Json file storing album informations
	private String _photoFileName; // Path of the Json file storing photos informations
	private int _nbPhotos; // Number of photos in the album
	
	// Distance between photos
    public double [][] photoDist;
    // Inverse of the distance between positions in the album
    public double [][] albumInvDist;
	
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
	computePhotoDistances();
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

   public void computePhotoDistances() {
	   
	   
	try {
	    FileReader reader = new FileReader(_photoFileName);

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
   public double eval(int [] solution) {
	double sum = 0;

	for(int i = 0; i < albumInvDist.length; i++) {
	    for(int j = i + 1; j < albumInvDist.length; j++) {
		sum += photoDist[ solution[i] ][ solution[j] ] * albumInvDist[i][j] ;
	    }
	}

	return sum;
   }


   
	
}
