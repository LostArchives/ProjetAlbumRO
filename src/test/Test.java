package test;

import model.AlbumCriteria;
import model.AlbumPhoto;
import model.AlgorithmManager;
import model.algorithms.HillClimberFirst;
import model.algorithms.IterativeLocalSearch;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		AlbumPhoto album = new AlbumPhoto("data/info-album.json","data/info-photo.json",55);
		
		HillClimberFirst hcf = new HillClimberFirst(album,AlbumCriteria.COLORS);
		
		HillClimberFirst hcfi = new HillClimberFirst(album,AlbumCriteria.COLORS);
		
		AlgorithmManager.Instance().LaunchHillClimber(hcf,50000);
		
		IterativeLocalSearch ils = new IterativeLocalSearch(album,hcfi,6000,30000);
		
		AlgorithmManager.Instance().LaunchIterativeLocalSearch(ils,50000);
		
		
	}

}
