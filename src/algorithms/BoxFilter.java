package algorithms;

import org.opencv.core.Mat;

public class BoxFilter {

	public static Mat boxFilter (Mat mat, int filtersize) {
		Mat result = mat.clone();
		int half = filtersize / 2;
		int filtersizeSquare = filtersize * filtersize;
		// For all the pixels that the filter can be put on
		for (int i = half; i < mat.height() - half; i++) {
			for (int j = half; j < mat.width() - half; j++) {
				double sum = 0;
				// For all the fields that the filter covers
				for (int k = i - half; k <= i + half; k++) {
					for (int l = j - half; l <= j + half; l++) {
						sum += mat.get(k, l)[0];
					}
				}
				sum /= filtersizeSquare;
				result.put(i, j, new double[]{sum});
			}
		}
		// Fill frame with average
		fillFrameAvg(result, mat, half);
		return result;
	}
	
	public static Mat runningBoxFilter (Mat mat, int filtersize) {
		Mat result = mat.clone();
		int half = filtersize / 2;
		
		boolean transposed = false;
		if (result.width() > 100*result.height()) {
			result = result.t();
			transposed = true;
		}
		
		// Helper array
		double[] S = new double[result.width()];
		for (int j = 0; j < result.width(); j++) {
			double s = 0;
			for (int i = 0; i < filtersize; i++) {
				s += result.get(i, j)[0];
			}
			S[j] = s;
		}
		
		// For all the pixels that the filter can be put on
		int filtersizeSquare = filtersize * filtersize;
		for (int i = half; i < mat.height() - half; i++) {
			// Initialize the sum
			double sum = 0;
			for (int ii = 0; ii < filtersize; ii++) {
				sum += S[ii];
			}
			// Go to the right one by one
			for (int j = half; j < mat.width() - half - 1; j++) {
				result.put(i, j, new double[]{sum / filtersizeSquare});
				sum += S[j+half+1] - S[j-half];
			}
			// Last column
			result.put(i, mat.width() - half, new double[]{sum / filtersizeSquare});
			// Update S if applicable
			if (i < mat.height() - half - 1) {
				for (int j = 0; j < mat.width(); j++) {
					S[j] += mat.get(i+half+1, j)[0] - mat.get(i-half, j)[0];
				}
			}
			
		}
		
		// Fill frame with average
		fillFrameAvg(result, mat, half);
		
		if (transposed) {
			result = result.t();
		}
		return result;
	}
	
	public static void fillFrameAvg (Mat result, Mat mat, int framesize) {
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
