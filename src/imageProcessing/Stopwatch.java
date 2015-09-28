package imageProcessing;

public class Stopwatch {
	private long startTime;
	private long stopTime;
	
	public void start () {
		startTime = System.currentTimeMillis();
	}
	
	public void stop () {
		stopTime = System.currentTimeMillis();
	}
	
	public long getElapsedMilliseconds () {
		return stopTime - startTime;
	}
	
	public double getElapsedSeconds () {
		return getElapsedMilliseconds() / 1000D;
	}
}
