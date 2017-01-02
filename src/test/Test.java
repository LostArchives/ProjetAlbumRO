package test;
import model.AlbumPhoto;
import model.AlgorithmManager;
import model.algorithms.HillClimberFirst;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		AlbumPhoto album = new AlbumPhoto("data/info-album.json","data/info-photo.json",55);
		
		HillClimberFirst hcf = new HillClimberFirst(album,10000);
		
		AlgorithmManager.Instance().LaunchHillClimber(hcf);
		
		
		
		
	}

}
