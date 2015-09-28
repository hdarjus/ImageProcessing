package imageProcessing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class WriteImage {
	
	public static void writeImage (Mat mat, String filepath) throws IOException {
		File file = new File (filepath);
		
		String[] parts = file.getName().split("\\.");
		String extension = parts[parts.length - 1];
		
		String[] knownExtensions = new String[]{"png", "tif", "jpg", "bmp"};
		
		if (extension.equals("gif")) {
			BufferedImage image = ConvertRepresentation.toBufferedImage(mat);
			ImageIO.write(image, "gif", file);
		} else if ((new HashSet<String>(Arrays.asList(knownExtensions))).contains(extension)) {
			Imgcodecs.imwrite(file.getAbsolutePath(), mat);
		} else {
			throw new RuntimeException();
		}
	}
}
