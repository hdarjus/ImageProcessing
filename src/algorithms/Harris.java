package algorithms;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import algorithms.ImageDiff.DiffMatrix;

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;

public class Harris {
	
	private double alpha;
	private double hthr;
	private int filtersize;
	private Mat corners;
	
	public Harris (double a, double thr, int f) {
		alpha = a;
		hthr = thr;
		filtersize = f;
	}
	
	public Mat getCorners() {
		return corners;
	}
	
	public Mat detect (Mat mat) {
		ImageDiff d = new ImageDiff(DiffMatrix.SOBEL);
		Mat fx = d.diffX(mat);
		Mat fy = d.diffY(mat);
		fx.convertTo(fx, CvType.CV_32FC1);
		fy.convertTo(fy, CvType.CV_32FC1);
		BoxFilter box = new BoxFilter(filtersize);
		Mat fx2 = fx.clone();
		Mat fy2 = fx.clone();
		Mat fxfy = fx.clone();
		Core.multiply(fx, fx, fx2);
		Core.multiply(fy, fy, fy2);
		Core.multiply(fx, fy, fxfy);
		Mat avgfx2 = box.boxFilter(fx2);
		Mat avgfy2 = box.boxFilter(fy2);
		Mat avgfxfy = box.boxFilter(fxfy);
		Mat c = fx.clone();
		Mat temp = fx.clone();
		Mat result = fx.clone();
		Core.multiply(avgfx2, avgfy2, c);
		Core.multiply(avgfxfy, avgfxfy, temp);
		Core.subtract(c, temp, c);
		Core.add(avgfx2, avgfy2, temp);
		Core.multiply(temp, temp, temp);
		Core.multiply(temp, new Scalar(alpha), temp);
		Core.subtract(c, temp, c);
		// Without non-maxima suppression
		Core.compare(c, new Scalar(hthr), result, Core.CMP_GE);
		corners = result.clone();
		corners.convertTo(corners, CvType.CV_8UC1);
		// With non-maxima suppression
		c = nms(c, filtersize);
		Core.compare(c, new Scalar(hthr), result, Core.CMP_GE);
		result.convertTo(result, CvType.CV_8UC1);
		return result;
	}
	
	private Mat nms(Mat mat, int fs) {
		int half = fs/2;
		Mat result = mat.clone();
		for (int i = half; i < mat.height() - half; i++) {
			for (int j = half; j < mat.width() - half; j++) {
				double val = mat.get(i, j)[0];
				Mat sub = mat.submat(i-half, i+half+1, j-half, j+half+1);
				MinMaxLocResult max = Core.minMaxLoc(sub);
				if (val < max.maxVal)
					result.put(i, j, new double[]{0});
			}
		}
		return result;
	}

}
