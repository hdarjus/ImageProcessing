package algorithms;

import org.opencv.core.Mat;

public class BoxFilter {
	
	private double[][] box;
	
	public BoxFilter (int filtersize) {
		box = new double[filtersize][filtersize];
		double value = 1/(filtersize * (double) filtersize);
		for (int i = 0; i < filtersize; i++) {
			for (int j = 0; j < filtersize; j++) {
				box[i][j] = value;
			}
		}
	}

	public Mat boxFilter (Mat mat, int filtersize) {
		Mat result = Filter.apply(mat, box);
		// Fill frame with average
		Filter.fillFrameAvg(result, mat, box);
		return result;
	}
	
	public Mat runningBoxFilter (Mat mat, int filtersize) {
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
		Filter.fillFrameAvg(result, mat, box);
		
		if (transposed) {
			result = result.t();
		}
		return result;
	}

}
