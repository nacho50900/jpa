package uo.ri.util.random;

public class Random {
	
	private Random() {}

	public static int inRange(int min, int max) {
		return (int) inRange((double) min, (double) max);
	}

	public static long inRange(long min, long max) {
		return (long) inRange((double) min, (double) max);
	}

	public static String string(int length) {
		String res = "";
		for(int i = 0; i < length; i++) {
			res += (char) Random.inRange('A', 'Z');
		}
		return res;
	}

	public static String string(char min, char max, int length) {
		String res = "";
		for(int i = 0; i < length; i++) {
			res += (char) Random.inRange(min, max+1);
		}
		return res;
	}

	public static double inRange(double min, double max) {
		return Math.random() * (max - min) + min;
	}

	public static int choose(int... options) {
		return options[Random.inRange(0, options.length)];
	}

}