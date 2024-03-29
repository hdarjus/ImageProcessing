package algorithms;

import org.opencv.core.Mat;

import algorithms.ImageDiff.DiffMatrix;

public class Prewitt {

	// x and y axes of a picture are j and i respectively in Mat
	private Mat magn;
	private Mat orie;
	
	public Prewitt () {}
	
	public Mat getMagnitude () {
		return magn;
	}
	
	public Mat getOrientation () {
		return orie;
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
				double oriDeg = (360 * ori.get(i, j)[0]) / Math.PI;
				if (Math.abs(oriDeg) > 67.5) {
					nmsHelper(mag, result, i, j+1, i, j-1);
				} else if (oriDeg >= -67.5 && oriDeg <= -22.5) {
					nmsHelper(mag, result, i-1, j+1, i+1, j-1);
				} else if (Math.abs(oriDeg) < 22.5) {
					nmsHelper(mag, result, i-1, j, i+1, j);
				} else {
					nmsHelper(mag, result, i+1, j+1, i-1, j-1);
				}
			}
		}
		return result;
	}
	
	public Mat detect (Mat mat) {
		ImageDiff d = new ImageDiff(DiffMatrix.PREWITT);
		Mat fx = d.diffX(mat);
		Mat fy = d.diffY(mat);
		magn = magnitude(fx, fy);
		orie = orientation(fx, fy);
		return nonMaximaSuppression (magn, orie);
	}
	
}
