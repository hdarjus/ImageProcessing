package imageProcessing;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
//import java.util.HashMap;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class ConvertRepresentation {
//	
//	public static HashMap<Integer, Integer> typeToMat = initializeTypeToMat();
//	
//	private static HashMap<Integer, Integer> initializeTypeToMat () {
//		HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();
//		result.put(BufferedImage.TYPE_USHORT_GRAY, CvType.CV_8UC1);
//		result.put(BufferedImage.TYPE_INT_BGR, CvType.CV_8UC3);
//		result.put(BufferedImage.TYPE_INT_RGB, CvType.CV_8UC3);
//		result.put(BufferedImage.TYPE_INT_ARGB, CvType.CV_8UC4);
//		return result;
//	}
	
	public static Mat toMat (BufferedImage image) {
		byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		int rows = image.getHeight();
        int cols = image.getWidth();
//        System.out.println(BufferedImage.TYPE_3BYTE_BGR);
//        System.out.println(BufferedImage.TYPE_BYTE_GRAY);
//        System.out.println(BufferedImage.TYPE_4BYTE_ABGR);
//        System.out.println(BufferedImage.TYPE_4BYTE_ABGR_PRE);
//        System.out.println(BufferedImage.TYPE_BYTE_BINARY);
//        System.out.println(BufferedImage.TYPE_CUSTOM);
//        System.out.println(BufferedImage.TYPE_BYTE_INDEXED);
//        System.out.println(BufferedImage.TYPE_INT_ARGB);
//        System.out.println(BufferedImage.TYPE_INT_ARGB_PRE);
//        System.out.println(BufferedImage.TYPE_INT_BGR);
//        System.out.println(BufferedImage.TYPE_INT_RGB);
//        System.out.println(BufferedImage.TYPE_USHORT_555_RGB);
//        System.out.println(BufferedImage.TYPE_USHORT_565_RGB);
//        System.out.println(BufferedImage.TYPE_USHORT_GRAY);
//        System.out.println(BufferedImage.BITMASK);
//        System.out.println(BufferedImage.OPAQUE);
//        System.out.println(BufferedImage.SCALE_AREA_AVERAGING);
//        System.out.println(BufferedImage.SCALE_DEFAULT);
//        System.out.println(BufferedImage.SCALE_FAST);
//        System.out.println(BufferedImage.SCALE_REPLICATE);
//        System.out.println(BufferedImage.SCALE_SMOOTH);
//        System.out.println(BufferedImage.TRANSLUCENT);
//        System.out.println(image.getType());
        // int type = typeToMat.get(image.getType());
		Mat m = new Mat(rows, cols, CvType.CV_8UC1);
		m.put(0, 0, pixels);

        return m;
	}

	// CREDITS TO DANIEL: http://danielbaggio.blogspot.com.br/ for the improved
	// version !

	public static BufferedImage toBufferedImage (Mat m) {
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if (m.channels() > 1) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		int bufferSize = m.channels() * m.cols() * m.rows();
		byte[] b = new byte[bufferSize];
		m.get(0, 0, b); // get all the pixels
		BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster()
				.getDataBuffer()).getData();
		System.arraycopy(b, 0, targetPixels, 0, b.length);
		return image;
	}
	
}
