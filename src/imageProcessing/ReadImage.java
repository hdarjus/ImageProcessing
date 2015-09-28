package imageProcessing;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
//import org.opencv.core.CvType;

public class ReadImage {
	
	public static Mat readImage (String filepath)
			throws IllegalArgumentException, IOException, RuntimeException {
		return readImage (filepath, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
	}

	public static Mat readImage (String filepath, int flags)
			throws IllegalArgumentException, IOException, RuntimeException {
		return readAndShowImage (filepath, flags, false);
	}
	
	public static Mat readAndShowImage (String filepath, int flags, boolean show)
			throws IllegalArgumentException, IOException, RuntimeException {
		// ex. flags == Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE
		File file = new File (filepath);
		if (!(file.exists() && file.isFile() && file.canRead())) {
			throw new IllegalArgumentException("Invalid filepath");
		}
		
		String[] parts = file.getName().split("\\.");
		String extension = parts[parts.length - 1];
		
		String[] knownExtensions = new String[]{"png", "tif", "jpg", "bmp"};
		
		ShowImage showImage = new ShowImage("Original");
		
		if (extension.compareTo("gif") == 0) {
			BufferedImage image = ImageIO.read(file);
			if (show) {
				showImage.showImageOriginal(image);
			}
			BufferedImage gray = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
			Graphics2D g = gray.createGraphics();
            g.drawImage(image, 0, 0, null);
			return ConvertRepresentation.toMat(gray);
		} else if ((new HashSet<String>(Arrays.asList(knownExtensions))).contains(extension)) {
			Mat m = Imgcodecs.imread(file.getAbsolutePath(), flags);
			if (show) {
				showImage.showImageOriginal(m);
			}
			return m;
		} else {
			throw new RuntimeException();
		}
	}
	
}
