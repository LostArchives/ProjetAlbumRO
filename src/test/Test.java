package test;

import model.AlbumCriteria;
import model.AlbumPhoto;
import model.AlgorithmManager;
import model.algorithms.HillClimberFirst;
import model.algorithms.IterativeLocalSearch;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		////////////////////////Album Declaration ////////////////////////////////////////////
		
		AlbumPhoto album = new AlbumPhoto("data/info-album.json","data/info-photo.json",55);
		
		
		/* 
		
		For the next examples , you can just comment or customize the example you want
		
		WARNING : Every example rewrites the album at the end
		 so if you run two algorithm at the same time (HCF+ILS) , you will lose the first album result
		 
		*/
		
		
		////////////////////////Example HillClimber ///////////////////////////////////////
		/*To change the criteria, just change the AlbumCriteria parameter (AlbumCriteria.XX) */
		
		
		HillClimberFirst hcf = new HillClimberFirst(album,AlbumCriteria.HASH);
		
		AlgorithmManager.Instance().LaunchHillClimber(hcf,25000);
		
		
		////////////////////////// Example ILS ///////////////////////////////
		
		/*
		
		For the ILS object : the first parameter is the album , 
		the second is the number of mutations and the third one the number of hillClimber iterations)
		
		As always , you can change the criteria in the hcfi variable
		
		*/
		
		
		HillClimberFirst hcfi = new HillClimberFirst(album,AlbumCriteria.HASH);
		
		IterativeLocalSearch ils = new IterativeLocalSearch(album,hcfi,150,25000);
			
		AlgorithmManager.Instance().LaunchIterativeLocalSearch(ils,10000);
		
		
		
		// Notes :
		
		////////////////////////// NeighBoring calculation///////////////////////////////////
		
		//	hcfi.computeCriteria();
		
		//	getMin(hcfi.getCriteriaArray());
		//	getMax(hcfi.getCriteriaArray());
		
		
		////////////////////////// End NeighBoring Calculation ///////////////////////////////
		
		
	
		
		
	}
	
	public static void getMin(double[][] arr) {
		
		double min = 1000000000;
		for (int i = 0 ; i< arr.length;i++) {
			
			for (int j = 0 ; j < arr[i].length;j++) {
				
				if (min > arr[i][j]) {
					
					min = arr[i][j];
				}
				
			}
		}
		
		System.out.println("Min : " + min);
	}
	
	public static void getMax(double[][] arr) {
		
		double max = 0;
		for (int i = 0 ; i< arr.length;i++) {
			
			for (int j = 0 ; j < arr[i].length;j++) {
				
				if (max < arr[i][j]) {
					
					max = arr[i][j];
				}
				
			}
		}
		
		System.out.println("Max : "+ max);
		
	}

}
