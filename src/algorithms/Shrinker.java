package algorithms;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class Shrinker {

	private Mat trmask;
	private Mat nzmask;
	
	public Shrinker () {
		trmask = Mat.zeros(3, 3, CvType.CV_32SC1);
		nzmask = Mat.zeros(3, 3, CvType.CV_32SC1);
		
		trmask.put(0, 1, 1);	// p2
		trmask.put(0, 0, 2);	// p3
		trmask.put(1, 0, 4);	// p4
		trmask.put(2, 0, 8);	// p5
		trmask.put(2, 1, 16);	// p6
		trmask.put(2, 2, 32);	// p7
		trmask.put(1, 2, 64);	// p8
		trmask.put(0, 2, 128);	// p9
		
		nzmask.put(0, 1, 1);	// p2
		nzmask.put(1, 0, 1);	// p4
		nzmask.put(2, 1, 1);	// p6
		nzmask.put(1, 2, 1);	// p8
	}
	
	public Mat shrink (Mat mat) {
		Mat u1 = mat.clone();
		Mat u2 = mat.clone();
		boolean changed = true;
		while (changed) {
			changed = false;
			for (int i = 2; i < mat.height()-1; i++) {
				for (int j = 2; j < mat.width()-1; j++) {
					if (deletePoint(u1, i, j)) {
						u2.put(i, j, 0);
						changed = true;
					}
				}
			}
			
			Mat temp = u1;
			u1 = u2;
			u2 = temp;
		}
		
		return u1;
	}
	
	private int tr (Mat mat, int i, int j) {	// point (i,j) should not be on the edge
		Mat neigh = mat.submat(i-1, i+1, j-1, j+1);
		Mat maskedneigh = new Mat();
		Core.multiply(neigh, trmask, maskedneigh);
		int sum = (int) Core.sumElems(maskedneigh).val[0];
		int transitions = 0;
		if (sum < 128 && sum % 2 == 1)
			transitions++;
		while (sum > 0) {
			transitions += (1 - (sum%2)) * ((sum/2) % 2);
			sum /= 2;
		}
		return transitions;
	}
	
	private int nz (Mat mat, int i, int j) {	// point (i,j) should not be on the edge
		Mat neigh = mat.submat(i-1, i+1, j-1, j+1);
		Mat maskedneigh = new Mat();
		Core.multiply(neigh, nzmask, maskedneigh);
		return (int) Core.sumElems(maskedneigh).val[0];
	}
	
	private boolean p2p4p8 (Mat mat, int i, int j) {	// returns false iff p2, p4 and p8 are all 1
		return nz(mat, i, j) < 3 + mat.get(2, 1)[0];
	}
	
	private boolean p2p4p6 (Mat mat, int i, int j) {	// returns false iff p2, p4 and p6 are all 1
		return nz(mat, i, j) < 3 + mat.get(1, 2)[0];
	}
	
	private boolean deletePoint (Mat mat, int i, int j) {
		int nzhelp = nz(mat, i, j);
		return (nzhelp >= 2) && (nzhelp <= 6)
				&& (tr(mat, i, j) == 1)
				&& (p2p4p8(mat, i, j) || tr(mat, i-1, j) != 1)
				&& (p2p4p6(mat, i, j) || tr(mat, i, j-1) != 1);
	}
}
