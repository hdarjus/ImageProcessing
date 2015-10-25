package algorithms;

import org.opencv.core.Mat;

public class ImageDiff {
	
	// x and y axes of a picture are j and i respectively in Mat
	private double[][] dx;
	private double[][] dy;
	
	public enum DiffMatrix {PREWITT, SOBEL}
	
	public ImageDiff (DiffMatrix type) {
		dx = new double[3][3];
		dy = new double[3][3];
		
		double mid, denom;
		
		switch (type) {
		case PREWITT:
			mid = 1;
			denom = 3;
			break;
		case SOBEL:
			mid = 2;
			denom = 4;
			break;
		default:
			mid = 1;
			denom = 3;
			break;
		}
		
		dx[0][0] = -1/denom;
		dx[0][1] = 0;
		dx[0][2] = 1/denom;
		dx[1][0] = -mid/denom;
		dx[1][1] = 0;
		dx[1][2] = mid/denom;
		dx[2][0] = -1/denom;
		dx[2][1] = 0;
		dx[2][2] = 1/denom;
		
		dy[0][0] = -1/denom;
		dy[1][0] = 0;
		dy[2][0] = 1/denom;
		dy[0][1] = -mid/denom;
		dy[1][1] = 0;
		dy[2][1] = mid/denom;
		dy[0][2] = -1/denom;
		dy[1][2] = 0;
		dy[2][2] = 1/denom;
	}
	
	public Mat diffX (Mat mat) {
		return Filter.apply(mat, dx);
	}
	
	public Mat diffY (Mat mat) {
		return Filter.apply(mat, dy);
	}

}
