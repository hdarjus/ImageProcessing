package algorithms;

import org.opencv.core.Mat;

public class Filter {
	
	// Apply square filter to image
	public static Mat apply (Mat mat, double[][] filter) {
		Mat result = mat.clone();
		int filtersize = filter.length;
		int half = filtersize / 2;
		// For all the pixels that the filter can be put on
		for (int i = half; i < mat.height() - half; i++) {
			for (int j = half; j < mat.width() - half; j++) {
				double sum = 0;
				// For all the fields that the filter covers
				for (int k = i - half; k <= i + half; k++) {
					for (int l = j - half; l <= j + half; l++) {
						sum += filter[k - (i - half)][l - (j - half)] * mat.get(k, l)[0];
					}
				}
				result.put(i, j, new double[]{sum});
			}
		}
		return result;
	}

	public static void fillFrameAvg (Mat result, Mat mat, double[][] filter) {
		int framesize = filter.length;
		double avg = 0;
		// Calculate average
		for (int i = 0; i < mat.height(); i++) {
			for (int j = 0; j < mat.width(); j++) {
				avg += mat.get(i, j)[0];
			}
		}
		avg /= mat.height() * mat.width();
		// Fill frame
		for (int i = 0; i < result.height(); i++) {
			for (int j = 0; j < result.width(); j++) {
				if (i < framesize || i >= result.height()-1
						|| j < framesize || j >= result.width()-1) {
					result.put(i, j, new double[]{avg});
				}
			}
		}
	}
	
}
