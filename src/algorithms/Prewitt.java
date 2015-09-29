package algorithms;

import org.opencv.core.Mat;

public class Prewitt {

	// x and y axes of a picture are j and i respectively in Mat
	private double[][] prewittX;
	private double[][] prewittY;
	private Mat magn;
	private Mat orie;
	
	public Prewitt () {
		prewittX = new double[3][3];
		prewittY = new double[3][3];
		
		prewittX[0][0] = -1/3D;
		prewittX[0][1] = 0;
		prewittX[0][2] = 1/3D;
		prewittX[1][0] = -1/3D;
		prewittX[1][1] = 0;
		prewittX[1][2] = 1/3D;
		prewittX[2][0] = -1/3D;
		prewittX[2][1] = 0;
		prewittX[2][2] = 1/3D;
		
		prewittY[0][0] = -1/3D;
		prewittY[1][0] = 0;
		prewittY[2][0] = 1/3D;
		prewittY[0][1] = -1/3D;
		prewittY[1][1] = 0;
		prewittY[2][1] = 1/3D;
		prewittY[0][2] = -1/3D;
		prewittY[1][2] = 0;
		prewittY[2][2] = 1/3D;
	}
	
	public Mat getMagnitude () {
		return magn;
	}
	
	public Mat getOrientation () {
		return orie;
	}

	private Mat diffX (Mat mat) {
		return Filter.apply(mat, prewittX);
	}
	
	private Mat diffY (Mat mat) {
		return Filter.apply(mat, prewittY);
	}
	
	private static Mat magnitude (Mat fx, Mat fy) {
		Mat result = fx.clone();
		for (int i = 0; i < result.height(); i++) {
			for (int j = 0; j < result.width(); j++) {
				double fxij = fx.get(i, j)[0];
				double fyij = fy.get(i, j)[0];
				result.put(i, j, new double[]{Math.sqrt(fxij*fxij + fyij*fyij)});
			}
		}
		return result;
	}
	
	private static Mat orientation (Mat fx, Mat fy) {
		Mat result = fx.clone();
		for (int i = 0; i < result.height(); i++) {
			for (int j = 0; j < result.width(); j++) {
				double fxij = fx.get(i, j)[0];
				double fyij = fy.get(i, j)[0];
				result.put(i, j, new double[]{Math.atan(fxij/fyij)});
			}
		}
		return result;
	}
	
	private void nmsHelper (final Mat mag, Mat result, int i1, int j1, int i2, int j2) {
		int i = (i1 + i2) / 2;
		int j = (j1 + j2) / 2;
		// Using naming from the slides
		double ma = mag.get(i1, j1)[0];		// one neighbour
		double mb = mag.get(i2, j2)[0];		// other neighbour
		double mc = mag.get(i, j)[0];		// examined pixel
		if (ma > mc || mb > mc) {
			result.put(i, j, new double[]{0});
		}
	}
	
	private Mat nonMaximaSuppression (final Mat mag, final Mat ori) {
		Mat result = mag.clone();
		for (int i = 1; i < mag.height() - 1; i++) {
			for (int j = 1; j < mag.width() - 1; j++) {
				// Absolute value of the orientation in degree
				double oriDeg = (360 * ori.get(i, j)[0]) / Math.PI;
				if (Math.abs(oriDeg) > 67.5) {
					nmsHelper(mag, result, i-1, j, i+1, j);
				} else if (oriDeg >= -67.5 && oriDeg <= -22.5) {
					nmsHelper(mag, result, i+1, j+1, i-1, j-1);
				} else if (Math.abs(oriDeg) < 22.5) {
					nmsHelper(mag, result, i, j+1, i, j-1);
				} else {
					nmsHelper(mag, result, i-1, j+1, i+1, j-1);
				}
			}
		}
		return result;
	}
	
	public Mat detect (Mat mat) {
		Mat fx = diffX(mat);
		Mat fy = diffY(mat);
		magn = magnitude(fx, fy);
		orie = orientation(fx, fy);
		return nonMaximaSuppression (magn, orie);
	}
	
}
