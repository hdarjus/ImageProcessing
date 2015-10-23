package algorithms;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Core;

public class Harris {
	
	private double alpha;
	private double hthr;
	private int filtersize;
	
	public Harris (double a, double thr, int f) {
		alpha = a;
		hthr = thr;
		filtersize = f;
	}
	
	public Mat detectEdges (Mat mat) {
		ImageDiff d = new ImageDiff();
		Mat fx = d.diffX(mat);
		Mat fy = d.diffY(mat);
		BoxFilter box = new BoxFilter(filtersize);
		Mat fx2 = fx.mul(fx);
		Mat fy2 = fy.mul(fy);
		Mat fxfy = fx.mul(fy);
		Mat avgfx2 = box.boxFilter(fx2);
		Mat avgfy2 = box.boxFilter(fy2);
		Mat avgfxfy = box.boxFilter(fxfy);
		Mat c = avgfx2.mul(avgfy2);
		Core.subtract(c, avgfxfy.mul(avgfxfy), c);
		Mat temp = avgfx2.clone();
		Core.add(avgfx2, avgfy2, temp);
		temp = temp.mul(temp);
		Core.scaleAdd(temp, (-1)*alpha, c, c);
		Mat result = mat.clone();
		Core.compare(c, new Scalar(hthr), result, Core.CMP_GT);
		return result;
	}

}
