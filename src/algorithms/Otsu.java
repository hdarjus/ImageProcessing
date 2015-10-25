package algorithms;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class Otsu {
	
	private short threshold;

	public short getThreshold () {
		return threshold;
	}
	
	public Mat classify (Mat mat) {
		List<Mat> matlist = new ArrayList<Mat>();
		matlist.add(mat);
		MatOfInt channels = new MatOfInt(0);
		Mat mask = new Mat();
		Mat hist = new Mat();
		final int histSizeInt = 256;
		MatOfInt histSize = new MatOfInt(histSizeInt);
		MatOfFloat ranges = new MatOfFloat(new float[] {0, histSizeInt-1});
		
		Imgproc.calcHist(matlist, channels, mask, hist, histSize, ranges);
		hist.convertTo(hist, CvType.CV_32FC1);
		Core.divide(hist, Core.sumElems(hist), hist);
		
		double mean = 0;
		int firstNonZero = histSizeInt;
		for (int i = 0; i < histSizeInt; i++) {
			double value = hist.get(i, 0)[0];
			if (value != 0)
				firstNonZero = Math.min(firstNonZero, i);
			mean += i*value;
		}
		
		Mat q1 = new Mat(histSizeInt - firstNonZero, 1, CvType.CV_32FC1);
		Mat m1 = q1.clone();
		Mat m2 = q1.clone();
		Mat sigma2;
		
		double q1prev = hist.get(firstNonZero, 0)[0];
		double m1curr = 0;
		double m2curr = mean;
		q1.put(0, 0, q1prev);
		m1.put(0, 0, m1curr);
		m2.put(0, 0, m2curr);
		for (int t = firstNonZero + 1; t < histSizeInt; t++) {
			double pt1 = hist.get(t, 0)[0];
			double q1curr = q1prev + pt1;
			m1curr = (q1prev*m1curr + (t+1)*pt1)/q1curr;
			m2curr = (mean - q1curr*m1curr)/(1 - q1curr);
			q1prev = q1curr;
			q1.put(t - firstNonZero, 0, q1curr);
			m1.put(t - firstNonZero, 0, m1curr);
			m2.put(t - firstNonZero, 0, m2curr);
		}
		
		Mat temp = new Mat();
		Core.subtract(q1, new Scalar(1), temp);
		Core.multiply(temp, new Scalar(-1), temp);
		System.out.println("Q1: " + q1.dump());
		System.out.println("1-Q1: " + temp.dump());
		sigma2 = q1.clone();
		Core.multiply(sigma2, temp, sigma2);
		Core.subtract(m1, m2, temp);
		Core.multiply(temp, temp, temp);
		Core.multiply(sigma2, temp, sigma2);
		
		MinMaxLocResult mmlr = Core.minMaxLoc(sigma2);
		System.out.println("MMLR: " + mmlr.maxLoc.y);
		
		threshold = (short) (firstNonZero + mmlr.maxLoc.y);
		Mat result = new Mat();
		Core.compare(mat, new Scalar(threshold), result, Core.CMP_GE);
		return result;
	}

}
