package imageProcessing;

import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import algorithms.BoxFilter;
import algorithms.Prewitt;
import ml.options.OptionSet;
import ml.options.Options;
import ml.options.Options.Multiplicity;
import ml.options.Options.Separator;

public class Main {
	
	private static enum Task {BOX_FILTER, EDGE_DETECTION}
	
	private static Task task;
	
	private static OptionSet set;
	private static Options opt;
	
	private static String inputpath;
	private static String out1;
	private static String out2;
	
	private static int filtersize;
	// private static double hthr;
	// private static double alpha;
	
	private static void setAndValidateOptions (String[] args) throws Exception {
		// Define command line options
		opt = new Options (args, 0);
		opt.addSet("boxset").addOption("box").addOption("filtersize", Separator.BLANK, Multiplicity.ZERO_OR_ONE)
			.addOption("out1", Separator.BLANK, Multiplicity.ZERO_OR_ONE).addOption("out2", Separator.BLANK, Multiplicity.ZERO_OR_ONE);
		opt.addOptionAllSets("i", Separator.BLANK, Multiplicity.ONCE);
		// opt.addSet("hset").addOption("h");
		set = opt.getMatchingSet(true, false);

		if (set.isSet("i")) {
			inputpath  = set.getOption("i").getResultValue(0);
		} else {
			throw new RuntimeException();
		}
		
		if (set.getSetName().equals("boxset")) {
			filtersize = 3;
			if (set.isSet("filtersize")) {
				filtersize = Integer.parseInt(set.getOption("filtersize").getResultValue(0));
				if (filtersize%2 == 0 || filtersize < 1) {
					throw new RuntimeException("Filter size should be a positive odd number");
				}
			}
			if (set.isSet("out1")) {
				out1 = set.getOption("out1").getResultValue(0);
			}
			if (set.isSet("out2")) {
				out2 = set.getOption("out2").getResultValue(0);
			}
			task = Task.BOX_FILTER;
		} else {
			throw new RuntimeException();
		}
	}
	
	private static void printUsage() {
		System.out.println("Usage:");
		System.out.println("ImageProcessing [/h | /i <inputpath> <algorithm flag> [<algorithm parameters>]]");
		System.out.println();
		System.out.println("  /h: Print this help.");
		System.out.println("  <inputpath> is the path of the input image file.");
		System.out.println();
		System.out.println("Values for <algorithm flag>:");
		System.out.println("  /box for box filtering");
		System.out.println();
		System.out.println("Values for <algorithm parameters>:");
		System.out.println("  /filtersize is the size of the filter");
		System.out.println("  /hthr is the value of hthr");
		System.out.println("  /alpha is the value of alpha");
		System.out.println("  /out1 is the first output path");
		System.out.println("  /out2 is the second output path");
	}

	public static void processImageWithBoxFilter(Mat mat, String out1, String out2, int filtersize)
			throws IOException {
		if (filtersize > mat.width() || filtersize > mat.height())
			throw new RuntimeException("Filter size bigger than picture dimensions");
		Stopwatch timer = new Stopwatch();
		BoxFilter boxFilter = new BoxFilter(filtersize);
		
		// Box filter
		timer.start();
		Mat boxFiltered = boxFilter.boxFilter(mat, filtersize);
		timer.stop();
		long boxFilterTime = timer.getElapsedMilliseconds();
		
		// Running box filter
		timer.start();
		Mat runningBoxFiltered = boxFilter.runningBoxFilter(mat, filtersize);
		timer.stop();
		long runningBoxFilterTime = timer.getElapsedMilliseconds();
		
		// Show images
		ShowImage.showImage(boxFiltered, "Box Filter");
		ShowImage.showImage(runningBoxFiltered, "Running Box Filter");
		
		// Print times
		System.out.println("Box filtered time in milliseconds:         " + boxFilterTime);
		System.out.println("Running box filtered time in milliseconds: " + runningBoxFilterTime);
		
		// Save images
		if (out1 != null) {
			WriteImage.writeImage(boxFiltered, out1);
		}
		if (out2 != null) {
			WriteImage.writeImage(runningBoxFiltered, out2);
		}
	}
	
	public static void processImageWithPrewitt (Mat mat, String out1, String out2)
			throws IOException {
		Prewitt prewitt = new Prewitt();
		
		// Result
		Mat result = prewitt.detect(mat);
		
		// Magnitude
		Mat magnitude = prewitt.getMagnitude();
		
		// Show images
		ShowImage.showImage(magnitude, "Magnitude");
		ShowImage.showImage(result, "Result after NMS");
		
		// Save images
		if (out1 != null) {
			WriteImage.writeImage(magnitude, out1);
		}
		if (out2 != null) {
			WriteImage.writeImage(result, out2);
		}
	}
	
	public static void main(String[] args) {
		// Load the native library
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		
		try {
			Mat mat;
			
			/*
			 * Define and validate command line arguments
			 */
			
			setAndValidateOptions(args);

			/*
			 *  Read image
			 */
			
			mat = ReadImage.readAndShowImage(inputpath, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE, true);
			
			/*
			 * Process and write image
			 */
			
			switch (task) {
			case BOX_FILTER:
				Main.processImageWithBoxFilter(mat, out1, out2, filtersize);
				break;
			case EDGE_DETECTION:
				Main.processImageWithPrewitt(mat, out1, out2);
				break;
			}
		} catch (Exception e) {
			if (e.getMessage() != null) {
				System.out.println("An error occured. " + e.getMessage());
			}
			// Print help
			printUsage();
			return;
		}
	}
	
}
