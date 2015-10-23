package algorithms;

import org.opencv.core.Mat;

public class ImageDiff {
	
	// x and y axes of a picture are j and i respectively in Mat
	private double[][] dx;
	private double[][] dy;
	
	public ImageDiff () {
		dx = new double[3][3];
		dy = new double[3][3];
		
		dx[0][0] = -1/3D;
		dx[0][1] = 0;
		dx[0][2] = 1/3D;
		dx[1][0] = -1/3D;
		dx[1][1] = 0;
		dx[1][2] = 1/3D;
		dx[2][0] = -1/3D;
		dx[2][1] = 0;
		dx[2][2] = 1/3D;
		
		dy[0][0] = -1/3D;
		dy[1][0] = 0;
		dy[2][0] = 1/3D;
		dy[0][1] = -1/3D;
		dy[1][1] = 0;
		dy[2][1] = 1/3D;
		dy[0][2] = -1/3D;
		dy[1][2] = 0;
		dy[2][2] = 1/3D;
	}
	
	public Mat diffX (Mat mat) {
		return Filter.apply(mat, dx);
	}
	
	public Mat diffY (Mat mat) {
		return Filter.apply(mat, dy);
	}

}
