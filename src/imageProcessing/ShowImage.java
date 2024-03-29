package imageProcessing;

/*
 * Author: ATUL
 * Thanks to Daniel Baggio , Jan Monterrubio and sutr90 for improvements
 * This code can be used as an alternative to imshow of OpenCV for JAVA-OpenCv 
 * Make sure OpenCV Java is in your Build Path
 * Usage :
 * -------
 * Imshow ims = new Imshow("Title");
 * ims.showImage(Mat image);
 * Check Example for usage with Webcam Live Video Feed
 */

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ShowImage {

	public JFrame Window;
	private ImageIcon image;
	private JLabel label;
	private Boolean SizeCustom;
	private int Height, Width;

	public ShowImage(String title) {
		Window = new JFrame();
		image = new ImageIcon();
		label = new JLabel();
		label.setIcon(image);
		Window.getContentPane().add(label);
		Window.setResizable(true);
		Window.setTitle(title);
		SizeCustom = false;
		setCloseOption(0);
	}

	public ShowImage(String title, int height, int width) {
		SizeCustom = true;
		Height = height;
		Width = width;

		Window = new JFrame();
		image = new ImageIcon();
		label = new JLabel();
		label.setIcon(image);
		Window.getContentPane().add(label);
		Window.setResizable(true);
		Window.setTitle(title);
		setCloseOption(0);

	}

	public void showImageOriginal(Mat img) {
		if (SizeCustom) {
			Imgproc.resize(img, img, new Size(Height, Width));
		}
		BufferedImage bufImage = null;
		try {
			bufImage = ConvertRepresentation.toBufferedImage(img);
			image.setImage(bufImage);
			Window.pack();
			label.updateUI();
			Window.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showImageOriginal(BufferedImage bufImage) {
		try {
			image.setImage(bufImage);
			Window.pack();
			label.updateUI();
			Window.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void showImage (Mat mat, String title) {
		ShowImage im = new ShowImage(title);
		im.showImageOriginal(mat);
	}

	// Thanks to sutr90 for reporting the issue : https://github.com/sutr90

	public void setCloseOption(int option) {

		switch (option) {
		case 0:
			Window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			break;
		case 1:
			Window.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			break;
		default:
			Window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		}

	}

	/**
	 * Sets whether this window should be resizable or not, by default it is not
	 * resizable
	 * 
	 * @param resizable
	 *            <code>true</code> if the window should be resizable,
	 *            <code>false</code> otherwise
	 */
	public void setResizable(boolean resizable) {
		Window.setResizable(resizable);
	}


	// Thanks to Jan Monterrubio for additional static methods for viewing images. 


	/**
	 * Displays the given {@link Mat} in a new instance of {@link ShowImage}
	 * 
	 * @param mat
	 *            the {@link Mat} to display
	 */
	public static void show(Mat mat) {
		show(mat, new Dimension(mat.rows(), mat.cols()), "", false,
				WindowConstants.EXIT_ON_CLOSE);
	}

	/**
	 * Displays the given {@link Mat} in a new instance of {@link ShowImage} with
	 * the given title as the title for the window
	 * 
	 * @param mat
	 *            the {@link Mat} to display
	 * @param frameTitle
	 *            the title for the frame
	 */
	public static void show(Mat mat, String frameTitle) {
		show(mat, new Dimension(mat.rows(), mat.cols()), frameTitle, false,
				WindowConstants.EXIT_ON_CLOSE);
	}

	/**
	 * Displays the given {@link Mat} in a new instance of {@link ShowImage} with
	 * the given title as the title for the window and determines whether the
	 * frame is resizable or not
	 * 
	 * @param mat
	 *            the {@link Mat} to display
	 * @param frameTitle
	 *            the title for the frame
	 * @param resizable
	 *            whether the frame should be resizable or not
	 */
	public static void show(Mat mat, String frameTitle, boolean resizable) {
		show(mat, new Dimension(mat.rows(), mat.cols()), frameTitle, resizable,
				WindowConstants.EXIT_ON_CLOSE);
	}

	/**
	 * Displays the given {@link Mat} in a new instance of {@link ShowImage} with a
	 * set size
	 * 
	 * @param mat
	 *            the {@link Mat} to display
	 * @param frameSize
	 *            the size for the frame
	 */
	public static void show(Mat mat, Dimension frameSize) {
		show(mat, frameSize, "", false, WindowConstants.EXIT_ON_CLOSE);
	}

	/**
	 * Displays the given {@link Mat} in a new instance of {@link ShowImage} with a
	 * set size and given title
	 * 
	 * @param mat
	 *            the {@link Mat} to display
	 * @param frameSize
	 *            the size for the frame
	 * @param frameTitle
	 *            the title for the frame
	 */
	public static void show(Mat mat, Dimension frameSize, String frameTitle) {
		show(mat, frameSize, frameTitle, false, WindowConstants.EXIT_ON_CLOSE);
	}

	/**
	 * Displays the given {@link Mat} in a new instance of {@link ShowImage} with a
	 * set size and given title and whether it is resizable or not
	 * 
	 * @param mat
	 *            the {@link Mat} to display
	 * @param frameSize
	 *            the size for the frame
	 * @param frameTitle
	 *            the title for the frame
	 */
	public static void show(Mat mat, Dimension frameSize, String frameTitle,
			boolean resizable) {
		show(mat, frameSize, frameTitle, resizable,
				WindowConstants.EXIT_ON_CLOSE);
	}

	/**
	 * Displays the given {@link Mat} in a new instance of {@link ShowImage} with a
	 * set size and given title and whether it is resizable or not, and with the
	 * close operation set
	 * 
	 * @param mat
	 *            the {@link Mat} to display
	 * @param frameSize
	 *            the size for the frame
	 * @param frameTitle
	 *            the title for the frame
	 * @param resizable
	 *            wether the frame is resizable or not
	 * @param closeOperation
	 *            the constant for the default close operation of the frame
	 */
	public static void show(Mat mat, Dimension frameSize, String frameTitle,
			boolean resizable, int closeOperation) {
		ShowImage frame = new ShowImage(frameTitle, frameSize.height, frameSize.width);
		frame.setResizable(resizable);

		/*
		 * This is a bad way to access the window, but due to legacy stuff I
		 * won't change the access patterns
		 */
		frame.Window.setDefaultCloseOperation(closeOperation);
		frame.showImageOriginal(mat);
	}


}
